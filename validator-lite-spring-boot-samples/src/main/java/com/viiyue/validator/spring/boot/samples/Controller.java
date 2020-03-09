package com.viiyue.validator.spring.boot.samples;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viiyue.plugins.validator.constraints.Rules;

@RestController
@RequestMapping
public class Controller {

	@Validated
	@GetMapping( "/test" )
	public void test( 
		@Rules( "required; not-blank; username<<-------------->>" ) String username, 
		@Rules( "required; not-blank; password('strict')" ) String password ) {
		System.out.println( "Controller.test -> " + username );
	}

	@GetMapping( "/test-bean" )
	public void testBean( @Validated User user ) {
		System.out.println( "Controller.testBean -> " + user.getUsername() + ", " + user.getPassword() );
	}

}
