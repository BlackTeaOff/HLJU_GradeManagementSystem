package com.example.grademanagementsystem.common;

import lombok.Data;

@Data
public class Result<T> { // 返回结果和信息的包装类
    private Integer code; // 状态码: 200表示成功，400表示失败等，前端识别错误码分辨出错误信息
    private String message; // 提示信息
    private T data; // 返回的具体数据，比如 Token 或者 User ID

    public static <T> Result<T> success(T data) { // 泛型方法
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() { // 静态方法, 直接类名调用就可以创建一个Result
        return success(null);
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(400); // 默认错误码
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}

