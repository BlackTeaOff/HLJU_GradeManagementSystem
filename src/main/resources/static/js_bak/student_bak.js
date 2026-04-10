// 页面加载完之后触发
document.addEventListener('DOMContentLoaded', async () => {
    // 没有Token就返回index登录页
    if (!getAuthToken()) {
        window.location.href = '/index.html';
        return;
    }

    // 从浏览器本地存储取出user数据(login.js里存的), 存到对象里
    const user = JSON.parse(localStorage.getItem('user'));
    // 把名字或id放到前端页面上
    if (user) {
        document.getElementById('userNameDisplay').textContent = `欢迎，${user.name || user.id}`;
    }

    // 获取课程与成绩, 加载课程列表
    await loadMyCoursesAndGrades();
    await loadAllCoursesList();
});

// clearAuth是在api.js里
function logout() {
    clearAuth();
}

/**
 * 加载我的课程和成绩
 */
async function loadMyCoursesAndGrades() {
    try {
        // 直接请求成绩接口获取已选课程及成绩信息
        const res = await fetchWithAuth('/students/my/grades');
        const data = await res.json();

        // 清空旧数据
        const tbody = document.getElementById('myCoursesTableBody');
        tbody.innerHTML = '';

        // 后端返回200(正常情况), 并且里面有数据
        if (data.code === 200 && data.data) {
            // 直接遍历返回的成绩列表
            data.data.forEach(item => {
                const tr = document.createElement('tr');

                // 判断成绩是否为空 (可能是null或undefined)
                let myGrade = (item.grade !== null && item.grade !== undefined) ? item.grade : '暂无成绩';

                tr.innerHTML = `
                    <td>${item.courseId}</td>
                    <td>${item.courseName || '-'}</td>
                    <td><span class="badge bg-${myGrade === '暂无成绩' ? 'secondary' : 'success'}">${myGrade}</span></td>
                    <td>
                        <button class="btn btn-sm btn-danger" onclick="dropCourse(${item.courseId})">退课</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });

            if (data.data.length === 0) {
                 tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">暂无已选课程</td></tr>';
            }
        }
    } catch (e) {
        console.error('加载我的课程失败', e);
    }
}

/**
 * 加载选课大厅的所有课程
 */
async function loadAllCoursesList() {
    try {
        const res = await fetchWithAuth('/courses');
        const data = await res.json();

        const tbody = document.getElementById('allCoursesTableBody');
        tbody.innerHTML = '';

        if (data.code === 200 && data.data) {
            data.data.forEach(course => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${course.courseId}</td>
                    <td>${course.courseName || '-'}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="selectCourse(${course.courseId})">选课</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        }
    } catch (e) {
        console.error('加载课程大厅失败', e);
    }
}

/**
 * 退课
 */
async function dropCourse(courseId) {
    if (!confirm('确定要退掉这门课吗？')) return;
    try {
        const res = await fetchWithAuth(`/students/courses/${courseId}/drop`, {
            method: 'DELETE'
        });
        const data = await res.json();
        if (data.code === 200) {
            alert('退课成功！');
            await loadMyCoursesAndGrades(); // 刷新表格
            await loadAllCoursesList();
        } else {
            alert(data.message || '退课失败');
        }
    } catch (e) {
        console.error(e);
    }
}

/**
 * 选课
 */
async function selectCourse(courseId) {
    try {
        const res = await fetchWithAuth(`/students/courses/${courseId}/select`, {
            method: 'POST'
        });
        const data = await res.json();
        if (data.code === 200) {
            alert('选课成功！');
            await loadMyCoursesAndGrades(); // 同步刷新
        } else {
            alert(data.message || '选课失败');
        }
    } catch (e) {
        console.error(e);
    }
}

async function loadMyProfile() {
    try {
        const res = await fetchWithAuth('/users/my/info');
        const data = await res.json();
        
        const container = document.getElementById('infoContent');
        if (data.code === 200 && data.data) {
            const u = data.data;
            container.innerHTML = `
                <ul class="list-group list-group-flush">
                    <li class="list-group-item"><strong>学生ID：</strong> ${u.id || '-'}</li>
                    <li class="list-group-item"><strong>姓名：</strong> ${u.name || '-'}</li>
                    <li class="list-group-item"><strong>入学年份：</strong> ${u.year || '-'}</li>
                    <li class="list-group-item"><strong>专业：</strong> ${u.major || '-'}</li>
                    <li class="list-group-item"><strong>班级：</strong> ${u.group || '-'}</li>
                </ul>
            `;
        } else {
            container.innerHTML = '<p class="text-danger">加载失败...</p>';
        }
    } catch (e) {
        console.error(e);
    }
}

document.getElementById('my-info-tab').addEventListener('shown.bs.tab', function () {
    loadMyProfile();
});
