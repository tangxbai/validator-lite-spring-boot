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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;

import com.viiyue.plugins.validator.common.Constants;
import com.viiyue.plugins.validator.metadata.result.ElementResult;
import com.viiyue.plugins.validator.metadata.result.FragmentResult;
import com.viiyue.plugins.validator.metadata.result.ValidatedResult;
import com.viiyue.plugins.validator.spring.bindings.ParameterBindingResult;
import com.viiyue.plugins.validator.spring.exception.ValidatedException;
import com.viiyue.plugins.validator.spring.utils.LocaleUtils;

/**
 * After {@code 1.1.0}, use method interceptor to check parameters.
 * 
 * @author tangxbai
 * @since 1.1.0
 */
public class MethodValidationInterceptor implements MethodInterceptor {

	private final ParameterNameDiscoverer parameterNameDiscoverer;

	public MethodValidationInterceptor( ParameterNameDiscoverer parameterNameDiscoverer ) {
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}

	@Override
	public Object invoke( MethodInvocation invocation ) throws Throwable {
		Method method = invocation.getMethod();
		Parameter [] parameters = method.getParameters();

		// Avoid Validator invocation on FactoryBean.getObjectType/isSingleton
		if ( isFactoryBeanMetadataMethod( method ) || ArrayUtils.isEmpty( parameters ) ) {
			return invocation.proceed();
		}
		
		String methodName = method.getName();
		Object [] arguments = invocation.getArguments();
		String [] parameterNames = parameterNameDiscoverer.getParameterNames( method );
		Class<?> beanType = invocation.getThis().getClass();
		Class<?>[] groups = determineValidationGroups( method, beanType );

		// The validator supports internationalized message display,
		// but needs to get the current locale from the spring framework.
		Locale locale = LocaleUtils.switchLocale();

		// Perform single parameter validation
		int paramLength = parameters.length;
		ValidatedResult validatedResult = null;
		Map<String, Object> target = new HashMap<String, Object>( paramLength );
		ParameterBindingResult bindingResult = new ParameterBindingResult( target, methodName );
		for ( int i = 0; i < paramLength; i ++ ) {
			Parameter parameter = parameters[ i ];
			String parameterName = parameterNames[ i ];
			String defaultMessage = "{" + beanType.getName() + "." + method.getName() + "." + parameterName + "}";
			ValidatedResult result = com.viiyue.plugins.validator.Validator.validateParameter( 
					arguments[ i ], parameter, parameterName, defaultMessage, locale, groups );
			if ( validatedResult == null ) {
				validatedResult = result;
			} else {
				validatedResult.merge( result );
			}
			bindingResult.putParameter( parameterName, arguments[ i ], parameter.getType() );
			// Append each validation result to BindingResult
			if ( !result.isPassed() ) {
				ElementResult rejectedResult = validatedResult.getLastRejectedResult();
				for ( FragmentResult fr : ( List<FragmentResult> ) rejectedResult.getResult() ) { // Updated in v1.0.3
					String basicMessageCode = Constants.DEFAULT_MESSAGE_KEY_PREFIX + "." + fr.getFragment();
					String [] errorCodes = bindingResult.resolveMessageCodes( basicMessageCode, parameterName );
					bindingResult.addError( new FieldError( methodName, parameterName, arguments[ i ], false, errorCodes, fr.getArguments(), fr.getErrorMessage() ) );
				}
			}
		}
		bindingResult.setValidated( validatedResult );
		
		// Inject validation results or throw exceptions
		if ( ValidatedResult.class.isAssignableFrom( parameters[ paramLength - 1 ].getType() ) ) {
			arguments[ paramLength - 1 ] = validatedResult;
			if ( invocation instanceof ReflectiveMethodInvocation ) {
				( ( ReflectiveMethodInvocation ) invocation ).setArguments( arguments );
			}
		} else if ( validatedResult != null && !validatedResult.isPassed() ) {
			throw new ValidatedException( bindingResult );
		}
		
		// If the data is verified, specify the target method normally.
		return invocation.proceed();
	}
	
	protected Class<?> [] determineValidationGroups( Method method, Class<?> beanType ) {
		Validated validatedAnn = AnnotationUtils.findAnnotation( method, Validated.class );
		if ( validatedAnn == null ) {
			validatedAnn = AnnotationUtils.findAnnotation( beanType, Validated.class );
		}
		return ( validatedAnn != null ? validatedAnn.value() : null );
	}

	private boolean isFactoryBeanMetadataMethod( Method method ) {
		Class<?> clazz = method.getDeclaringClass();

		// Call from interface-based proxy handle, allowing for an efficient check?
		if ( clazz.isInterface() ) {
			return ( ( clazz == FactoryBean.class || clazz == SmartFactoryBean.class ) && !method.getName().equals( "getObject" ) );
		}

		// Call from CGLIB proxy handle, potentially implementing a FactoryBean method?
		Class<?> factoryBeanType = null;
		if ( SmartFactoryBean.class.isAssignableFrom( clazz ) ) {
			factoryBeanType = SmartFactoryBean.class;
		} else if ( FactoryBean.class.isAssignableFrom( clazz ) ) {
			factoryBeanType = FactoryBean.class;
		}
		return ( factoryBeanType != null && !method.getName().equals( "getObject" ) && 
				ClassUtils.hasMethod( factoryBeanType, method.getName(), method.getParameterTypes() ) );
	}

}
