// 负责与后端通信
// 给请求加上Headers(Token), Method等等

// 所有api请求的前缀
const API_BASE_URL = "/api/v1"

// 从浏览器localStorage获取Token, 被其他js调用, 检查有没有Token, 没有就返回主页面
function getAuthToken() {
    // getItem输入键, 返回值
    // 单引号双引号都可以表示字符串
    return localStorage.getItem("token");
}

// 把Token存入浏览器, 方便后续取用
function setAuthToken(token) {
    localStorage.setItem("token", token); // token就是字符串
}

// 清除浏览器里的Token和user, 退出登录的时候用
function clearAuth() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");

    window.location.href = "index.html";
}

// async异步函数, 里面可以用await等待某个网络或其他请求的完成而暂停这个函数, 不会影响其他函数的运行
// fetch返回的是Promise对象, await后返回HTTP Response对象, 里面的body才是后端返回的对象
// options里面具体放headers和method(可有可无, 看具体函数需不需要, headers里可以放Token, 可以根据method(Put/Post)确定使用什么函数)
async function fetchWithAuth(url, options = {}) { // = {} 是默认参数, 如果没传值就是空
    console.log(`fetchWithAuth: ${url}`);

    // 不写const等修饰符就是隐式全局变量, 必须写修饰符
    const token = getAuthToken();

    // fetch: method, headers, body

    // options是对象, 可以后面加点接任何属性, 没有这个属性就返回undefined, 可用|| {}避免报错, 默认空对象
    // JS对象一般都是键值对
    // 组装请求头
    const headers = options.headers || {};

    // 让后端知道是json格式
    // 如果调用传入了headers, Content-Type就不改了
    if (!headers["Content-Type"]) {
        headers["Content-Type"] = "application/json";
    }

    // 有token就把它放到请求头里
    if (token) {
        headers["Authorization"] = token;
    }
    // 组装新的Options(带有组装好的请求头(有token和Content-Type))
    const newOptions = {
        ...options, // 把原来options里的内容展开放到这里
        headers: headers
    }

    // API前缀加上具体请求的路由, 请求包体传给后端
    const response = await fetch(`${API_BASE_URL}${url}`, newOptions);

    // 401: 无Token或格式错误, 403: 无权限(在LoginInterpreter里定义的)
    if (response.status === 401 || response.status === 403) {
        alert("登录已过期, 请重新登录");
        clearAuth(); // 清除本地token和user重新登录
        // Error相当于Exception, 返回Promise
        // 调用者需要try catch
        return Promise.reject(new Error("UnAuthorized"));
    }

    // 成功就给调用者返回后端的信息
    // async函数自动把结果包装成Promise对象
    return response;
}