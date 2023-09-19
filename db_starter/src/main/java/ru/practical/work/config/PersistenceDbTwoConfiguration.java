package ru.practical.work.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "dbtwoEntityManager",
        transactionManagerRef = "dbtwoTransactionManager",
        basePackages = "ru.practical.work.dbtwo.repository")
public class PersistenceDbTwoConfiguration {

    @Bean(name = "dbtwoDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.dbtwo.datasource")
    public DataSource dbtwoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "dbtwoEntityManager")
    public LocalContainerEntityManagerFactoryBean dbtwoEntityManager(EntityManagerFactoryBuilder builder,
                                                                     @Qualifier("dbtwoDataSource") DataSource accountDataSource) {
        return builder
                .dataSource(accountDataSource)
                .packages("ru.practical.work.dbtwo.entity")
                .build();
    }


    @Bean(name = "dbtwoTransactionManager")
    public PlatformTransactionManager dbtwoTransactionManager(
            @Qualifier("dbtwoEntityManager") EntityManagerFactory dbtwoEntityManagerFactory) {
        return new JpaTransactionManager(dbtwoEntityManagerFactory);
    }


}
