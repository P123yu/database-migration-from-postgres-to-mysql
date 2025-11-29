package com.crackit.springbatch.demo.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // 1. PostgreSQL Configuration (Primary)
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties postgresProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "postgresDataSource")
    @Primary
    public DataSource postgresDataSource() {
        return postgresProperties().initializeDataSourceBuilder().build();
    }

    // 2. MySQL Configuration (Target)
    @Bean
    @ConfigurationProperties("spring.datasource-mysql")
    public DataSourceProperties mysqlProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource() {
        return mysqlProperties().initializeDataSourceBuilder().build();
    }
}