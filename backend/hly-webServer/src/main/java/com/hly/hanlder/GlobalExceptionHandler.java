package com.hly.hanlder;

import com.hly.constant.ErrorCodeConstant;
import com.hly.constant.InfoConstant;
import com.hly.exception.BaseException;
import com.hly.exception.PasswordErrorException;
import com.hly.result.Result;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String msg =  message.split(" ")[2] + InfoConstant.ALREADY_EXISTS;
            return  Result.error(msg);
        }else{
            return Result.error(InfoConstant.UNKNOWN_ERROR);
        }
        
    }
    
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException e){
        String message = e.getMessage();
        if(message.contains("Duplicate entry")){
            String[] s = message.split(" ");
            String s1 = s[2];
            String msg = s1 + InfoConstant.ALREADY_EXISTS;
            
            System.out.println("当前线程：" + Thread.currentThread().getId());
            
            return Result.error(msg, ErrorCodeConstant.COUNT_EXIST);
        }else{
            return Result.error(InfoConstant.UNKNOWN_ERROR);
        }
    }
    
    @ExceptionHandler
    public Result exceptionHandler(PasswordErrorException ex){
        log.error("异常信息：{}", ex.getMessage());
        String message = ex.getMessage();
        return Result.error(InfoConstant.PASSWORD_ERROR);
    }
    
//    @ExceptionHandler
//    public Result allHandler(Exception e){
//        log.error("异常:{}", e.getMessage());
//        return Result.error(InfoConstant.UNKNOWN_ERROR);
//    }

    public Result notFoundHandler(NotFoundException e){
        log.error("异常:{}", e.getMessage());
        return Result.error(InfoConstant.NOT_FOUND_RESOURCE);
        
    }

}
