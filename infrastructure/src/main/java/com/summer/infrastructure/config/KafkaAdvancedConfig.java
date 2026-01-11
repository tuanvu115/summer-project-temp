//package com.summer.infrastructure.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.*;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Cách 2: Advanced Kafka Configuration - Custom Producer & Consumer
// *
// * Dùng khi cần:
// * - Customize Producer settings (retries, compression, etc.)
// * - Customize Consumer settings (concurrency, offset management)
// * - Multiple Kafka templates cho different topics
// * - Custom error handling
// *
// * Để sử dụng config này thay vì KafkaConfig:
// * 1. Comment/xóa KafkaConfig.java
// * 2. Uncomment class này
// * 3. Set property trong application.yml: infrastructure.kafka.advanced-config=true
// */
//@Configuration
//@EnableKafka
//@ConditionalOnProperty(name = "infrastructure.kafka.advanced-config", havingValue = "true", matchIfMissing = false)
//public class KafkaAdvancedConfig {
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Value("${spring.kafka.consumer.group-id}")
//    private String consumerGroupId;
//
//    /**
//     * Producer Configuration
//     */
//    @Bean
//    public ProducerFactory<String, Object> producerFactory() {
//        Map<String, Object> config = new HashMap<>();
//
//        // Bootstrap servers
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//
//        // Serializers
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//
//        // Reliability settings
//        config.put(ProducerConfig.ACKS_CONFIG, "all"); // Wait for all replicas
//        config.put(ProducerConfig.RETRIES_CONFIG, 3);
//        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
//
//        // Performance settings
//        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        config.put(ProducerConfig.LINGER_MS_CONFIG, 10);
//        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
//
//        // Idempotence for exactly-once semantics
//        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
//
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    /**
//     * KafkaTemplate for sending messages
//     */
//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    /**
//     * Consumer Configuration
//     */
//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory() {
//        Map<String, Object> config = new HashMap<>();
//
//        // Bootstrap servers
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//
//        // Consumer group
//        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
//
//        // Deserializers
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//
//        // Offset management
//        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Manual commit
//
//        // Fetching settings
//        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
//        config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
//
//        // JSON Deserializer settings
//        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.summer.*");
//        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
//        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.summer.infrastructure.messaging.event.TransactionEvent");
//
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
//
//    /**
//     * Kafka Listener Container Factory
//     * Customize listener behavior
//     */
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
//            new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(consumerFactory());
//
//        // Concurrency - number of consumer threads
//        factory.setConcurrency(3);
//
//        // Manual acknowledgment mode
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//
//        // Error handling
//        factory.setCommonErrorHandler(new org.springframework.kafka.listener.DefaultErrorHandler());
//
//        return factory;
//    }
//}
