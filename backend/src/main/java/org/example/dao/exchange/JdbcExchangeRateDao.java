package org.example.dao.exchange;

import org.example.model.ExchangeRate;
import org.example.repository.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateDao implements ExchangeRateDao {

    private static final String INSERT = """
            INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)
            """;

    private static final String FIND_BY_ID = """
            SELECT id, base_currency_id, target_currency_id, rate 
            FROM exchange_rates 
            WHERE id = ?
            """;

    private static final String FIND_BY_CURRENCY_IDS = """ 
            SELECT id, base_currency_id, target_currency_id, rate
            FROM exchange_rates
            WHERE base_currency_id = ? AND target_currency_id = ?
            """;

    private static final String FIND_ALL = """
            SELECT id, base_currency_id, target_currency_id, rate
            FROM exchange_rates
            """;

    private static final String UPDATE = """
            UPDATE exchange_rates SET rate = ?
            WHERE id = ?
            """;

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {

        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, exchangeRate.baseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.targetCurrencyId());
            preparedStatement.setBigDecimal(3, exchangeRate.rate());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                int id = resultSet.getInt(1);
                return new ExchangeRate(id, exchangeRate.baseCurrencyId(), exchangeRate.targetCurrencyId(),
                        exchangeRate.rate()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save exchange rate", e);
        }
    }

    @Override
    public Optional<ExchangeRate> findById(int id) {

        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(map(resultSet));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find exchange rate by id", e);
        }
    }

    @Override
    public Optional<ExchangeRate> findByCurrencyId(int baseCurrencyId, int targetCurrencyId) {

        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CURRENCY_IDS)) {

            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(map(resultSet));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find exchange rate by currency pair", e);
        }
    }

    @Override
    public List<ExchangeRate> findAll() {

        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<ExchangeRate> exchangeRates = new ArrayList<>();

            while (resultSet.next()) {
                exchangeRates.add(map(resultSet));
            }

            return exchangeRates;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all exchange rates", e);
        }
    }

    @Override
    public void updateRate(int id, ExchangeRate exchangeRate) {
        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setBigDecimal(1, exchangeRate.rate());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update exchange rate", e);
        }
    }

    private ExchangeRate map(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getInt("id"),
                resultSet.getInt("base_currency_id"),
                resultSet.getInt("target_currency_id"),
                resultSet.getBigDecimal("rate")
        );
    }
}