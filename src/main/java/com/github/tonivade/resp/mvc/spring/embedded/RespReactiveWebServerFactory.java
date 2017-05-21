package com.github.tonivade.resp.mvc.spring.embedded;

import org.springframework.boot.web.reactive.server.AbstractReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;

import com.github.tonivade.resp.RespServer;
import com.github.tonivade.resp.command.CommandSuite;
import com.github.tonivade.resp.mvc.spring.server.RespMvcServer;

public class RespReactiveWebServerFactory extends AbstractReactiveWebServerFactory {

  @Override
  public WebServer getWebServer(HttpHandler httpHandler) {
    RespServer server = new RespServer(getAddress().getHostAddress(), getPort(), new CommandSuite());
    ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
    return new RespMvcServer(server, handlerAdapter);
  }
}
