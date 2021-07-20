package com.hy.zookeeper.config.systemconfig;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hy.zookeeper.config.base.BaseRepositoryFactoryBean;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.hy.zookeeper.config",
        transactionManagerRef = "jpaTransactionManager",
        entityManagerFactoryRef = "localContainerEntityManagerFactoryBean",
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class
)
@EnableTransactionManagement
public class JpaConfiguration {
	

	
    @Autowired
    @Bean
    public JpaTransactionManager jpaTransactionManager(@Qualifier(value = "EmbeddeddataSource") DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        jpaTransactionManager.setDataSource(dataSource);
        return jpaTransactionManager;
    }

    @Autowired
    @Bean
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(@Qualifier(value = "EmbeddeddataSource") DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
    	LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.hy.zookeeper.config");
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        hibernateJpaVendorAdapter.setShowSql(false);
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.DmDialect");
        return hibernateJpaVendorAdapter;
    }
    
}