package com.ski.springboot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring boot initializer
 * @Author : sunil.mohanty@live.com
 */

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    @Autowired
    private Environment environment;

    public static void main( String[] args ){
        System.setProperty("input", "file:/" + new File("C:/Users/609777365/workspace/Demo/git/spring/JPAWithSpringBoot/src/main/resources/students.csv").getAbsolutePath());
        System.setProperty("output", "file:/" + new File("C:/Users/609777365/workspace/Demo/git/spring/JPAWithSpringBoot/src/main/resources/age.csv").getAbsolutePath());
        SpringApplication.exit(SpringApplication.run(Application.class, args));
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws PropertyVetoException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.ski.springboot.pojo");
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(hibernateJpaVendorAdapter);

        /*
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", environment.getProperty("spring.jpa.properties.primary.hibernate.dialect"));
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("spring.jpa.properties.primary.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.show_sql", environment.getProperty("spring.jpa.properties.hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getProperty("spring.jpa.properties.hibernate.format_sql"));
        properties.put("hibernate.order_inserts", environment.getProperty("spring.jpa.properties.hibernate.order_inserts"));
        properties.put("hibernate.order_updates", environment.getProperty("spring.jpa.properties.hibernate.order_updates"));
        properties.put("hibernate.jdbc.batch_size", environment.getProperty("spring.jpa.properties.hibernate.jdbc.batch_size"));
        properties.put("hibernate.cache.use_query_cache", environment.getProperty("spring.jpa.properties.hibernate.cache.use_query_cache"));
        properties.put("hibernate.cache.use_second_level_cache", environment.getProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache"));

        em.setJpaPropertyMap(properties);
        */
        return em;
    }

    /*
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/c1adb?profileSQL=true");
        //jdbc:mysql://localhost:3333/batch?useSSL=false
        dataSource.setUsername("root");
        dataSource.setPassword("newpass1");

        return dataSource;
    }*/
}
