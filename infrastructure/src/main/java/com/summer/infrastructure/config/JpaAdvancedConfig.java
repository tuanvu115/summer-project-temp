//package com.summer.infrastructure.config;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
///**
// * Cách 2: Advanced JPA Configuration - Custom DataSource & EntityManagerFactory
// *
// * Dùng khi cần:
// * - Customize DataSource (connection pool, multiple datasources)
// * - Fine-tune EntityManagerFactory
// * - Custom TransactionManager
// * - Advanced Hibernate properties
// *
// * Để sử dụng config này thay vì JpaConfig:
// * 1. Comment/xóa JpaConfig.java
// * 2. Uncomment class này
// * 3. Set property trong application.yml: infrastructure.jpa.advanced-config=true
// *
// * Note: Không dùng @EntityScan vì nó từ spring-boot-autoconfigure
// * Thay vào đó set packages trong LocalContainerEntityManagerFactoryBean
// */
//@Configuration
//@EnableJpaRepositories(
//    basePackages = "com.summer.infrastructure.persistence.repository",
//    entityManagerFactoryRef = "entityManagerFactory",
//    transactionManagerRef = "transactionManager"
//)
//@EnableTransactionManagement
//@ConditionalOnProperty(name = "infrastructure.jpa.advanced-config", havingValue = "true", matchIfMissing = false)
//public class JpaAdvancedConfig {
//
//    @Value("${spring.datasource.url}")
//    private String jdbcUrl;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//
//    /**
//     * Custom DataSource với HikariCP configuration
//     */
//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(jdbcUrl);
//        config.setUsername(username);
//        config.setPassword(password);
//        config.setDriverClassName(driverClassName);
//
//        // Connection Pool Settings
//        config.setMaximumPoolSize(20);
//        config.setMinimumIdle(5);
//        config.setConnectionTimeout(30000);
//        config.setIdleTimeout(600000);
//        config.setMaxLifetime(1800000);
//
//        // Performance Settings
//        config.setAutoCommit(false);
//        config.setConnectionTestQuery("SELECT 1");
//        config.setPoolName("TransactionServiceHikariPool");
//
//        return new HikariDataSource(config);
//    }
//
//    /**
//     * Custom EntityManagerFactory
//     * Chỉ định packages để scan entities thay vì dùng @EntityScan
//     */
//    @Bean
//    @Primary
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//
//        // Thay vì @EntityScan, dùng setPackagesToScan
//        em.setPackagesToScan("com.summer.infrastructure.persistence.entity");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setGenerateDdl(false);
//        vendorAdapter.setShowSql(true);
//        em.setJpaVendorAdapter(vendorAdapter);
//
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        properties.setProperty("hibernate.format_sql", "true");
//        properties.setProperty("hibernate.jdbc.batch_size", "20");
//        properties.setProperty("hibernate.order_inserts", "true");
//        properties.setProperty("hibernate.order_updates", "true");
//        properties.setProperty("hibernate.jdbc.batch_versioned_data", "true");
//        properties.setProperty("hibernate.connection.provider_disables_autocommit", "true");
//        em.setJpaProperties(properties);
//
//        return em;
//    }
//
//    /**
//     * Custom TransactionManager
//     */
//    @Bean
//    @Primary
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory);
//        return transactionManager;
//    }
//}
