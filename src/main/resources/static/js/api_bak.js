// api.js: Base configuration and fetch wrapper that automatically attaches JWT tokens.
// api.js负责与后端的通信
// 把前端的请求加上Token
// 如果后端返回401或Token过期, api.js会清空本地缓存并强制跳转回登录页

// 所有发出的请求, 都会在前面加上api/v1, 如果后端统一api改了, 直接在这改
const API_BASE_URL = '/api/v1';

// 从浏览器里的localStorage拿Token
function getAuthToken() {
    return localStorage.getItem('token');
}

// 向浏览器的localStorage里存Token(键值对)
function setAuthToken(token) {
    localStorage.setItem('token', token);
}

// 注销时清理token, 返回登录页
function clearAuth() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    window.location.href = '/index.html';
}

// async异步函数, 网络请求需要时间, 不能让网页卡住, 可以同时执行其他函数
// url是请求的后端接口路径, options里存的是请求的高级设置, 如method:'POST'
async function fetchWithAuth(url, options = {}) {
    const headers = options.headers || {}; // 从参数中拿请求头, 如果没传就是空
    const token = getAuthToken(); // 从浏览器拿Token

    if (token) {
        headers['Authorization'] = `${token}`; // 把token放入请求头(Header是键值对)
    } // ``标记模板字符串的开始与结束, ${}是模板字符串

    // Default to JSON if not specified
    // 如果Headers里没有content-type, 并且传入的option不是提交表单的形式
    if (!headers['Content-Type'] && options.body instanceof URLSearchParams === false) {
        headers['Content-Type'] = 'application/json'; // 就把返回给后端的格式设置为json格式
    } // 因为@RequestBody只认json格式

    // 在这里组装并给后端发送请求
    // fetch是浏览器自带的网络请求工具, 会给后端API发送请求
    // await让代码得到response之后再往下走
    //把API_BASE_URL和具体的接口url拼接, 得到/api/v1/users等等请求路由地址
    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers
    }); // 传入options和headers, 里面有token和method等信息

    // 后端发来的response里有状态码, 401是无Token或错误Token, 403是没有权限(在LoginInterceptor里定义的)
    if (response.status === 401 || response.status === 403) {
        alert('认证失败或过时，请重新登录！');
        clearAuth();
        return Promise.reject(new Error('Unauthorized')); // 给调用者返回reject, 会在调用者那边抛出异常, 不会解析空数据报错
    }

    // 如果没有错误就把返回信息返回给调用者
    return response;
}

function showChangePasswordModal() {
    // 移除已有的Modal避免重复
    let oldModal = document.getElementById('globalPasswordModal');
    if (oldModal) {
        oldModal.remove();
        // Remove bootstrap modal backdrop
        let backdrop = document.querySelector('.modal-backdrop');
        if(backdrop) backdrop.remove();
    }

    let html = `
    <div class="modal fade" id="globalPasswordModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">修改密码</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
              <div class="mb-3">
                  <label>原密码</label>
                  <input type="password" class="form-control" id="globalOldPass">
              </div>
              <div class="mb-3">
                  <label>新密码</label>
                  <input type="password" class="form-control" id="globalNewPass">
              </div>
              <button class="btn btn-primary" onclick="submitChangePassword()">确认修改</button>
          </div>
        </div>
      </div>
    </div>
    `;

    document.body.insertAdjacentHTML('beforeend', html);
    const modal = new bootstrap.Modal(document.getElementById('globalPasswordModal'));
    modal.show();
}

window.submitChangePassword = async function() {
    const oldP = document.getElementById('globalOldPass').value;
    const newP = document.getElementById('globalNewPass').value;
    if(!oldP || !newP) return alert('原密码和新密码都必须填写！');

    try {
        const res = await fetchWithAuth('/users/modifyPassword', {
            method: 'POST',
            body: JSON.stringify({ old_password: oldP, new_password: newP })
        });
        const data = await res.json();

        if (data.code === 200) {
            alert('修改成功！请重新登录。');
            clearAuth(); // 强制重新登录
        } else {
            alert('密码修改失败: ' + data.message);
        }
    } catch(e) {
        console.error(e);
        alert('系统网络异常');
    }
}
