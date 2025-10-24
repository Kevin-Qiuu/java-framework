package com.bitejiuyeke.bitecommondomain.exception;

import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应构造方法（推荐）
     *
     * @param resultCode ResultCode(code, msg)
     */
    public ServiceException(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 消息构造方法
     *
     * @param message 异常信息
     */
    public ServiceException(String message) {
        this.msg = message;
    }

    /**
     * 异常状态码构造方法
     *
     * @param code 异常状态码
     */
    public ServiceException(Integer code) {
        this.code = code;
    }


    public ServiceException(String message, int code) {
        this.msg = message;
        this.code = code;
    }
}
