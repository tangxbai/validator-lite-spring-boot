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
package com.viiyue.validator.spring.boot.samples.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viiyue.plugins.validator.constraints.Rules;
import com.viiyue.validator.spring.boot.samples.bean.User;

@RestController
@RequestMapping
public class Controller {

	@Validated
	@GetMapping( "/test" )
	public void test( 
		@Rules( "required; not-blank; username; test(12345, 333)<<测试1>>; test2(12345, 333)<<测试2>>" ) String username,
		@Rules( "required; not-blank; password('strong')" ) String password ) {
		System.out.println( "Controller.test -> " + username );
	}

	@GetMapping( "/test-bean" )
	public void testBean( @Validated User user ) {
		System.out.println( "Controller.testBean -> " + user.getUsername() + ", " + user.getPassword() );
	}

}
