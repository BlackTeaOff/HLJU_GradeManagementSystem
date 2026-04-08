document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault(); // 防止表单默认提交

    const id = document.getElementById('id').value; // 从index.html里拿到用户输入的id
    const password = document.getElementById('password').value; // 拿到密码
    const errorMsgDiv = document.getElementById('errorMessage'); // 拿到index里面显示error的部分

    // 把前端的errorMessage元素的class加上d-none, d-none是Bootstrap里隐藏元素的class
    errorMsgDiv.classList.add('d-none');

    // 向后端发送登录请求
    try {
        const res = await fetch(`${API_BASE_URL}/users/login`, { // 请求的一系列信息
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: Number(id), password: password }) // 对应UserLoginDTO里面的数据
        });

        const data = await res.json(); // 等到接收到后端发送的数据再往下继续
        if (data.code === 200 && data.data) { // 返回的是Result类, json格式, 里面有code和data
            // 返回的data里就有后端返回的token
            const tokenStr = data.data;

            // 保存token到浏览器localStorage
            setAuthToken(tokenStr);

            // 把token拆开(token格式是token-role-id)
            const parts = tokenStr.split('-');
            // length >= 2的时候, role就取parts[1], 也就是role, length < 2 role就取student
            const role = parts.length >= 2 ? parts[1] : 'student';

            // 登录后要显示名字, 在users接口获取
            const infoRes = await fetchWithAuth('/users/my/info');
            const infoData = await infoRes.json(); // .json之后才是真正的Result, 之前带有自带的请求头, Result和自带的请求头里面状态码相互独立

            if (infoData.code === 200) { // 用的是Result里面的code
                localStorage.setItem('role', role); // role存到浏览器
                localStorage.setItem('user', JSON.stringify(infoData.data)); // Result里面的data变成字符串放入localStorage(里面只能放字符串)

                // 根据不同的角色跳转到不同的页面
                if (role.toUpperCase() === 'ADMIN') {
                    window.location.href = '/admin.html';
                } else if (role.toUpperCase() === 'TEACHER') {
                    window.location.href = '/teacher.html';
                } else {
                    window.location.href = '/student.html';
                }
            } else { // 进入这里代表Result返回的不是200, 后端返回的错误
                throw new Error(infoData.message || 'Failed to fetch user info');
            }
        } else { // 对应上面login的状态码判断
            throw new Error(data.message || 'Login failed');
        }
    } catch (err) {
        errorMsgDiv.textContent = err.message;
        errorMsgDiv.classList.remove('d-none');
    }
});
