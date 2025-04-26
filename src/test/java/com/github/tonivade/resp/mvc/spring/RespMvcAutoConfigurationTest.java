/*
 * Copyright (c) 2015-2025, Antonio Gabriel Muñoz Conejo <me at tonivade dot es>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.resp.mvc.spring;

import static com.github.tonivade.resp.protocol.RedisToken.string;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.tonivade.resp.RespServerContext;
import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.command.CommandSuite;
import com.github.tonivade.resp.command.CommandWrapperFactory;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.command.RespCommand;
import com.github.tonivade.resp.protocol.RedisToken;

public class RespMvcAutoConfigurationTest {

  private static final String TEST = "test";

  @Test
  public void empty() {
    try (AnnotationConfigApplicationContext context = loadConfiguration(EmptyConfiguration.class)) {
      assertThat(context.getBeanNamesForType(CommandSuite.class).length, equalTo(1));
      assertThat(context.getBean(CommandSuite.class).contains(TEST), is(true));
    }
  }

  @Test
  public void overridesPort() {
    try (AnnotationConfigApplicationContext context = loadConfiguration(EmptyConfiguration.class, "resp.port:8081")) {
      assertThat(context.getBean(RespServerContext.class).getPort(), equalTo(8081));
    }
  }

  @Test
  public void nonEmpty() {
    try(AnnotationConfigApplicationContext context = loadConfiguration(NonEmptyConfiguration.class)) {
      assertThat(context.getBeanNamesForType(CommandSuite.class).length, equalTo(1));
      assertThat(context.getBean(CommandSuite.class).contains(TEST), is(true));
    }
  }

  private AnnotationConfigApplicationContext loadConfiguration(Class<?> config, String ... environment) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    TestPropertyValues.of(environment).applyTo(context);
    context.register(config, RespMvcAutoConfiguration.class);
    context.refresh();
    return context;
  }
}

@Configuration
@EnableAutoConfiguration
class EmptyConfiguration {
}

@Configuration
@EnableAutoConfiguration
class NonEmptyConfiguration {
  @Bean
  public CommandSuite commandSuite(CommandWrapperFactory factory) {
    return new CommandSuite(factory) {{
      addCommand("test", request -> string("test"));
    }};
  }
}

@Command("test")
class TestCommand implements RespCommand {
  @Override
  public RedisToken execute(Request request) {
    return string("test");
  }
}
