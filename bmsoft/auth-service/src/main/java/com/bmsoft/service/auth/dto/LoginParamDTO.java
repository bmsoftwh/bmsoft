package com.bmsoft.service.auth.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="登录请求参数")
public class LoginParamDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8119505026409734551L;
	
	@ApiModelProperty(value="clientId")
	private String client;
	
	@ApiModelProperty(value="登录名")
	private String loginName;
	
	@ApiModelProperty(value="验证码key")
	private String codeKey;
	
	@ApiModelProperty(value="验证码")
	private String validateCode;
	
	@ApiModelProperty(value="登录密码")
	private String password;
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
