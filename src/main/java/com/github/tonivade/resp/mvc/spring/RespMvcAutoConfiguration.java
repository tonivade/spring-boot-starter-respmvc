/*
 * Copyright (c) 2015-2025, Antonio Gabriel Muñoz Conejo <me at tonivade dot es>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.resp.mvc.spring;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.tonivade.resp.RespServer;
import com.github.tonivade.resp.RespServerContext;
import com.github.tonivade.resp.command.CommandSuite;
import com.github.tonivade.resp.command.CommandWrapperFactory;

@Configuration
@ConditionalOnClass(RespServer.class)
@EnableConfigurationProperties(RespMvcProperties.class)
public class RespMvcAutoConfiguration {

  @Bean
  public RespServerContext respServerContext(RespMvcProperties properties, CommandSuite commandSuite) {
    return new RespServerContext(properties.getHost(), properties.getPort(), commandSuite);
  }

  @Bean(initMethod = "start", destroyMethod = "stop")
  public RespServer server(RespServerContext serverContext) {
    return new RespServer(serverContext);
  }

  @Bean
  @ConditionalOnMissingBean
  public CommandSuite defaultCommandSuite(ApplicationContext context) {
    return new SpringCommandSuite(context);
  }

  @Bean
  @ConditionalOnMissingBean
  public CommandWrapperFactory defaultCommandWrapperFactory(AutowireCapableBeanFactory factory) {
    return new SpringCommandWrapperFactory(factory);
  }
}
