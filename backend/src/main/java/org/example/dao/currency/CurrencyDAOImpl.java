package org.example.dao.currency;

import org.example.model.Currency;
import org.example.repository.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAOImpl implements CurrencyDAO {

    private static final String FIND_ALL = """
            SELECT id, code, full_name, sign FROM currencies
            """;
    private static final String INSERT = """
            INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)
            """;

    private static final String FIND_BY_ID = """
            SELECT id, code, full_name, sign
            FROM currencies 
            WHERE id = ?
            """;

    private static final String FIND_BY_CODE = """
            "SELECT id, code, full_name, sign
            FROM currencies 
            WHERE code = ?
            """;


    private Currency map(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign"));
    }

    @Override
    public List<Currency> findAll() {

        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Currency> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(map(resultSet));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all currencies", e);
        }
    }

    @Override
    public Currency save(Currency currency) {

        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, currency.code());
            preparedStatement.setString(2, currency.fullName());
            preparedStatement.setString(3, currency.sign());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                int id = resultSet.getInt(1);
                return new Currency(id, currency.code(), currency.fullName(), currency.sign());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save currency", e);
        }
    }

    @Override
    public Optional<Currency> findById(int id) {

        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(map(resultSet));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find currency by id", e);
        }
    }

    @Override
    public Optional<Currency> findByCode(String code) {

        try (Connection connection = DatabaseConnector.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {

            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(map(resultSet));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find currency by code", e);
        }
    }
}