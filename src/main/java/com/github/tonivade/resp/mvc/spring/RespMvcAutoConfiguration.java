package com.github.tonivade.resp.mvc.spring;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.tonivade.resp.RespServer;
import com.github.tonivade.resp.command.CommandSuite;
import com.github.tonivade.resp.command.CommandWrapperFactory;

@Configuration
@ConditionalOnClass(RespServer.class)
public class RespMvcAutoConfiguration {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public RespServer server(RespMvcConfig config, CommandSuite commands) {
    return new RespServer(config.getHost(), config.getPort(), commands);
  }

  @Bean
  @ConditionalOnBean
  public CommandSuite commandSuite(CommandWrapperFactory factory) {
    return new CommandSuite(factory);
  }

  @Bean
  @ConditionalOnBean
  public CommandWrapperFactory commandWrapperFactory(AutowireCapableBeanFactory factory) {
    return new SpringCommandWrapperFactory(factory);
  }
}
