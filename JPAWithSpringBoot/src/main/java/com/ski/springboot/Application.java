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
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.File;

/**
 * Spring boot initializer
 * @Author : sunil.mohanty@live.com
 */

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    @Autowired
    private Environment environment;

    @Autowired
    private ResourceLoader resourceLoader;

    public static void main( String[] args ){
        //@Value(value = "classpath:xmlToParse/companies.xml")
        System.setProperty("output", "file://" + new File("/Users/sunilmohanty/Documents/MyDrive/projects/java/spring/intellij-workspace/JPAWithSpringBoot/student-details.csv").getAbsolutePath());
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

        /* Below JPA property setting mechanism is used it you want to customize the defult one and if you are interested to use the java way instead of via application.yml / application.properties
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

    /* Use the below data source configuration ,if you want to use java based configuration instead of useing application.yml or application.properties.

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
