package com.crackit.springbatch.demo.config;

import com.crackit.springbatch.demo.dto.EmployeeDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MigrationJobConfig {

    // 1. Transaction Manager (Must use Primary/Postgres)
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("postgresDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // 2. READER: Reads from PostgreSQL
//    @Bean
//    public JdbcCursorItemReader<EmployeeDto> migrationReader(@Qualifier("postgresDataSource") DataSource postgresDataSource) {
//        return new JdbcCursorItemReaderBuilder<EmployeeDto>()
//                .name("migrationReader")
//                .dataSource(postgresDataSource)
//                .sql("SELECT employee_id, full_name, job_title, department, business_unit, gender, ethnicity, age FROM employee_details")
//                .rowMapper(new BeanPropertyRowMapper<>(EmployeeDto.class))
//                .build();
//    }


    @Bean
    public JdbcCursorItemReader<EmployeeDto> migrationReader(@Qualifier("postgresDataSource") DataSource postgresDataSource) {
        return new JdbcCursorItemReaderBuilder<EmployeeDto>()
                .name("migrationReader")
                .dataSource(postgresDataSource)
                .sql("SELECT employee_id, full_name, job_title, department, business_unit, gender, ethnicity, age FROM employee_details")
                // CHANGE THIS LINE: Use DataClassRowMapper instead of BeanPropertyRowMapper
                .rowMapper(new DataClassRowMapper<>(EmployeeDto.class))

                .build();
    }

    // 3. WRITER: Writes to MySQL
    @Bean
    public JdbcBatchItemWriter<EmployeeDto> migrationWriter(@Qualifier("mysqlDataSource") DataSource mysqlDataSource) {
        return new JdbcBatchItemWriterBuilder<EmployeeDto>()
                .dataSource(mysqlDataSource)
                .sql("INSERT INTO employee_details (employee_id, full_name, job_title, department, business_unit, gender, ethnicity, age) " +
                        "VALUES (:employeeId, :fullName, :jobTitle, :department, :businessUnit, :gender, :ethnicity, :age)")
                .beanMapped() // Automatically maps DTO fields (camelCase) to SQL parameters
                .build();
    }

    // 4. STEP
    @Bean
    public Step migrationStep(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager,
                              @Qualifier("postgresDataSource") DataSource postgresDataSource,
                              @Qualifier("mysqlDataSource") DataSource mysqlDataSource) {
        return new StepBuilder("migrationStep", jobRepository)
                .<EmployeeDto, EmployeeDto>chunk(100, transactionManager)
                .reader(migrationReader(postgresDataSource))
                .writer(migrationWriter(mysqlDataSource))
                .build();
    }

    // 5. JOB
    @Bean(name = "migrationJob")
    public Job migrationJob(JobRepository jobRepository, Step migrationStep) {
        return new JobBuilder("migrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrationStep)
                .build();
    }
}