package com.summer.infrastructure.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA Configuration for Infrastructure Layer.
 *
 * Cách 1: Basic Configuration with Spring Boot Auto-configuration
 *
 * Configuration này sẽ được Spring Boot tự động phát hiện khi:
 * - PresentationApplication có @SpringBootApplication(scanBasePackages = "com.summer")
 * - Spring component scanning sẽ tìm thấy @Configuration này
 *
 * Responsibilities:
 * - Enable JPA repositories trong infrastructure layer
 * - Enable transaction management
 *
 * Note: @EntityScan không cần thiết vì Spring Boot auto-configuration
 * sẽ tự động scan entities trong classpath khi có @EnableJpaRepositories
 *
 * Tất cả các properties (datasource, hibernate, entity scan, etc.)
 * được config trong application.yml
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.summer.infrastructure.persistence.repository"
)
@EntityScan(basePackages = "com.summer.infrastructure.persistence.entity")
@EnableTransactionManagement
public class JpaConfig {
    // Spring Boot auto-configuration sẽ tự động tạo:
    // - DataSource từ application.yml
    // - EntityManagerFactory (tự động scan entities trong packages)
    // - TransactionManager
    // - Hibernate properties
}
