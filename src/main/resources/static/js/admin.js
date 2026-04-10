// admin.html用到的js
document.addEventListener("DOMContentLoaded", async () => {
    console.log("管理员页面加载完成");

    if (!getAuthToken() || localStorage.getItem("role") !== "admin") {
        window.location.href = "/index.html"
        clearAuth();
    }

    // 从浏览器localStorage里取出user字符串转为js对象
    const user = JSON.parse(localStorage.getItem("user"));
    document.getElementById("userNameDisplay").textContent = `欢迎! ${user.name}`;

    await loadUsersList();
    await loadCoursesList();
    await loadProfile();
})

function logout() {
    clearAuth();
}

// 加载所有用户
async function loadUsersList() {
    try {
        // 返回Result<List<Object>>
        const response = await fetchWithAuth("/admins/users");
        const result = await response.json();

        console.log(result);

        const tbody = document.getElementById("usersTableBody");
        tbody.innerHTML = "";

        if (result.code === 200) {
            const currentUser = JSON.parse(localStorage.getItem("user"));
            // 对于List里的每一个Object(实际上是多种DTO)
            result.data.forEach(item => {
                let role = "";
                let extra = ""; // 不同用户类型显示的不同属性

                // 根据不同用户类型显示不同内容
                // 如果有major就是学生
                if (item.major !== undefined) {
                    role = "学生";
                    extra = `年级: ${item.year} 专业: ${item.major} 班级: ${item.group}`;
                } else if (item.department !== undefined) {
                    role = "教师";
                    extra = `部门: ${item.department}`;
                } else if (item.level !== undefined) {
                    role = "管理员";
                    extra = `等级: ${item.level}`;
                }

                const isMe = currentUser && String(currentUser.id) === String(item.id);

                // 每一个User, 创建一行数据(table row)
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${item.id}</td>
                    <td>${item.name}</td>
                    <td>${role}</td>
                    <td>${extra}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-info" onclick="openEditUserModal(${JSON.stringify(item).replace(/"/g, "&#39;")}, '${role}')">修改</button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteUser(${item.id})" ${isMe ? 'disabled title="不能删除自己"' : ''}>删除</button>
                    </td>
                `;
                tbody.appendChild(tr);
            })
        }

    } catch (e) {
        console.error(e);
    }
}

function openCreateUserModal() {
    document.getElementById('createUserForm').reset();

    document.getElementById('userModalTitle').textContent = '新建用户';
    document.getElementById('editUserId').value = '';
    document.getElementById('newUserRole').disabled = false;
    document.getElementById("newUserPass").placeholder = "请输入密码";

    roleChange();
    const modal = new bootstrap.Modal(document.getElementById('createUserModal'));
    modal.show();
}

// 根据不同的角色显示不同的信息
function roleChange() {
    const role = document.getElementById('newUserRole').value;
    document.getElementById('studentFields').classList.add('d-none');
    document.getElementById('teacherFields').classList.add('d-none');
    document.getElementById('adminFields').classList.add('d-none');

    if (role === 'student') document.getElementById('studentFields').classList.remove('d-none');
    if (role === 'teacher') document.getElementById('teacherFields').classList.remove('d-none');
    if (role === 'admin') document.getElementById('adminFields').classList.remove('d-none');
}

// 点击用户列表右面的修改弹出的弹窗
function openEditUserModal(user, role) {
    // 修改用户需要先把弹窗里的信息改成用户原来的信息
    document.getElementById("createUserForm").reset();

    document.getElementById("userModalTitle").textContent = "修改用户 " + user.name;
    document.getElementById("editUserId").value = user.id;

    // 根据传进来的Role把roleVal变成英文, 因为html里select里面option是英文的
    let roleVal = "student";
    if (role === "教师") {
        roleVal = "teacher";
    } else if (role === "管理员") {
        roleVal = "admin";
    }

    document.getElementById("newUserRole").value = roleVal;
    document.getElementById("newUserRole").disabled = true;
    roleChange();

    // 把userName部分填上用户原来的name
    document.getElementById("newUserName").value = user.name;
    // 密码栏
    document.getElementById("newUserPass").value = "";
    document.getElementById("newUserPass").placeholder = "留空则不修改密码";

    if (roleVal === "student") {
        document.getElementById("stuYear").value = user.year;
        document.getElementById("stuMajor").value = user.major;
        document.getElementById("stuGroup").value = user.group;
    } else if (roleVal === "teacher") {
        document.getElementById("teacherDept").value = user.department;
    } else if (roleVal === "admin") {
        document.getElementById("adminLevel").value = user.level;
    }

    const modal = new bootstrap.Modal(document.getElementById("createUserModal"));
    modal.show();
}

// html中提交绑定的函数
async function createUser() {
    const isEdit = document.getElementById('editUserId').value !== '';
    const editId = document.getElementById('editUserId').value;
    const role = document.getElementById('newUserRole').value;
    const name = document.getElementById('newUserName').value;
    const pass = document.getElementById('newUserPass').value;

    if (!isEdit && (!name || !pass)) return alert("姓名和密码必须填写！");

    let endpoint = isEdit ? '/users/modify' : '/users/' + role;
    let method = isEdit ? 'PUT' : 'POST';

    let payload = {
        name: name,
        role: role
    };

    if (isEdit) {
        payload.id = Number(editId);
        if (pass.trim() !== '') {
            payload.password = pass;
        }
    } else {
        payload.password = pass;
    }

    if (role === 'student') {
        payload.enrollment_year = document.getElementById('stuYear').value;
        payload.major = document.getElementById('stuMajor').value;
        payload.student_group = document.getElementById('stuGroup').value;
    } else if (role === 'teacher') {
        payload.department = document.getElementById('teacherDept').value;
    } else if (role === 'admin') {
        payload.level = document.getElementById('adminLevel').value;
    }

    try {
        const res = await fetchWithAuth(endpoint, {
            method: method,
            body: JSON.stringify(payload)
        });
        const data = await res.json();

        if (data.code === 200) {
            alert(isEdit ? '修改用户成功！' : ('成功创建用户！分配的新ID为：' + data.data));
            bootstrap.Modal.getInstance(document.getElementById('createUserModal')).hide();
            await loadUsersList();
        } else {
            alert('操作失败报错：' + data.message);
        }
    } catch (e) {
        console.error(e);
        alert('系统异常');
    }
}

async function deleteUser(userId) {
    if (!confirm('确定要删除ID为 ' + userId + ' 的用户吗？')) return;

    try {
        const res = await fetchWithAuth('/users/delete', {
            method: 'DELETE',
            body: JSON.stringify({ id: Number(userId) })
        });

        const data = await res.json();
        if (data.code === 200) {
            alert('删除用户成功');
            await loadUsersList();
        } else {
            alert('删除失败: ' + data.message);
        }
    } catch (e) {
        alert('系统异常');
        console.error(e);
    }
}

// 加载课程管理
async function loadCoursesList() {
    try {
        // 返回Result<List<CourseInfoResponseDTO>>
        const response = await fetchWithAuth("/courses");
        const result = await response.json();

        console.log(result);

        const tbody = document.getElementById("coursesTableBody");
        tbody.innerHTML = "";

        if (result.code === 200) {
            // 每一个课程
            result.data.forEach(item => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${item.courseId}</td>
                    <td>${item.courseName}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-info" onclick="openModifyCourseModal(${item.courseId}, '${item.courseName}')">修改</button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteCourse(${item.courseId})">删除</button>
                    </td>
                `;
                tbody.appendChild(tr);
            })
        }
    } catch (e) {
        console.error(e);
    }
}

function openCreateCourseModal() {
    document.getElementById("courseModalTitle").textContent = "新建课程";
    document.getElementById('editCourseId').value = '';
    document.getElementById('newCourseName').value = '';
    document.getElementById('newCourseTeacherIds').value = '';
    const modal = new bootstrap.Modal(document.getElementById('createCourseModal'));
    modal.show();
}

function openModifyCourseModal(id, name) {
    document.getElementById('courseModalTitle').textContent = '修改课程';
    document.getElementById('editCourseId').value = id;
    document.getElementById('newCourseName').value = name;
    document.getElementById('newCourseTeacherIds').value = '';
    const modal = new bootstrap.Modal(document.getElementById('createCourseModal'));
    modal.show();
}

async function deleteCourse(courseId) {
    if (!confirm('确定要删除该课程吗?')) return;
    try {
        const res = await fetchWithAuth('/courses/delete', {
            method: 'DELETE',
            body: JSON.stringify({ id: Number(courseId) })
        });

        const data = await res.json();
        if (data.code === 200) {
            alert('删除课程成功');
            await loadCoursesList();
        } else {
            alert('删除失败: ' + data.message);
        }
    } catch (e) {
        alert('系统异常');
        console.error(e);
    }
}

async function submitCourse() {
    const isEdit = document.getElementById('editCourseId').value !== '';
    const editId = document.getElementById('editCourseId').value;
    const name = document.getElementById('newCourseName').value;
    const teacherStr = document.getElementById('newCourseTeacherIds').value;

    if (!name) return alert('课程名称不能为空');

    let teacherIds = null;
    if (teacherStr.trim() !== '') {
        teacherIds = teacherStr.split(',')
            .map(s => s.trim())
            .filter(s => s !== '')
            .map(Number);
    }

    // 如果没有输入教师，防止传 null，可以传空数组 []
    if (teacherIds === null) {
        teacherIds = [];
    }

    const endpoint = isEdit ? '/courses/modify' : '/courses/create';
    const method = isEdit ? 'PUT' : 'POST';
    const payload = isEdit ? {
        id: Number(editId),
        name: name,
        teacherIds: teacherIds
    } : {
        name: name,
        teacherIds: teacherIds
    };

    try {
        const res = await fetchWithAuth(endpoint, {
            method: method,
            body: JSON.stringify(payload)
        });

        const data = await res.json();
        if (data.code === 200) {
            alert(isEdit ? '修改课程成功' : '创建课程成功');
            bootstrap.Modal.getInstance(document.getElementById('createCourseModal')).hide();
            await loadCoursesList(); // 刷新表格
        } else {
            alert('操作失败: ' + data.message);
        }
    } catch (e) {
        alert('系统异常');
        console.error(e);
    }
}

async function loadProfile() {
    try {
        const res = await fetchWithAuth('/users/my/info');
        const data = await res.json();

        const container = document.getElementById('adminInfoContent');

        if (data.code === 200 && data.data) {
            const u = data.data;
            container.innerHTML = `
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">管理员ID: ${u.id}</li>
                    <li class="list-group-item">管理员姓名: ${u.name}</li>
                    <li class="list-group-item">管理员级别: ${u.level}</li>
                </ul>
            `;
        } else {
            container.innerHTML = '<p>加载失败</p>';
        }
    } catch (e) {
        console.error(e);
    }
}