package com.github.tonivade.resp.mvc.spring;

import static com.github.tonivade.resp.protocol.RedisToken.string;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.tonivade.resp.command.CommandSuite;
import com.github.tonivade.resp.command.CommandWrapperFactory;

public class RespMvcAutoConfigurationTest {
  
  @Test
  public void empty() {
    AnnotationConfigApplicationContext context = loadConfiguration(EmptyConfiguration.class);

    assertThat(context.getBeanNamesForType(CommandSuite.class).length, equalTo(1));
    assertThat(context.getBean(CommandSuite.class).getCommand("test").getClass().getSimpleName(), equalTo("NullCommand"));

    context.close();
  }
  
  @Test
  public void nonEmpty() {
    AnnotationConfigApplicationContext context = loadConfiguration(NonEmptyConfiguration.class);

    assertThat(context.getBeanNamesForType(CommandSuite.class).length, equalTo(1));
    assertThat(context.getBean(CommandSuite.class).getCommand("test").getClass().getSimpleName(), not(equalTo("NullCommand")));
    
    context.close();
  }
  
  private AnnotationConfigApplicationContext loadConfiguration(Class<?> config) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(config);
    context.register(RespMvcAutoConfiguration.class);
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
