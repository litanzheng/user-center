package com.tencent.usercenter.exception;

import com.tencent.usercenter.common.BaseResponse;
import com.tencent.usercenter.common.ErrorCode;
import com.tencent.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BussinessException.class)
    public BaseResponse bussinessExceptionHandler(BussinessException exception){
        return ResultUtils.error(exception.getCode(),exception.getMessage(),"");
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException exception){
        log.error("runtimeException",exception);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,exception.getMessage(),"");
    }
}
