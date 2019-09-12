package com.kevin.base.common.exception;

/**
 * @Description: zookeeper操作异常
 * @Author: kevin
 * @Date: 2019/7/4 14:43
 */
public class RemoteCallException extends BaseRuntimeException {

    private static final long serialVersionUID = -1759054694656192925L;

    public RemoteCallException() {
        super("remote call exception.");
    }

    public RemoteCallException(String errMsg) {
        super(errMsg);
    }

    public RemoteCallException(Throwable cause) {
        super(cause);
    }

    public RemoteCallException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
}
