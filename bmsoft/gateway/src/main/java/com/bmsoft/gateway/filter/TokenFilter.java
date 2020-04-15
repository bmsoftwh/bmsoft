package com.bmsoft.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.bmsoft.common.base.R;
import com.bmsoft.common.exception.code.BaseExceptionCode;
import com.bmsoft.common.exception.code.ExceptionCode;
import com.bmsoft.common.jwt.JwtToken;
import com.bmsoft.gateway.util.IgnoreTokenConfig;

import io.jsonwebtoken.ExpiredJwtException;
import reactor.core.publisher.Mono;

@Component
public class TokenFilter implements GlobalFilter, Ordered {

	
	@Autowired
	private JwtToken jwt;
	
	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String url = exchange.getRequest().getURI().getPath();
		if (!IgnoreTokenConfig.isIgnoreToken(url)) {
			String token = exchange.getRequest().getHeaders().getFirst("access_token");
			if (token != null ) {
			    try {
					if (jwt.validateToken(token, null))
					    return chain.filter(exchange);
					else 
						return errorResponse(exchange.getResponse(), 
								ExceptionCode.JWT_TOKEN_EXPIRED,HttpStatus.OK.value());	
			    } catch (ExpiredJwtException e) {
			    	return errorResponse(exchange.getResponse(), 
							ExceptionCode.JWT_TOKEN_EXPIRED,HttpStatus.OK.value());	
			    } catch (Exception e) {
			    	return errorResponse(exchange.getResponse(), 
							ExceptionCode.JWT_PARSER_TOKEN_FAIL,HttpStatus.OK.value());	
			    }
			} else 
				return errorResponse(exchange.getResponse(), 
						ExceptionCode.JWT_ILLEGAL_ARGUMENT,HttpStatus.OK.value());			
		}
		
		return chain.filter(exchange);
	}
	
	protected Mono<Void> errorResponse(ServerHttpResponse response, BaseExceptionCode err, int httpStatusCode) {
        R tokenError = R.fail(err);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DataBuffer dataBuffer = response.bufferFactory().wrap(tokenError.toString().getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

}
