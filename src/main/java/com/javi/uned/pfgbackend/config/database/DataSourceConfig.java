package com.javi.uned.pfgbackend.config.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // Host
    @Value("${DB_HOST:localhost}")
    private String dbHost;

    // Port
    @Value("${DB_PORT:3307}")
    private String dbPort;

    // Database user
    @Value("${DB_USER:pfg}")
    private String dbUser;

    // Database password
    @Value("${DB_PASS:47921093pP}")
    private String dbPass;

    // Database schema
    @Value("${DB_SCHEMA:melodia}")
    private String dbSchema;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true", dbHost, dbPort, dbSchema);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(dbUser);
        dataSourceBuilder.password(dbPass);
        return dataSourceBuilder.build();
    }

}
