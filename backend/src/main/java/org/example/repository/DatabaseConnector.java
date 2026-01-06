package org.example.repository;

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

        try (InputStream in = DatabaseConnector.class.getClassLoader().getResourceAsStream(PROPERTIES)) {
            if (in == null) throw new RuntimeException("Properties file " + PROPERTIES + " not found");
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Error database", e);
        }

        String url = pick(System.getenv("DB_URL"), properties.getProperty("db.url"));
        String username = pick(System.getenv("DB_USERNAME"), properties.getProperty("db.username"));
        String password = pick(System.getenv("DB_PASSWORD"), properties.getProperty("db.password"));

        String poolSize = pick(System.getenv("POOL_SIZE"), properties.getProperty("pool.size"));
        String poolTimeout = pick(System.getenv("POOL_TIMEOUT"), properties.getProperty("pool.timeout"));

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(properties.getProperty("db.driver"));
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(Integer.parseInt(poolSize));
        hikariConfig.setConnectionTimeout(Long.parseLong(poolTimeout));

        DATA_SOURCE = new HikariDataSource(hikariConfig);
    }

    private static String pick(String env, String prop) {
        return (env != null && !env.isBlank()) ? env : prop;
    }
}
