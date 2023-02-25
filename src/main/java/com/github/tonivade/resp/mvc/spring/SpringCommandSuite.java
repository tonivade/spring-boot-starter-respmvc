/*
 * Copyright (c) 2015-2023, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.resp.mvc.spring;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.BeanCreationException;
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
      Class<?> loadClass = loadClass(beanDefinition);
      Constructor<?> declaredConstructor = loadClass.getDeclaredConstructor();
      addCommand(() -> {
        try {
          return declaredConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException ex) {
          throw new BeanCreationException(beanDefinition.getResourceDescription(), null,
                                      "command cannot be instantiated " + beanDefinition.getBeanClassName(), ex);
        }
      });
    } catch (NoSuchMethodException ex) {
      throw new BeanCreationException(beanDefinition.getResourceDescription(), null,
                                      "not empty constructor found for class " + beanDefinition.getBeanClassName(), ex);
    } catch (ClassNotFoundException ex) {
      throw new CannotLoadBeanClassException(beanDefinition.getResourceDescription(), null,
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
