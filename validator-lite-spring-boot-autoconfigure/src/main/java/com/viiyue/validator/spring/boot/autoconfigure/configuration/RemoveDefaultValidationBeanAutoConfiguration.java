/**
 * Copyright (C) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.validator.spring.boot.autoconfigure.configuration;

import java.util.stream.Stream;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.DispatcherServlet;

import com.viiyue.plugins.validator.spring.ValidatorLite;

/**
 * After {@code 1.1.0}, the method interceptor is used for parameter verification, so it
 * is necessary to remove some of the Beans that come with the framework for
 * verification to prevent logical conflicts.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
@Configuration
@ConditionalOnWebApplication( type = Type.SERVLET )
@ConditionalOnClass( { Servlet.class, DispatcherServlet.class, ValidatorLite.class } )
public class RemoveDefaultValidationBeanAutoConfiguration implements BeanDefinitionRegistryPostProcessor {

	private final Stream<String> stream;
	
	public RemoveDefaultValidationBeanAutoConfiguration() {
		Class<?> pocessor = MethodValidationPostProcessor.class;
		Class<?> validatorBean = LocalValidatorFactoryBean.class;
		this.stream = Stream.of( pocessor.getName(), validatorBean.getName(),
				StringUtils.uncapitalize( pocessor.getSimpleName() ),
				StringUtils.uncapitalize( validatorBean.getSimpleName() ) );
	}
	
	@Override
	public void postProcessBeanFactory( ConfigurableListableBeanFactory beanFactory ) throws BeansException {
		// Ignore
	}

	@Override
	public void postProcessBeanDefinitionRegistry( BeanDefinitionRegistry registry ) throws BeansException {
		stream.forEach( name -> {
			if ( registry.containsBeanDefinition( name ) ) {
				registry.removeBeanDefinition( name );
			}
		});
	}

}
