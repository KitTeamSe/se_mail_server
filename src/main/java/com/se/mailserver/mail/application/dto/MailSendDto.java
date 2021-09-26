package com.se.mailserver.mail.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel("회원 가입 요청")
public class MailSendDto {
  @ApiModelProperty(example = "rcpt@kumoh.ac.kr", notes = "수신인")
  @Email
  private String rcpt;

  @ApiModelProperty(example = "from@kumoh.ac.kr", notes = "발신인")
  @Email
  private String from;

  @ApiModelProperty(example = "title", notes = "제목")
  @NotBlank
  private String subject;

  @ApiModelProperty(example = "text", notes = "본문")
  @NotBlank
  private String text;

  public MailSendDto(String rcpt, String from, String subject, String text) {
    this.rcpt = rcpt;
    this.from = from;
    this.subject = subject;
    this.text = text;
  }
}