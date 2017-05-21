package com.github.tonivade.resp.mvc.spring;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.tonivade.resp.RespServer;
import com.github.tonivade.resp.command.CommandSuite;
import com.github.tonivade.resp.command.CommandWrapperFactory;

@Configuration
@ConditionalOnClass(RespServer.class)
@EnableConfigurationProperties(RespMvcProperties.class)
public class RespMvcAutoConfiguration {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public RespServer server(RespMvcProperties properties, CommandSuite commands) {
    return new RespServer(properties.getHost(), properties.getPort(), commands);
  }

  @Bean
  @ConditionalOnMissingBean
  public CommandSuite commandSuite(CommandWrapperFactory factory) {
    return new CommandSuite(factory);
  }

  @Bean
  @ConditionalOnMissingBean
  public CommandWrapperFactory commandWrapperFactory(AutowireCapableBeanFactory factory) {
    return new SpringCommandWrapperFactory(factory);
  }
}
