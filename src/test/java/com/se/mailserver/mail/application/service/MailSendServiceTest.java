package com.se.mailserver.mail.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.se.mailserver.common.domain.exception.ConnectionFailedException;
import com.se.mailserver.common.domain.exception.InvalidInputException;
import com.se.mailserver.common.domain.exception.SeException;
import com.se.mailserver.mail.application.dto.MailSendDto;
import java.util.Collections;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MailSendServiceTest {

  @Mock
  private JavaMailSender javaMailSender;

  @InjectMocks
  private MailSendService mailSendService;

  private static MailSendDto mailSendDto;

  @BeforeAll
  public static void setUp(){
    mailSendDto = new MailSendDto(
        "rcpt@kumoh.ac.kr",
        "from@kumoh.ac.kr",
        "subject",
        "text");
  }

  @Test
  public void 메일_송신_성공() throws Exception{
    //given
    when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
    //when
    //then
    assertDoesNotThrow(() -> mailSendService.send(mailSendDto));
  }

  @Test
  public void 메일_송신_실패_NULL_포함() throws Exception{
    //given
    MailSendDto mailSendDtoWithNull = new MailSendDto(null, null, null, null);
    when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
    //when
    InvalidInputException ex = assertThrows(InvalidInputException.class, () -> mailSendService.send(mailSendDtoWithNull));
    //then
    assertEquals("Attributes must not be null", ex.getMessage());
  }

  @Test
  public void 메일_송신_실패_유효하지_않은_정보() throws Exception{
    //given
    MailSendDto mailSendDtoWithNull = new MailSendDto("@", null, null, null);
    when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
    //when
    InvalidInputException ex = assertThrows(InvalidInputException.class, () -> mailSendService.send(mailSendDtoWithNull));
    //then
    assertEquals("Attributes is invalid", ex.getMessage());
  }

  @Test
  public void 메일_송신_실패_연결_불량() throws Exception{
    //given
    MailSendException mailSendException = mock(MailSendException.class);
    when(mailSendException.getCause()).thenReturn(mock(MessagingException.class));
    when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
    doThrow(mailSendException).when(javaMailSender).send(any(MimeMessage.class));
    //when
    ConnectionFailedException ex = assertThrows(ConnectionFailedException.class, () -> mailSendService.send(mailSendDto));
    //then
    assertEquals("SMTP server connection failed", ex.getMessage());
  }

  @Test
  public void 메일_송신_실패_수신자_불일치() throws Exception{
    //given
    MailSendException mailSendException = new MailSendException("msg", null, Collections.singletonMap(mock(Object.class), mock(SendFailedException.class)));
    when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
    doThrow(mailSendException).when(javaMailSender).send(any(MimeMessage.class));
    //when
    InvalidInputException ex = assertThrows(InvalidInputException.class, () -> mailSendService.send(mailSendDto));
    //then
    assertEquals("Invalid address", ex.getMessage());
  }

  @Test
  public void 메일_송신_실패_기타() throws Exception{
    //given
    MailSendException mailSendException = new MailSendException("msg", null, Collections.singletonMap(mock(Object.class), mock(NullPointerException.class)));
    when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
    doThrow(mailSendException).when(javaMailSender).send(any(MimeMessage.class));
    //when
    SeException ex = assertThrows(SeException.class, () -> mailSendService.send(mailSendDto));
    //then
    assertEquals("UnExpected Error", ex.getMessage());
  }
}