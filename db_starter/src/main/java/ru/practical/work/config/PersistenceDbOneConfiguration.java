package ru.practical.work.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "dboneEntityManagerFactory",
        transactionManagerRef = "dboneTransactionManager",
        basePackages = {"ru.practical.work.dbone.repository"}
)
public class PersistenceDbOneConfiguration {

    @Bean(name = "dboneDataSource")
    @ConfigurationProperties(prefix = "spring.dbone.datasource")
    public DataSource dboneDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dboneEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dboneEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dboneDataSource") DataSource dboneDataSource) {
        return builder
                .dataSource(dboneDataSource)
                .packages("ru.practical.work.dbone.entity")
                .build();
    }

    @Bean(name = "dboneTransactionManager")
    public PlatformTransactionManager dboneTransactionManager(
            @Qualifier("dboneEntityManagerFactory") EntityManagerFactory dboneEntityManagerFactory) {
        return new JpaTransactionManager(dboneEntityManagerFactory);
    }
}
