package com.bmsoft.common.exception;

import com.bmsoft.common.exception.code.BaseExceptionCode;

/**
 * 非业务异常
 * 用于在处理非业务逻辑时，进行抛出的异常。
 */
public class CommonException extends BaseCheckedException {

	public CommonException(BaseExceptionCode ec) {
		super(ec.getCode(), ec.getMsg());
	}
    
    public CommonException(int code, String message) {
        super(code, message);
    }

    public CommonException(int code, String format, Object... args) {
        super(code, String.format(format, args));
        this.code = code;
        this.message = String.format(format, args);
    }

    public CommonException wrap(int code, String format, Object... args) {
        return new CommonException(code, format, args);
    }

    @Override
    public String toString() {
        return "BizException [message=" + message + ", code=" + code + "]";
    }
}
