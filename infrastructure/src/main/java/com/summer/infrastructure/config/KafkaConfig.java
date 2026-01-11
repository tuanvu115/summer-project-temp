package com.summer.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Kafka Configuration for Infrastructure Layer.
 *
 * Cách 1: Basic Configuration with Spring Boot Auto-configuration
 *
 * Configuration này sẽ được Spring Boot tự động phát hiện khi:
 * - PresentationApplication có @SpringBootApplication(scanBasePackages = "com.summer")
 * - Spring component scanning sẽ tìm thấy @Configuration này
 *
 * Responsibilities:
 * - Enable Kafka listeners
 * - Configure Kafka integration
 *
 * Tất cả Kafka properties được config trong application.yml:
 * - bootstrap-servers
 * - serializers/deserializers
 * - consumer group-id
 * - producer settings
 */
@Configuration
@EnableKafka
public class KafkaConfig {
    // Spring Boot auto-configuration sẽ tự động tạo:
    // - KafkaTemplate
    // - ProducerFactory
    // - ConsumerFactory
    // - Listener Container Factory
}
