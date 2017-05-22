package com.github.tonivade.resp.mvc.spring;

import static com.github.tonivade.resp.protocol.RedisToken.string;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.tonivade.resp.RespServer;
import com.github.tonivade.resp.command.CommandSuite;
import com.github.tonivade.resp.command.CommandWrapperFactory;

public class RespMvcAutoConfigurationTest {

  private static final String TEST = "test";

  @Test
  public void empty() {
    AnnotationConfigApplicationContext context = loadConfiguration(EmptyConfiguration.class);

    assertThat(context.getBeanNamesForType(CommandSuite.class).length, equalTo(1));
    assertThat(context.getBean(CommandSuite.class).contains(TEST), is(false));

    context.close();
  }

  @Test
  public void overridesPort() {
    AnnotationConfigApplicationContext context = loadConfiguration(EmptyConfiguration.class, "resp.port:8081");

    assertThat(context.getBean(RespServer.class).getPort(), equalTo(8081));

    context.close();
  }

  @Test
  public void nonEmpty() {
    AnnotationConfigApplicationContext context = loadConfiguration(NonEmptyConfiguration.class);

    assertThat(context.getBeanNamesForType(CommandSuite.class).length, equalTo(1));
    assertThat(context.getBean(CommandSuite.class).contains(TEST), is(true));

    context.close();
  }

  private AnnotationConfigApplicationContext loadConfiguration(Class<?> config, String ... environment) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    EnvironmentTestUtils.addEnvironment(context, environment);
    context.register(config, RespMvcAutoConfiguration.class);
    context.refresh();
    return context;
  }
}

@Configuration
class EmptyConfiguration {
}

@Configuration
class NonEmptyConfiguration {
  @Bean
  public CommandSuite commandSuite(CommandWrapperFactory factory) {
    return new CommandSuite(factory) {{
      addCommand("test", request -> string("test"));
    }};
  }
}
