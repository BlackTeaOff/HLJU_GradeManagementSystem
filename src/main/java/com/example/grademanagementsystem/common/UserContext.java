package com.example.grademanagementsystem.common;

public class UserContext {
    // 使用 ThreadLocal 存储当前线程（即当前 HTTP 请求）的用户 ID
    // 存放当前访问用户的ID, 一个用户一个线程, 相互独立
    // 不需要实例化, 通过UserContext.getUserId就可以得到用户的ID, 不需要在路由上传ID了
    // 一次请求一个Context
    // 请求包含Token, Token解析出UserId, 然后UserId就存在这里, 不需要层层传递ID
    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();

    public static void setUserId(Integer id) {
        USER_ID.set(id);
    }

    public static Integer getUserId() {
        return USER_ID.get();
    }

    // 必须remove, 否则分配新线程时可能USERID还没清除, 就用了别人的ID, 还有remove可以防止内存泄露
    public static void remove() {
        USER_ID.remove();
    }
}

