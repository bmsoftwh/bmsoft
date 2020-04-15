package com.bmsoft.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.bmsoft.common.jwt.UserAuth;
import com.bmsoft.service.auth.dto.LoginParamDTO;

@Component
@RefreshScope
public class AuthTest {
	
	@Value("${auth.username:tom}")
	private String name;
	
	@Value("${auth.password:cat}")
	private String pwd;
	
	@Value("${auth.userId:1001}")
	private String userId;
	
	@Value("${auth.tenant:1}")
	private String tenant;
	
	@Value("${auth.client:1}")
	private String client;
	
	public UserAuth vailduser(String username, String pass) {
		if (name.equals(username) && pwd.equals(pass)) {
			UserAuth user = new UserAuth();
			user.setUserName(username);
			user.setTenant(tenant);
			user.setClient(client);
			user.setUserId(userId);
			return user;
		}
		return null;
	}
	
	public UserAuth vailduser(LoginParamDTO param) {
		if (name.equals(param.getLoginName()) && pwd.equals(param.getPassword())) {
			UserAuth user = new UserAuth();
			user.setUserId(userId);
			user.setTenant(tenant);
			user.setClient(param.getClient());
			return user;
		}
		return null;
	}

}
