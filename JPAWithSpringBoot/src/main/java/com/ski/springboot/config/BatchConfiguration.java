package com.ski.springboot.config;

import com.ski.springboot.pojo.Student;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean(name = "csvtodbUpdator")
    public Step csvtodbUpdator(StepBuilderFactory stepBuilderFactory, FlatFileItemReader fileReader, JdbcBatchItemWriter jdbcWriter
    )  {
        try {
            return  stepBuilderFactory.get("file-db")
                    .<Student, Student>chunk(100)
                    .reader(fileReader(null))
                    .writer(jdbcWriter(null))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean(name = "dbToFile")
    public Step dbToFile(StepBuilderFactory stepBuilderFactory, JdbcCursorItemReader dbReader, FlatFileItemWriter fileItemWriter)  {
        try {
            return stepBuilderFactory.get("db-file")
                    .<Student, Student>chunk(100)
                    .reader(dbReader(null))
                    .processor(studentProcessor())
                    .writer(fileItemWriter(null))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public FlatFileItemReader<Student> fileReader(@Value(value = "students.csv") Resource resource){
        try {
            return new FlatFileItemReaderBuilder<Student>()
                    .name("file-reader")
                    .resource(resource)
                    .targetType(Student.class)
                    .linesToSkip(1)
                    .delimited().delimiter(",").names(new String[]{"firstName", "lastName",  "email", "age"}).build();
        } catch (Exception e) {
            System.out.println("in the exception block");
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    @Autowired
    public JdbcBatchItemWriter<Student> jdbcWriter(DataSource dataSource) {
            return new JdbcBatchItemWriterBuilder<Student>()
                    .dataSource(dataSource)
                    .sql("insert into STUDENT( FIRST_NAME, LAST_NAME, EMAIL, AGE) values (:firstName, :lastName, :email, :age)")
                    .beanMapped()
                    .build();
    }


    @Bean
    @Autowired
    public JdbcCursorItemReader<Student> dbReader(DataSource dataSource) {
        System.out.println("in the JdbcCursorItemReader");
        return new JdbcCursorItemReaderBuilder<Student>()
                .dataSource(dataSource)
                .sql("SELECT FIRST_NAME, LAST_NAME, EMAIL, AGE FROM STUDENT")
                .rowMapper(( rs,  rowNum) -> {

                    Student student = new Student(rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"), rs.getString("EMAIL"), rs.getInt("AGE"));
                    return student;
                })
                .name("db-reader")
                .build();
    }

    @Bean
    public CompositeItemProcessor<Student, Student>  studentProcessor() {
        CompositeItemProcessor compositeItemProcessor = new CompositeItemProcessor ();
        compositeItemProcessor.setDelegates(delegates());
        return compositeItemProcessor;
    }

    List<ItemProcessor<Student, Student>> delegates () {
        List<ItemProcessor<Student, Student>> itemProcessors = new ArrayList<>();

        ItemProcessor<Student, Student> itemProcessor = new ItemProcessor<Student, Student> (){

            @Override
            public Student process(Student student) throws Exception {
                 student.setFirstName( "Hello, " + student.getFirstName());
                 student.setLastName( " " + student.getLastName());
                 student.setEmail(" your email id is : " + student.getEmail());
                 return student;
            }
        };
        itemProcessors.add(itemProcessor);
        return itemProcessors;
    }

    @Bean
    public FlatFileItemWriter<Student> fileItemWriter(@Value("${output}") Resource resource) throws Exception {

        return new FlatFileItemWriterBuilder<Student>()
                .resource(resource)
                .append(true)
                .name("file-writer")
                .lineAggregator(createStudentLineAggregator())
                .saveState(true)

               .build();
    }

    private DelimitedLineAggregator<Student> createStudentLineAggregator() {

        return new DelimitedLineAggregator<Student>() {
            {
                setDelimiter(",");
                setFieldExtractor(resultSet -> {
                    return new Object[]{new Student(resultSet.getFirstName(), resultSet.getLastName(), resultSet.getEmail(),resultSet.getAge())};
                });
            }
        };
    }


    @Bean
    public Job  infoUpdator(JobBuilderFactory jobBuilderFactory, Step csvtodbUpdator, Step dbToFile) {
        return jobBuilderFactory.get("infoUpdator").preventRestart()
                .incrementer(new RunIdIncrementer())
                .start(csvtodbUpdator)
                .next(dbToFile)
                .build();
    }
}
