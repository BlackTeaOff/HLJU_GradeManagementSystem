// student.html用到的js
document.addEventListener("DOMContentLoaded", async () => {
    console.log("学生页面加载完成");

    if (!getAuthToken() || localStorage.getItem("role") !== "student") {
        window.location.href = "/index.html";
        clearAuth();
    }

    // 从浏览器localStorage里取出user字符串, 解析成js对象
    const user = JSON.parse(localStorage.getItem("user"));

    // 把用户名字添加到html页面上
    document.getElementById("userNameDisplay").textContent = `欢迎! ${user.name}`;

    await loadMyCoursesAndGrades();
    await loadAllCoursesList();
    await loadProfile();
})

// html页面里注销的按钮有onclick, 点击就会执行这个函数
// 清除浏览器本地存储, 跳回登录页
function logout() {
    clearAuth();
}

// 加载首页默认的"我的课程 & 成绩"标签
async function loadMyCoursesAndGrades() {
    /*
    // Promise.all 同时执行多个async函数, 加快速度, 返回Promise对象, await后变成js对象
    // 按顺序返回给前面的两个对象
    const [courseResponse, gradeResponse] = await Promise.all([
        fetchWithAuth("/students/my/courses"),
        fetchWithAuth("/students/my/grades")
    ])

    // 取出里面的body
    // courseResult是Result类, 里面的data是一个List<StudentCourseResponseDTO>
    const courseResult = await courseResponse.json();
    const gradeResult = await gradeResponse.json();

    console.log(courseResult);
    console.log(gradeResult);
    */
    try {
        const gradeResponse = await fetchWithAuth("/students/my/grades");
        // gradeResult是Result类, 里面的data存的是List<StudentGradeResponseDTO>, .json返回Response的body部分
        const gradeResult = await gradeResponse.json();

        console.log(gradeResult);

        // 在html里找到表格元素, 准备向里填充信息(Table body)
        const tbody = document.getElementById("myCoursesTableBody");
        // 先清空表格
        tbody.innerHTML = "";

        // 后端返回正常状态码
        if (gradeResult.code === 200) {
            // 遍历每一个成绩信息, 依次填入html
            gradeResult.data.forEach(item => {
                // 创建一行(Table Row)
                const tr = document.createElement("tr");
                // 成绩不为空就设为成绩的值, 为空就写暂无成绩
                let myGrade = item.grade !== null ? item.grade : "暂无成绩";

                // 向行内添加列数据(Table data)
                tr.innerHTML = `
                    <td>${item.courseId}</td>
                    <td>${item.courseName}</td>  
                    <td>${myGrade}</td>
                    <td>
                        <button class="btn btn-sm btn-danger" onclick="dropCourse(${item.courseId})">退课</button>
                    </td>
                `;

                tbody.appendChild(tr); // 把空行加到表格
            })
            // 没有成绩(课程学生关联)的情况
            if (gradeResult.data.length === 0) {
                // colspan是占的列数
                tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">暂无已选课程</td></tr>';
            }
        }
    } catch(e) {
        // fetchWithAuth里面会抛异常
        console.error("加载课程失败", e);
    }
}

// 加载选课大厅里面的所有课程
async function loadAllCoursesList() {
    try {
        // course接口GET所有课程
        const courseResponse = await fetchWithAuth("/courses");
        // 取出请求体body部分, 就是后端返回的数据, Result类, data里是List<CourseInfoResponseDTO>
        const courseResult = await courseResponse.json();

        console.log(courseResult);

        // 找到html里面课程表格元素, 准备向里添加课程信息
        const tbody = document.getElementById("allCoursesTableBody");
        // 先清空表格
        tbody.innerHTML = "";

        // 后端Result里状态码正确
        if (courseResult.code === 200) {
            // 对于每一个课程信息
            courseResult.data.forEach(item => {
                // 创建一行, 用于存储信息(table row)
                const tr = document.createElement("tr");
                // 在这一行里创建列信息(table data)
                tr.innerHTML = `
                    <td>${item.courseId}</td>
                    <td>${item.courseName}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="selectCourse(${item.courseId})">选课</button> 
                    </td>
                `;
                // 把这一行放入表格中(table body)
                tbody.appendChild(tr);
            })
            if (courseResult.data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" class="text-center text-muted">暂无课程</td></tr>';
            }
        }
    } catch (e) {
        console.error("加载课程大厅失败", e);
    }
}

// 选课大厅每个课程的选课按钮都绑定这个函数, 附带自己的课程id(只需要传courseId, 自己id在UserContext)
async function selectCourse(courseId) {
    try { // fetchWithAuth里出错会抛异常
        // 选课, 是POST方法, 需要写options
        const response = await fetchWithAuth(`/students/courses/${courseId}/select`, {
            method: "POST"
        });

        // 选课返回的是Result<Void>, 只看状态码就知道成不成功
        const result = await response.json();
        console.log(result);
        if (result.code === 200) {
            alert("选课成功!");
            // 刷新我的课程页面
            await loadMyCoursesAndGrades();
        } else {
            // 显示后端的错误信息, 没有则返回选课失败
            console.log(result.message);
            alert(result.message || "选课失败");
        }
    } catch (e) {
        console.error(e);
    }
}

// 我的课程&成绩标签页里退课按钮绑定这个函数
async function dropCourse(courseId) {
    try {
        const response = await fetchWithAuth(`/students/courses/${courseId}/drop`, {
            method: "DELETE"
        })
        const result = await response.json();
        console.log(result);

        if (result.code === 200) {
            alert("退课成功!");
            await loadMyCoursesAndGrades();
            await loadAllCoursesList();
        } else {
            alert(result.message || "退课失败");
        }
    } catch (e) {
        console.error(e);
    }
}

async function loadProfile() {
    try {
        const response = await fetchWithAuth("/users/my/info");
        const result = await response.json();
        console.log(result);

        // 在student.html里找到放个人信息的容器
        const container = document.getElementById("infoContent");

        if (result.code === 200) {
            const data = result.data; // 取出Result里的data, 里面是StudentInfoResponseDTO
            // 把信息填入container(unordered list)
            container.innerHTML = `
            <ul class="list-group list-group-flush">
                <li class="list-group-item">学生ID: ${data.id}</li>
                <li class="list-group-item">姓名: ${data.name}</li>
                <li class="list-group-item">专业: ${data.major}</li>
                <li class="list-group-item">班级: ${data.group}</li>
                <li class="list-group-item">年级: ${data.year}</li>
            </ul>
            `;
        } else {
            container.innerHTML = '<p>加载失败</p>';
        }
    } catch (e) {
        console.error(e);
    }
}