package com.datn.datn_mangostore;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
public class DatnMangostoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatnMangostoreApplication.class, args);
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog("classpath:databaseTools/changelog-master.xml");
        springLiquibase.setDataSource(dataSource);
        return springLiquibase;
    }
}
