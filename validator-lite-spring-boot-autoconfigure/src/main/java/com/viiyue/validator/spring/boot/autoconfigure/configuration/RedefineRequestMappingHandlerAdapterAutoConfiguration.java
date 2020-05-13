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
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.viiyue.plugins.validator.spring.ValidatorLite;
import com.viiyue.validator.spring.boot.autoconfigure.support.MethodValidationPostProcessor;
import com.viiyue.validator.spring.boot.autoconfigure.support.VariantRequestMappingHandlerAdapter;

/**
 * After {@code 1.1.0}, the method interceptor is used for parameter
 * verification, so it is necessary to remove some of the Beans that come with
 * the framework for verification to prevent logical conflicts.
 * 
 * @author tangxbai
 * @since 1.2.0
 */
@Configuration
@ConditionalOnWebApplication( type = Type.SERVLET )
@AutoConfigureAfter({ EnableWebMvcConfiguration.class })
@ConditionalOnClass( { Servlet.class, DispatcherServlet.class, ValidatorLite.class } )
public class RedefineRequestMappingHandlerAdapterAutoConfiguration implements ApplicationContextAware {

	private final Stream<String> stream;
	
	public RedefineRequestMappingHandlerAdapterAutoConfiguration() {
		Class<?> pocessor = MethodValidationPostProcessor.class;
		Class<?> validatorBean = LocalValidatorFactoryBean.class;
		Class<?> adapter = RequestMappingHandlerAdapter.class;
		this.stream = Stream.of( 
			adapter.getName(), 
			StringUtils.uncapitalize( adapter.getSimpleName() ),
			pocessor.getName(), 
			StringUtils.uncapitalize( pocessor.getSimpleName() ),
			validatorBean.getName(),
			StringUtils.uncapitalize( validatorBean.getSimpleName() ) 
		);
	}
	
	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		DefaultListableBeanFactory beanFactory = ( DefaultListableBeanFactory ) applicationContext.getAutowireCapableBeanFactory();
		RequestMappingHandlerAdapter handlerAdapter = beanFactory.getBean( RequestMappingHandlerAdapter.class );
		stream.forEach( name -> {
			if ( beanFactory.containsBeanDefinition( name ) ) {
				beanFactory.removeBeanDefinition( name );
			}
		});
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.rootBeanDefinition( VariantRequestMappingHandlerAdapter.class );
		definition.addPropertyValue( "handlerAdapter", handlerAdapter );
		beanFactory.registerBeanDefinition( "requestMappingHandlerAdapter", definition.getBeanDefinition() );
	}

}
