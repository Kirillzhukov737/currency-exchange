package src.main.java.org.example.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnector {

    private static final String PROPERTIES = "db.properties";
    private static final DataSource DATA_SOURCE;

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = DatabaseConnector.class.getClassLoader().getResourceAsStream(PROPERTIES)) {
            if (inputStream == null) {
                throw new RuntimeException("Properties file " + PROPERTIES + " not found");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error database " + e);
        }

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(properties.getProperty("db.driver"));
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("pool.size")));
        hikariConfig.setConnectionTimeout(Long.parseLong(properties.getProperty("pool.timeout")));

        DATA_SOURCE = new HikariDataSource(hikariConfig);
    }
}