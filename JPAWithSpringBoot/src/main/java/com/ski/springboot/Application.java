package com.ski.springboot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Spring boot initializer
 * @Author : sunil.mohanty@live.com
 */

@SpringBootApplication
public class Application {

    @Autowired
    DataSource dataSource;

    public static void main( String[] args ){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/c1adb?profileSQL=true");
        //jdbc:mysql://localhost:3333/batch?useSSL=false
        dataSource.setUsername("root");
        dataSource.setPassword("newpass1");

        return dataSource;
    }
}
