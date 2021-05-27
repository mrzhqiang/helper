package com.github.mrzhqiang.helper.io;

/**
 * 资源管理器异常。
 *
 * @author mrzhqiang
 */
public final class ExplorerException extends RuntimeException {

    private static final long serialVersionUID = 4510908274351398195L;

    public ExplorerException(String message) {
        super(message);
    }

    public ExplorerException(String message, Throwable cause) {
        super(message, cause);
    }
}
