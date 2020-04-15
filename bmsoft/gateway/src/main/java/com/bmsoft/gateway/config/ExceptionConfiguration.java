package com.bmsoft.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bmsoft.common.handler.DefaultGlobalExceptionHandler;

/**
 * 全局异常处理
 *
 */
@Configuration
@ControllerAdvice
@ResponseBody
public class ExceptionConfiguration extends DefaultGlobalExceptionHandler {
}
