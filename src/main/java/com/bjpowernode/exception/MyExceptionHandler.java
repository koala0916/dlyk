package com.bjpowernode.exception;

import com.bjpowernode.result.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 统一异常管理器
 */
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        e.printStackTrace();
        return Result.FAIL();
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handlerException(DataIntegrityViolationException e) {
        e.printStackTrace();
        return Result.FAIL("外键关联,删除失败");
    }
}
