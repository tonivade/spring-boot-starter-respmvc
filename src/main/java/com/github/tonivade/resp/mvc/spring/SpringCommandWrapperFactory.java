/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.resp.mvc.spring;

import static java.util.Objects.requireNonNull;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.github.tonivade.resp.command.DefaultCommandWrapperFactory;
import com.github.tonivade.resp.command.RespCommand;

public class SpringCommandWrapperFactory extends DefaultCommandWrapperFactory {

  private AutowireCapableBeanFactory factory;

  public SpringCommandWrapperFactory(AutowireCapableBeanFactory factory) {
    this.factory = requireNonNull(factory);
  }

  @Override
  public RespCommand wrap(Object command) {
    requireNonNull(command, "command cannot be null");
    factory.autowireBean(command);
    factory.initializeBean(command, command.getClass().getSimpleName());
    return super.wrap(command);
  }
}
