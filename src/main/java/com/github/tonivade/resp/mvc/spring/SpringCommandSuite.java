package com.github.tonivade.resp.mvc.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.command.CommandSuite;

public class SpringCommandSuite extends CommandSuite {

  public SpringCommandSuite(ApplicationContext context) {
    super(new SpringCommandWrapperFactory(context.getAutowireCapableBeanFactory()));
    loadCommands(context);
  }

  private void loadCommands(ApplicationContext context) {
    for (String basePackage : AutoConfigurationPackages.get(context)) {
      for (BeanDefinition beanDefinition : scannerFor(context).findCandidateComponents(basePackage)) {
        loadCommand(beanDefinition);
      }
    }
  }

  private void loadCommand(BeanDefinition beanDefinition) {
    try {
      addCommand(loadClass(beanDefinition));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private Class<?> loadClass(BeanDefinition beanDefinition) throws ClassNotFoundException {
    Class<?> forName = Class.forName(beanDefinition.getBeanClassName());
    return forName;
  }

  private ClassPathScanningCandidateComponentProvider scannerFor(ApplicationContext context) {
    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false, context.getEnvironment());
    scanner.addIncludeFilter(new AnnotationTypeFilter(Command.class));
    return scanner;
  }

}
