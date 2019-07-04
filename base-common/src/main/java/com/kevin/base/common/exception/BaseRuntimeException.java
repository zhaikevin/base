package com.kevin.base.common.exception;

/**
 * @Description: 基础运行时异常类，无需显性捕获处理
 * @Author: kevin
 * @Date: 2019/7/4 14:32
 */
public class BaseRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5245028542591963131L;

    public BaseRuntimeException() {
    }

    public BaseRuntimeException(String errMsg) {
        super(errMsg);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaseRuntimeException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
}
