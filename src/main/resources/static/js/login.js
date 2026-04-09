// index.html用到的js
// DOMContentLoaded之后再执行下面的函数, 防止html没加载完, js找不到html里面的元素, => 箭头函数
document.addEventListener("DOMContentLoaded", () => {
    console.log("登录页面加载完成");

    // JS找到html里面的表单, 监听submit动作, submit就执行下面的函数
    document.getElementById("loginForm").addEventListener("submit", async (e) => {
        console.log("监听到submit");

        e.preventDefault(); // 阻止表单提交自动刷新, 交给js处理

        // 从html里取出用户输入的id, 密码
        const id = document.getElementById("id").value;
        const password = document.getElementById("password").value;
        // 在html里拿到显示错误信息的部分, 如果出错可以直接把错误信息塞到html页面
        const errorMsgDiv = document.getElementById("errorMessage");

        // 重置错误块为隐藏(之前密码错误, 后续重新登录的时候会先隐藏)
        errorMsgDiv.classList.add("d-none");

        console.log(`ID: ${id}, Password: ${password}`);

        // 下面开始登录
        try { // 这里面出现错误会抛出异常, catch里统一处理
            // 把ID, 密码组装成UserLoginDTO能接受的json格式
            // fetchWithAuth传入的第二个参数是一个js对象
            // login返回的是token, 被包装成HTTP包返回, 需要response.json取出后端返回的Result, 然后.data取出token
            const loginResponse = await fetchWithAuth("/users/login", {
                method: "POST",
                // stringify接收三个参数, 第一个参数是value, 要把所有要序列化的对象都放进大括号
                // body部分必须用字符串, 后端@RequestBody自动把字符串解析成对象
                body: JSON.stringify({id, password})
            })

            // 从后端返回的数据, await把Promise对象变成js对象
            const loginResult = await loginResponse.json();
            console.log(loginResult);

            // 判断是否登录成功, Result类success默认code是200, 登录失败会进入error, 默认code是400
            // login的api不会被拦截器拦截, 不用判断Response里的状态码
            if (loginResult.code === 200) {
                // 把token存到浏览器
                setAuthToken(loginResult.data);

                console.log(getAuthToken());
                // 从Token中提取Role(token-role-id)
                const parts = loginResult.data.split("-"); // 用-提取出三部分, 0-token, 1-role, 2-id
                const role = parts[1];
                console.log(role);

                // 从后端users/info获取用户信息
                // 不写method, 默认是GET. 访问这个API会被拦截, 需要带Token访问
                const infoResponse = await fetchWithAuth("/users/my/info");
                const infoResult = await infoResponse.json();
                console.log(infoResult);

                // fetch会被拦截器拦截, 可能拦截器不通过Response返回错误码
                if (infoResponse.status === 200 && infoResult.code === 200) {
                    // infoResult是Result类(JS里是Object), 里面有data属性, 也是JS对象, 需要变成字符串存到localStorage
                    localStorage.setItem("user", JSON.stringify(infoResult.data));
                    localStorage.setItem("role", role);

                    console.log(localStorage.getItem("user"));
                    console.log(localStorage.getItem("role"));

                    // 根据不同角色跳转不同页面
                    if (role === "admin") {
                        window.location.href = "/admin.html";
                    } else if (role === "teacher") {
                        window.location.href = "/teacher.html";
                    } else {
                        window.location.href = "/student.html";
                    }
                } else {
                    throw new Error(infoResult.message || "获取用户信息失败");
                }
            } else { // Result里有后端返回的错误信息, 如果有就用, 没有就用后面的替代
                throw new Error(loginResult.message || "登录失败");
            }
        } catch(e) {
            console.log(e);
            // 把错误信息放到html页面上去
            errorMsgDiv.textContent = e.message;
            // 原本html标签上有d-none隐藏了错误块, remove掉
            errorMsgDiv.classList.remove("d-none");
        }
    })
})