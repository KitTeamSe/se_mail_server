package com.se.mailserver.common.infra.kafka.config;

import java.util.Collection;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;


public class SaveOffsetsOnRebalance implements ConsumerAwareRebalanceListener {

  @Override
  public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer,
      Collection<TopicPartition> partitions) {
      consumer.commitSync();
  }

  @Override
  public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
    for(TopicPartition partition: partitions) {
      consumer.seek(partition, consumer.position(partition));
    }
  }
}
