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
package com.viiyue.validator.spring.boot.autoconfigure.support;

import java.lang.annotation.Annotation;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.validation.annotation.Validated;

/**
 * After {@code 1.1.0}, the use of {@code AOP} dynamic proxy needs to implement
 * the target parameter verification method.
 * 
 * @see org.springframework.validation.beanvalidation.MethodValidationPostProcessor
 * @author tangxbai
 * @since 1.1.0
 */
public class MethodValidationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor
	implements InitializingBean {

	private static final long serialVersionUID = 1L;

	private ParameterNameDiscoverer parameterNameDiscoverer;
	private Class<? extends Annotation> validatedAnnotationType = Validated.class;

	@Override
	public void afterPropertiesSet() {
		Pointcut classPointcut = AnnotationMatchingPointcut.forClassAnnotation( validatedAnnotationType );
		Pointcut methodPointcut = AnnotationMatchingPointcut.forMethodAnnotation( validatedAnnotationType );
		ComposablePointcut pointcut = new ComposablePointcut( classPointcut ).union( methodPointcut );
		this.advisor = new DefaultPointcutAdvisor( pointcut, new MethodValidationInterceptor( parameterNameDiscoverer ) );
	}

	public void setParameterNameDiscoverer( ParameterNameDiscoverer parameterNameDiscoverer ) {
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}

}
