package com.se.mailserver.mail.presentation.presenter;

import com.se.mailserver.common.presentation.response.Response;

public interface MailPresenter {
  Response<Void> sendMail();
}
