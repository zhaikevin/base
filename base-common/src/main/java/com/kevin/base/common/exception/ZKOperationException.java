package com.kevin.base.common.exception;

/**
 * @Description: zookeeper操作异常
 * @Author: kevin
 * @Date: 2019/7/4 14:43
 */
public class ZKOperationException extends BaseRuntimeException {

    private static final long serialVersionUID = 2986523839140033826L;

    public ZKOperationException() {
        super("Zookeeper operation exception.");
    }

    public ZKOperationException(String errMsg) {
        super(errMsg);
    }

    public ZKOperationException(Throwable cause) {
        super(cause);
    }
}
