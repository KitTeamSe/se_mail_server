package com.se.mailserver.common.infra.kafka.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

@Configuration
public class KafkaListenerContainerConfiguration {

  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServer;
  @Value("${kafka.auto-offset-commit}")
  private String autoOffsetCommit;
  @Value("${kafka.enable-auto-commit}")
  private boolean enableAutoCommit;
  @Value("${kafka.fetch-min-bytes-mb}")
  private int fetchMinBytesMb;
  @Value("${kafka.fetch-max-wait-ms}")
  private int fetchMaxWaitMs;

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(){
    Map<String, Object> props = new HashMap<>();
    System.out.println(autoOffsetCommit);
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetCommit);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
    props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, fetchMinBytesMb * 1024 * 1024);
    props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs);

    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.getContainerProperties().setConsumerRebalanceListener(new SaveOffsetsOnRebalance());
    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
    factory.getContainerProperties().setAckMode(AckMode.MANUAL);
    factory.setBatchListener(true);
    return factory;
  }
}
