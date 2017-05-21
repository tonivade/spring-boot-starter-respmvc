package com.github.tonivade.resp.mvc.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("resp")
public class RespMvcConfig {

  private String host = "locahost";
  private Integer port = 7081;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }
}
