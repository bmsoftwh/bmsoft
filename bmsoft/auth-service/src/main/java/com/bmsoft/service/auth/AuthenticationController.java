package com.bmsoft.service.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bmsoft.common.base.R;
import com.bmsoft.common.exception.code.ExceptionCode;
import com.bmsoft.common.jwt.JwtToken;
import com.bmsoft.common.jwt.UserAuth;
import com.bmsoft.common.redis.RedisUtil;
import com.bmsoft.service.auth.dto.LoginParamDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value="/auth")
@Api( tags={"用户验证服务"})
public class AuthenticationController {
	
	@Autowired
	private AuthTest auth;
	
	@Autowired
	private JwtToken jwt;
	
	@Autowired
	private RedisUtil redis;
	
	private final String REFRESH_TOKEN_KEY = "refresh_token";
	
	@ApiOperation(value="用户登录", notes="用户名密码登录验证")
	@ApiResponses(value={
			@ApiResponse(code=0, message="请求成功", response=R.class),
			@ApiResponse(code=40006, message="用户名或密码错误", response=R.class)
		})
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body", name = "userName", value = "用户名", required = true, dataType = "String"),
		@ApiImplicitParam(paramType = "body",name = "password", value = "密码", required = true, dataType = "String")
	})
	@RequestMapping(value = "/login1", method = RequestMethod.POST)
	public R<Map<String,String>> login( @RequestParam("userName") String userName, @RequestParam("password") String password) {
		UserAuth user = auth.vailduser(userName, password);
		if (null == user)
			return R.fail(ExceptionCode.JWT_USER_INVALID);
		
		return R.success(jwt.generateUserToken(user));
	}
	
	@ApiOperation(value="用户登录", notes="使用登录参数登录验证")
	@ApiResponses(value={
			@ApiResponse(code=0, message="请求成功", response=R.class),
			@ApiResponse(code=40006, message="用户名或密码错误", response=R.class)
		})
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body",name = "param", value = "登录请求参数", required = true, dataType = "LoginParamDTO")
	})
	@RequestMapping(value = "/login2", method = RequestMethod.POST)
	public R<Map<String, String>> login(@RequestBody LoginParamDTO param) {
		UserAuth user = auth.vailduser(param);
		if (null == user)
			return R.fail(ExceptionCode.JWT_USER_INVALID);
		Map<String, String> data = jwt.generateUserToken(user);
		redis.hset(REFRESH_TOKEN_KEY, user.getClient() + ":" + user.getUserId(), data.get(JwtToken.GRANT_REFRESH));
		return R.success(data);
	}
	
	@ApiOperation(value="刷新token", notes="使用refreshToken获取新的accessToken")
	@ApiResponses(value={
			@ApiResponse(code=0, message="请求成功", response=R.class),
			@ApiResponse(code=40001, message="freshToke已过期", response=R.class),
			@ApiResponse(code=40008, message="freshToke已失效", response=R.class)
		})
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "body",name = "refreshToken", value = "refreshToken", required = true, dataType = "String")
	})
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public R<String> refreshToken(@RequestParam("refreshToken") String refreshToken) {
		UserAuth user = jwt.getUserFromToken(refreshToken, JwtToken.GRANT_REFRESH);
		if (user == null) {
			return R.fail(ExceptionCode.JWT_TOKEN_EXPIRED);
		}
		//检查redis中的refreshtoken,存在切相等，则给与刷新
		String rrs = (String)redis.hget(REFRESH_TOKEN_KEY, user.getClient() + ":" + user.getUserId());
		//不存在则为无效，需要登录
		if (null == rrs || !rrs.equals(refreshToken))
			return R.fail(ExceptionCode.JWT_TOKEN_INVAILD);
		return R.success(jwt.refreshToken(refreshToken));
	}

}
