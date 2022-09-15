package com.tencent.usercenter.common;

public class ResultUtils {

    public static <T> BaseResponse<T> success(T data){
       return new BaseResponse<>(0,data,"ok",null);
    }

    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse<>(errorCode,message,description);
    }

    public static BaseResponse error(int errorCode,String message,String description){
        return new BaseResponse<>(errorCode,message,description);
    }
}
