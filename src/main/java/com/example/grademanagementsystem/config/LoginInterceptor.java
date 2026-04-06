package com.example.grademanagementsystem.config;

import com.example.grademanagementsystem.common.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component // 交给Spring处理, 可以在WebMvcConfig里注入(Autowired)
public class LoginInterceptor implements HandlerInterceptor { // Spring的拦截器接口

    // WebMvcConfig里被拦截的请求会进入这里
    // 前端的请求头(Header)里含有Token, 名字一般叫Authorization
    @Override // request是从HTTP请求得到的, response是没有Token或者不对的情况下返回给前端的, handler是要去往的Controller
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求放行前：从 Header 里获取 Token
        String token = request.getHeader("Authorization");

        // 校验 Token(格式是：token-role-id，例如 token-admin-1, token-student-4)
        if (token != null && token.startsWith("token-")) {
            try {
                String[] parts = token.split("-"); // 把token字符串拆分
                if (parts.length == 3) {
                    String role = parts[1]; // admin, teacher, student
                    int userId = Integer.parseInt(parts[2]);

                    // 看请求的uri
                    String uri = request.getRequestURI();

                    // 1. 如果是访问 Admin 接口，但角色不是 Admin，直接拦截
                    if (uri.startsWith("/api/v1/admins") && !"admin".equals(role)) {
                        return reject(response, 403, "权限不足：仅管理员可操作");
                    }

                    // 2. 如果是对课程进行增删改操作，仅限管理员
                    if (uri.startsWith("/api/v1/courses/create") || uri.startsWith("/api/v1/courses/modify") || uri.startsWith("/api/v1/courses/delete")) {
                        if (!"admin".equals(role)) {
                            return reject(response, 403, "权限不足：仅管理员可进行课程管理");
                        }
                    }

                    // 3. 老师接口，只有老师和管理员能看
                    if (uri.startsWith("/api/v1/teachers") && !("teacher".equals(role) || "admin".equals(role))) {
                        return reject(response, 403, "权限不足：仅教师可操作");
                    }

                    // 4. 学生接口，只有学生自己能访问
                    if (uri.startsWith("/api/v1/students") && !"student".equals(role)) {
                        return reject(response, 403, "权限不足：仅学生可操作");
                    }

                    // 5. 对用户进行增删操作，仅限管理员
                    if (uri.startsWith("/api/v1/users/create") || uri.startsWith("/api/v1/users/delete")) {
                        if (!"admin".equals(role)) {
                            return reject(response, 403, "权限不足：仅管理员可进行用户管理");
                        }
                    }

                    // 权限校验通过，将提取到的 ID 存入当前线程的 UserContext
                    UserContext.setUserId(userId);
                    return true; // 让请求继续走到 Controller
                }
            } catch (NumberFormatException e) {
                // 格式不对，往下走拦截
            }
        }

        // 发现没有Token或者Token不对，直接拦截：
        return reject(response, 401, "未登录或无效的Token");
    }

    private boolean reject(HttpServletResponse response, int code, String message) throws Exception {
        response.setStatus(code);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\": " + code + ", \"message\": \"" + message + "\", \"data\": null}");
        return false;
    }

    // 请求结束时, 一定会执行下面的代码, 清空这个线程的UserContext, 防止其它用户分配到这个线程, 里面的id没清空, 还能用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 整个请求结束返回前端后, 必须清除数据
        UserContext.remove();
    }
}
