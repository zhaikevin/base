package com.kevin.base.common.exception;

/**
 * @Description: 序列化异常
 * @Author: kevin
 * @Date: 2019/7/4 14:43
 */
public class SerializeException extends BaseRuntimeException {

    private static final long serialVersionUID = -1759054694656192925L;

    public SerializeException() {
        super("serialize exception.");
    }

    public SerializeException(String errMsg) {
        super(errMsg);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }

    public SerializeException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
}
