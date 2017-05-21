package com.github.tonivade.resp.mvc.spring.server;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;

import com.github.tonivade.resp.RespServer;

public class RespMvcServer implements WebServer {

  private RespServer server;
  private ReactorHttpHandlerAdapter handlerAdapter;

  public RespMvcServer(RespServer server, ReactorHttpHandlerAdapter handlerAdapter) {
    this.server = server;
    this.handlerAdapter = handlerAdapter;
  }

  @Override
  public void start() throws WebServerException {
    server.start();
  }

  @Override
  public void stop() throws WebServerException {
    server.stop();
  }

  @Override
  public int getPort() {
    return server.getPort();
  }
}
