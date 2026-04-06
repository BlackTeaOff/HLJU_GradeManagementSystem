// api.js: Base configuration and fetch wrapper that automatically attaches JWT tokens.
const API_BASE_URL = '/api/v1';

function getAuthToken() {
    return localStorage.getItem('token');
}

function setAuthToken(token) {
    localStorage.setItem('token', token);
}

function clearAuth() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    window.location.href = '/index.html';
}

async function fetchWithAuth(url, options = {}) {
    const headers = options.headers || {};
    const token = getAuthToken();

    if (token) {
        headers['Authorization'] = `${token}`;
    }

    // Default to JSON if not specified
    if (!headers['Content-Type'] && options.body instanceof URLSearchParams === false) {
        headers['Content-Type'] = 'application/json';
    }

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers
    });

    if (response.status === 401 || response.status === 403) {
        alert('认证失败或过时，请重新登录！');
        clearAuth();
        return Promise.reject(new Error('Unauthorized'));
    }

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
