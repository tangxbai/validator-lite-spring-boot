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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.StringUtils;

import com.viiyue.plugins.validator.ValidatorFactory;
import com.viiyue.plugins.validator.handler.Handler;
import com.viiyue.plugins.validator.scripting.configuration.ContextConfigurion;
import com.viiyue.plugins.validator.utils.BeanUtil;

/**
 * Javabean based on springboot automatic assembly property configuration
 *
 * @author tangxbai
 * @since 1.0.0
 */
@ConfigurationProperties( prefix = "validator.setting", ignoreInvalidFields = true )
public class ValidatorProperties extends ContextConfigurion {

	private static final ClassPathScanningCandidateComponentProvider scanner;
	
	static {
		scanner = new ClassPathScanningCandidateComponentProvider( false );
		scanner.addIncludeFilter( new AssignableTypeFilter( Handler.class ) );
	}
	
	private String handlers;
	private Class<? extends ValidatorFactory> factory;

	public Class<? extends ValidatorFactory> getFactory() {
		return factory;
	}

	public void setFactory( Class<? extends ValidatorFactory> factory ) {
		this.factory = factory;
	}
	
	public void setHandlers( String handlers ) {
		this.handlers = handlers;
	}

	public ValidatorFactory getFactoryInstance() {
		return factory == null ? null : BeanUtil.newInstance( factory );
	}
	
	public List<String> getHandlerClassNames() {
		if ( StringUtils.isEmpty( handlers ) ) {
			return Collections.emptyList();
		}
		List<String> handlerClassNames = new ArrayList<String>( 32 );
		String [] packages = StringUtils.tokenizeToStringArray( handlers, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS );
		for ( String pattern : packages ) {
			for ( BeanDefinition bean : scanner.findCandidateComponents( pattern )) {
				handlerClassNames.add( bean.getBeanClassName() );
			}
		}
		return handlerClassNames;
	}
	
}
