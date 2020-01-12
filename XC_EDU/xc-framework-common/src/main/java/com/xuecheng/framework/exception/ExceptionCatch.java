package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//异常捕获类

@ControllerAdvice//控制器增强
public class ExceptionCatch {

    //出现异常后将日志记录下来
    private static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);
    //采用google提供的异常捕获 ImmutableMap,一旦创建不可更改
    // 定义map捕获可能的异常
    private static ImmutableMap<Class<? extends  Throwable>,ResultCode> EXCEPTIONS;
    protected static ImmutableMap.Builder<Class<? extends  Throwable>,ResultCode> builder=ImmutableMap.builder();

    //抛出自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException customException){
        //记录日志
        LOGGER.error("catch exception:{}",customException.getMessage());
        ResultCode resultCode=customException.getResultCode();
        return new ResponseResult(resultCode);
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception){
        //记录日志
        LOGGER.error("catch exception:{}",exception.getMessage());
        if(EXCEPTIONS==null){
        builder.build();//map为空时,构建map
        }

        //如果EXCEPTIONS中找到异常类型所对应的错误代码,就响应回去,否则返回99999
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if(resultCode!=null){
            return new ResponseResult(resultCode);
        }else{
            //返回99999
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }
    static{
        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }


}
