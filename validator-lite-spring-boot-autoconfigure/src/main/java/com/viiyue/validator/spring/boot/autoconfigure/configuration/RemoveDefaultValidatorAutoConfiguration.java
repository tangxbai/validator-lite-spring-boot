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

import javax.servlet.Servlet;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;

import com.viiyue.plugins.validator.spring.ValidatorLite;

/**
 * Since {@link javax.validation.Validator} is not introduced,
 * {@code Springboot} will automatically assemble a
 * {@link LocalValidatorFactoryBean} for the current environment. In this case,
 * multiple {@link org.springframework.validation.Validator} will appear, which
 * will cause the final {@code Validator} injection to fail. This configuration
 * is mainly used to remove the {@code Springboot} default assembly
 * {@code Validator}.
 * 
 * @author tangxbai
 * @since 1.2.1
 */
@Configuration
@ConditionalOnClass( { Servlet.class, DispatcherServlet.class, ValidatorLite.class } )
@AutoConfigureAfter( org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration.class )
public class RemoveDefaultValidatorAutoConfiguration implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanDefinitionRegistry( BeanDefinitionRegistry registry ) throws BeansException {
		String defaultValidator = "defaultValidator";
		if ( registry.containsBeanDefinition( defaultValidator ) ) {
			registry.removeBeanDefinition( defaultValidator );
		}
	}

	@Override
	public void postProcessBeanFactory( ConfigurableListableBeanFactory beanFactory ) throws BeansException {
		// Ignore
	}

}
