package com.grant.MiShe.common.exception;

import com.grant.MiShe.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class LeaseException extends RuntimeException{
    private Integer code;

    public LeaseException(Integer code,String mes){
        super(mes);
        this.code=code;
    }

    public LeaseException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code= resultCodeEnum.getCode();
    }
}
