package com.example.grademanagementsystem.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 集中管理Controller抛出的异常，Controller里只需要抛出就行，简化Controller逻辑
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class) // 专门处理RuntimeException
    public Result<Void> handleRuntimeException(RuntimeException e) { // 因为错误所以data为空
        // 当UserServiceImpl中抛出 new RuntimeException("密码错误") 时，会进入这里
        return Result.error(e.getMessage()); // 创建Result包装类返回给前端错误信息
    }
}

