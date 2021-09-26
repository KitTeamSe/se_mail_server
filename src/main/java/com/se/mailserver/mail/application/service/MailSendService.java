package com.se.mailserver.mail.application.service;

import com.se.mailserver.common.domain.exception.ConnectionFailedException;
import com.se.mailserver.common.domain.exception.InvalidInputException;
import com.se.mailserver.common.domain.exception.SeException;
import com.se.mailserver.mail.application.dto.MailSendDto;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailSendService {
  private final JavaMailSender javaMailSender;

  public MailSendService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void send(MailSendDto mailSendDto) {
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setTo(mailSendDto.getRcpt());
      helper.setSubject(mailSendDto.getSubject());
      helper.setText(mailSendDto.getText(), true);
      helper.setFrom(mailSendDto.getFrom());

      javaMailSender.send(mimeMessage);
    }catch (MailSendException e){
      if(e.getCause() instanceof MessagingException){
        throw new ConnectionFailedException("SMTP server connection failed");
      }

      if(e.getFailedMessages().values().iterator().next() instanceof SendFailedException){
        throw new InvalidInputException("Invalid address");
      }
      throw new SeException(HttpStatus.INTERNAL_SERVER_ERROR, "UnExpected Error");
    }catch (MessagingException e){
      throw new InvalidInputException("Attributes is invalid");
    }catch (IllegalArgumentException e){
      throw new InvalidInputException("Attributes must not be null");
    }
  }
}
