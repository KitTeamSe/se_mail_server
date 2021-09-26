package com.se.mailserver.common.domain.exception;

import org.springframework.http.HttpStatus;

public class ConnectionFailedException extends SeException{
  public ConnectionFailedException( String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

  public ConnectionFailedException(String message, Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
  }

  public ConnectionFailedException(Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, cause);
  }
}

