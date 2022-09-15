package com.tencent.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 5074733379064005353L;

    private int code;
    private T data;
    private String message;

    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }

    public BaseResponse(ErrorCode code) {
        this(code.getCode(),null,code.getMessage(),code.getDescription());
    }

    public BaseResponse(ErrorCode code, String message, String description) {
        this.code = code.getCode();
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
