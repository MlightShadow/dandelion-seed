package com.company.project.configurer;

import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DataSourceConfigurer {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.main")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.auth")
    public DataSource authDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    public JdbcOperations mainJdbcOperations(@Qualifier("mainDataSource") DataSource mainDataSource) {
        return new JdbcTemplate(mainDataSource);
    }

    @Bean
    public JdbcOperations authJdbcOperations(@Qualifier("authDataSource")DataSource authDataSource) {
        return new JdbcTemplate(authDataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
    @Bean
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = Maps.newHashMap();
        targetDataSources.put("main", mainDataSource());
        targetDataSources.put("auth", authDataSource());

        dynamicDataSource.setDefaultTargetDataSource(mainDataSource());

        dynamicDataSource.setTargetDataSources(targetDataSources);

        DataSourceContextHolder.supportList.addAll(targetDataSources.keySet());

        return dynamicDataSource;
    }
}