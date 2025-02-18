package com.grant.MiShe.web.admin.custom.converter;

import com.grant.MiShe.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String code) {
                T[] enumConstants = targetType.getEnumConstants();
                for(T en:enumConstants){
                    if(en.getCode().equals(Integer.valueOf(code))){
                        return en;
                    }
                }

                throw new IllegalArgumentException("code:"+code+"非法");
            }
        };
    }
}
