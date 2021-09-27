package com.se.mailserver.mail.presentation.presenter;

import com.se.mailserver.common.presentation.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class MailPresenterFormatter implements MailPresenter{

  @Override
  public Response<Void> sendMail() {
    return new Response<>(HttpStatus.OK, "메일이 성공적으로 송신 되었습니다.");
  }
}
