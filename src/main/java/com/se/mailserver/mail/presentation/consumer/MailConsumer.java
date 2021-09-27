package com.se.mailserver.mail.presentation.consumer;

import com.google.gson.Gson;
import com.se.mailserver.mail.application.dto.MailSendDto;
import com.se.mailserver.mail.application.service.MailSendService;
import java.util.List;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class MailConsumer {
  private final MailSendService mailSendService;
  private final Gson gson;

  public MailConsumer(MailSendService mailSendService) {
    this.mailSendService = mailSendService;
    this.gson = new Gson();
  }

  @KafkaListener(topics = "${kafka.topic.mail.name}",
      groupId = "${kafka.topic.mail.default-group}",
      concurrency = "${kafka.topic.mail.concurrency}",
      containerFactory = "kafkaListenerContainerFactory")
  public void listen(List<String> records, Acknowledgment ack) {
    for(String record: records){
      mailSendService.send(gson.fromJson(record, MailSendDto.class));
      ack.acknowledge();
    }
  }
}
