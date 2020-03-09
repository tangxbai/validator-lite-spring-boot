package com.viiyue.validator.spring.boot.samples.bean;

import com.viiyue.plugins.validator.annotation.Label;
import com.viiyue.plugins.validator.constraints.Rules;

public class Role {

	@Label( "{common.form.roleName}" )
	@Rules( "required; not-blank; email" )
	private String roleName;

	private String remark;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName( String roleName ) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark( String remark ) {
		this.remark = remark;
	}

}
