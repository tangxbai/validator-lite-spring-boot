package com.viiyue.validator.spring.boot.samples;

import com.viiyue.plugins.validator.annotation.Label;
import com.viiyue.plugins.validator.annotation.Valid;
import com.viiyue.plugins.validator.annotation.When;
import com.viiyue.plugins.validator.constraints.Required;
import com.viiyue.plugins.validator.constraints.Rules;

public class User {

	@Label( "{common.form.username}" )
	@Rules( "required; not-blank; range(10,20); pattern(/\\d+/)" )
	private String username;

	@Label( "{common.form.password}" )
	@Rules( "required; not-blank; password('strong'); equals('#username')" )
	@When( fields = "username", is = When.Result.PASSED )
	private String password;
	
	@Valid
	@Required
	private Role role;

	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole( Role role ) {
		this.role = role;
	}

}
