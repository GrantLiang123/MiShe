package com.grant.MiShe.common.exception;

import com.grant.MiShe.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handler(Exception e){
        e.printStackTrace();;
        return Result.fail();
    }

    @ExceptionHandler(LeaseException.class)
    @ResponseBody
    public Result handler(LeaseException e){
        e.printStackTrace();;
        return Result.fail(e.getCode(),e.getMessage());
    }
}
