package com.ski.springboot.config;

import com.ski.springboot.pojo.Student;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean(name = "csvtodbUpdator")
    public Step csvtodbUpdator(StepBuilderFactory stepBuilderFactory, FlatFileItemReader fileReader, JdbcBatchItemWriter jdbcWriter
    ) throws Exception {
        return  stepBuilderFactory.get("file-db")
                .<Student, Student>chunk(100)
                .reader(fileReader(null))
                .writer(jdbcWriter(null))
                .build();
    }

    @Bean
    public FlatFileItemReader<Student> fileReader(@Value("${input}") Resource resource) throws Exception{
        return new FlatFileItemReaderBuilder<Student>()
                .name("file-reader")
                .resource(resource)
                .targetType(Student.class)
                .delimited().delimiter(",").names(new String[]{"firstName", "lastName",  "email", "age"}).build();
    }

    @Bean
    public JdbcBatchItemWriter<Student> jdbcWriter(DataSource datasource) {
        return new JdbcBatchItemWriterBuilder<Student>()
                .dataSource(datasource)
                .sql("insert into STUDENT( FIRST_NAME, LAST_NAME, EMAIL, AGE) values (:firstName, :lastName, :email, :age)")
                .beanMapped()
                .build();
    }

    @Bean
    public Job  infoUpdator(JobBuilderFactory jobBuilderFactory, Step csvtodbUpdator) {
        return jobBuilderFactory.get("infoUpdator").preventRestart()
                .incrementer(new RunIdIncrementer())
                .start(csvtodbUpdator)
                .build();
    }
}
