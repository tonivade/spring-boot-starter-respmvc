/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.resp.mvc.spring;

import org.springframework.beans.factory.CannotLoadBeanClassException;
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
    } catch (ClassNotFoundException ex) {
      throw new CannotLoadBeanClassException(beanDefinition.getResourceDescription(), /*bean name*/ null,
                                             beanDefinition.getBeanClassName(), ex);
    }
  }

  private Class<?> loadClass(BeanDefinition beanDefinition) throws ClassNotFoundException {
    return Class.forName(beanDefinition.getBeanClassName());
  }

  private ClassPathScanningCandidateComponentProvider scannerFor(ApplicationContext context) {
    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false, context.getEnvironment());
    scanner.addIncludeFilter(new AnnotationTypeFilter(Command.class));
    return scanner;
  }
}
