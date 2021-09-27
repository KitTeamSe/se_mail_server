package com.se.mailserver.mail.presentation.api;

import com.se.mailserver.common.presentation.response.Response;
import com.se.mailserver.mail.application.dto.MailSendDto;
import com.se.mailserver.mail.application.service.MailSendService;
import com.se.mailserver.mail.presentation.presenter.MailPresenter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail/v1")
@Api(tags = "메일 관리")
public class MailApiController {
  private final MailSendService mailSendService;
  private final MailPresenter mailPresenter;

  public MailApiController(MailSendService mailSendService,
      MailPresenter mailPresenter) {
    this.mailSendService = mailSendService;
    this.mailPresenter = mailPresenter;
  }

  @PostMapping(path = "/mail")
  @ResponseStatus(value = HttpStatus.OK)
  @ApiOperation(value = "메일 송신")
  public Response<Void> sendMail(@RequestBody @Validated MailSendDto mailSendDto) {
    mailSendService.send(mailSendDto);
    return mailPresenter.sendMail();
  }
}
