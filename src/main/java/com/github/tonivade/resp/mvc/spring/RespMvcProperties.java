/*
 * Copyright (c) 2015-2025, Antonio Gabriel Muñoz Conejo <me at tonivade dot es>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.resp.mvc.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("resp")
public class RespMvcProperties {

  private String host = "localhost";
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
