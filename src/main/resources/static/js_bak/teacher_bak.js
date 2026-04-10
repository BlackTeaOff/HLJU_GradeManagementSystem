document.addEventListener('DOMContentLoaded', async () => {
    if (!getAuthToken()) {
        window.location.href = '/index.html';
        return;
    }

    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        document.getElementById('userNameDisplay').textContent = `欢迎老师，${user.name || user.id}`;
    }

    await loadMyCourses();
});

function logout() {
    clearAuth();
}

/**
 * 加载老师自己教的课程
 */
async function loadMyCourses() {
    try {
        const tbody = document.getElementById('myCoursesTableBody');
        tbody.innerHTML = '';

        const coursesRes = await fetchWithAuth('/teachers/my/courses');
        const coursesData = await coursesRes.json();

        if (coursesData.code === 200 && coursesData.data) {
            coursesData.data.forEach(course => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${course.courseId}</td>
                    <td>${course.courseName || '-'}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-success" onclick="openGradesModal(${course.courseId}, '${course.courseName}')">
                            管理成绩
                        </button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
            if(coursesData.data.length === 0){
                tbody.innerHTML = '<tr><td colspan="3" class="text-center text-muted">暂无教授课程</td></tr>';
            }
        }
    } catch (e) {
        console.error('加载教课信息失败', e);
    }
}

/**
 * 打开成绩模态框，查询这门课下所有学生的成绩
 */
async function openGradesModal(courseId, courseName) {
    document.getElementById('gradesModalLabel').textContent = `《${courseName}》- 成绩管理名单`;
    document.getElementById('currentCourseId').value = courseId;

    // 打开 Boostrap Modal
    const modalElement = document.getElementById('gradesModal');
    const bsModal = new bootstrap.Modal(modalElement, { keyboard: false });
    bsModal.show();

    // 加载学生名单与他们分数的重构逻辑
    await refreshStudentGrades(courseId);
}

/**
 * 获取并渲染某个课程内特定讲师所有学生的成绩
 */
async function refreshStudentGrades(courseId) {
    try {
        const tbody = document.getElementById('studentsTableBody');
        tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">加载中...</td></tr>';

        // 实际上老师查询成绩接口已经返回了所有学生的名单及其当前分数(含为null情况)
        const gradesRes = await fetchWithAuth(`/teachers/${courseId}/grades`);
        const gradesData = await gradesRes.json();

        tbody.innerHTML = '';
        if (gradesData.code === 200 && gradesData.data) {
            gradesData.data.forEach(student => {
                const currentGrade = student.grade; // null if not graded
                const isModify = (currentGrade !== null && currentGrade !== undefined);

                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${student.studentId}</td>
                    <td>${student.studentName || '未命名'}</td>
                    <td>
                        <span class="badge ${isModify ? 'text-bg-info' : 'text-bg-secondary'} px-3 py-2 fs-6">
                            ${isModify ? currentGrade : '未录入'}
                        </span>
                    </td>
                    <td>
                        <div class="input-group input-group-sm mb-0 mx-auto" style="width: 250px;">
                          <input type="number" step="0.1" class="form-control text-center" id="input_grade_${student.studentId}" placeholder="输入新分数">
                          <button class="btn btn-primary" onclick="submitGrade(${courseId}, ${student.studentId}, ${isModify})">
                              ${isModify ? '修改' : '录入'}
                          </button>
                        </div>
                    </td>
                `;
                tbody.appendChild(tr);
            });
             if(gradesData.data.length === 0){
                tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">这门课暂无学生选修。</td></tr>';
            }
        }
    } catch (e) {
        console.error('加载学生名单成绩失败', e);
    }
}

/**
 * 录入/修改分数调用接口
 */
async function submitGrade(courseId, studentId, isModify) {
    const inputVal = document.getElementById(`input_grade_${studentId}`).value;
    if (!inputVal) {
        alert("请输入分数后提交！");
        return;
    }

    try {
        const payload = {
            studentId: Number(studentId),
            courseId: Number(courseId),
            grade: Number(inputVal)
        };

        const res = await fetchWithAuth('/teachers/grades', {
            method: isModify ? 'PUT' : 'POST', // POST for new, PUT for modify
            body: JSON.stringify(payload)
        });

        const data = await res.json();

        if (data.code === 200) {
            alert((isModify ? '修改' : '录入') + '成绩成功!');
            // 提交完刷新这个界面的表格数据
            await refreshStudentGrades(courseId);
            // 清理输入框
            document.getElementById(`input_grade_${studentId}`).value = '';
        } else {
            alert(data.message || '成绩操作失败');
        }

    } catch (e) {
        console.error(e);
        alert("网络或系统错误，请重试！");
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
                    <li class="list-group-item"><strong>教工号 (ID)：</strong> ${u.id || '-'}</li>
                    <li class="list-group-item"><strong>姓名：</strong> ${u.name || '-'}</li>
                    <li class="list-group-item"><strong>所属学院：</strong> ${u.department || '暂无'}</li>
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
