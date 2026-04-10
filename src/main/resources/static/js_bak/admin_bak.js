document.addEventListener('DOMContentLoaded', async () => {
    // Already checked basic token in HTML script

    window.currentUser = JSON.parse(localStorage.getItem('user'));

    await loadUsersList();
    await loadCoursesList();
});

/**
 * 加载系统所有用户
 */
async function loadUsersList() {
    try {
        const tbody = document.getElementById('usersTableBody');
        tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">加载中...</td></tr>';

        const res = await fetchWithAuth('/admins/users');
        const data = await res.json();

        tbody.innerHTML = '';
        if (data.code === 200 && data.data) {
            data.data.forEach(u => {
                let role = '未知';
                let extra = '';

                // Determine logic basic on object shape
                if (u.major !== undefined) {
                    role = '学生';
                    extra = `年级:${u.year || '-'} 专业:${u.major || '-'} 班级:${u.group || '-'}`;
                } else if (u.department !== undefined) {
                    role = '教师';
                    extra = `所属:${u.department || '-'}`;
                } else if (u.level !== undefined) {
                    role = '管理员';
                    extra = `等级:${u.level || '-'}`;
                } else {
                    role = u.role ? u.role : '用户';
                }

                const isMe = window.currentUser && String(window.currentUser.id) === String(u.id);

                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${u.id}</td>
                    <td>${u.name || '-'}</td>
                    <td>${role}</td>
                    <td><span class="text-secondary opacity-75">${extra}</span></td>
                    <td>
                        <button class="btn btn-sm btn-outline-info me-1" onclick='showEditUserModal(${JSON.stringify(u).replace(/'/g, "&#39;")}, "${role}")'>修改</button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteUser(${u.id})" ${isMe ? 'disabled title="不能删除自己"' : ''}>删除</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        }
    } catch (e) {
        console.error('加载用户列表失败', e);
    }
}

/**
 * 加载课程大厅 (所有人可用接口, 借用)
 */
async function loadCoursesList() {
    try {
        const tbody = document.getElementById('coursesTableBody');
        tbody.innerHTML = '<tr><td colspan="3" class="text-center text-muted">加载中...</td></tr>';

        const res = await fetchWithAuth('/courses');
        const data = await res.json();

        tbody.innerHTML = '';
        if (data.code === 200 && data.data) {
            data.data.forEach(c => {
                 const tr = document.createElement('tr');
                 tr.innerHTML = `
                    <td>${c.courseId}</td>
                    <td>${c.courseName || '-'}</td>
                    <td>
                       <button class="btn btn-sm btn-outline-info me-2" onclick="showModifyCourseModal(${c.courseId}, '${c.courseName}')">修改</button>
                       <button class="btn btn-sm btn-outline-danger" onclick="deleteCourse(${c.courseId})">删除</button>
                    </td>
                 `;
                 tbody.appendChild(tr);
            });
        }
    } catch (e) {
        console.error('加载全校课程列表失败', e);
    }
}

function showCreateCourseModal() {
    document.getElementById('newCourseName').value = '';
    document.getElementById('newCourseTeacherIds').value = '';
    const modal = new bootstrap.Modal(document.getElementById('createCourseModal'));
    modal.show();
}

async function createCourse() {
    const name = document.getElementById('newCourseName').value;
    const teacherStr = document.getElementById('newCourseTeacherIds').value;

    if (!name) return alert('课程名称不能为空');

    const teacherIds = teacherStr.split(',')
        .map(s => s.trim())
        .filter(s => s !== '')
        .map(Number);

    try {
        const res = await fetchWithAuth('/courses/create', {
            method: 'POST',
            body: JSON.stringify({
                name: name,
                teacherIds: teacherIds
            })
        });

        const data = await res.json();
        if (data.code === 200) {
            alert('创建课程成功');
            // Hide modal
            bootstrap.Modal.getInstance(document.getElementById('createCourseModal')).hide();
            await loadCoursesList(); // 刷新表格
        } else {
            alert('创建失败: ' + data.message);
        }
    } catch (e) {
        alert('系统异常');
        console.error(e);
    }
}

async function deleteCourse(courseId) {
    if (!confirm('确定要删除该课程吗？这可能会影响学生选课情况！')) return;
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

function showCreateUserModal() {
    document.getElementById('createUserForm').reset();

    // Switch modal state to create mode
    document.getElementById('userModalTitle').textContent = '新建用户';
    document.getElementById('editUserId').value = '';
    document.getElementById('newUserRole').disabled = false;

    onRoleChange();
    const modal = new bootstrap.Modal(document.getElementById('createUserModal'));
    modal.show();
}

function showEditUserModal(u, roleName) {
    document.getElementById('createUserForm').reset();

    document.getElementById('userModalTitle').textContent = '修改用户: ' + u.name;
    document.getElementById('editUserId').value = u.id;

    // Map human readable role to option value and disable editing of role itself
    let roleVal = 'student';
    if (roleName === '教师') roleVal = 'teacher';
    else if (roleName === '管理员') roleVal = 'admin';

    document.getElementById('newUserRole').value = roleVal;
    document.getElementById('newUserRole').disabled = true; // DO NOT allow changing roles directly
    onRoleChange();

    document.getElementById('newUserName').value = u.name || '';
    document.getElementById('newUserPass').value = ''; // leave blank = no change
    document.getElementById('newUserPass').placeholder = '留空则不修改密码';

    if (roleVal === 'student') {
        document.getElementById('stuYear').value = u.year || '';
        document.getElementById('stuMajor').value = u.major || '';
        document.getElementById('stuGroup').value = u.group || '';
    } else if (roleVal === 'teacher') {
        document.getElementById('teacherDept').value = u.department || '';
    } else if (roleVal === 'admin') {
        document.getElementById('adminLevel').value = u.level || '';
    }

    const modal = new bootstrap.Modal(document.getElementById('createUserModal'));
    modal.show();
}

function onRoleChange() {
    const role = document.getElementById('newUserRole').value;
    document.getElementById('studentFields').classList.add('d-none');
    document.getElementById('teacherFields').classList.add('d-none');
    document.getElementById('adminFields').classList.add('d-none');

    if (role === 'student') document.getElementById('studentFields').classList.remove('d-none');
    if (role === 'teacher') document.getElementById('teacherFields').classList.remove('d-none');
    if (role === 'admin') document.getElementById('adminFields').classList.remove('d-none');
}

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

function showCreateCourseModal() {
    document.getElementById('courseModalTitle').textContent = '新建课程';
    document.getElementById('editCourseId').value = '';
    document.getElementById('newCourseName').value = '';
    document.getElementById('newCourseTeacherIds').value = '';
    const modal = new bootstrap.Modal(document.getElementById('createCourseModal'));
    modal.show();
}

function showModifyCourseModal(id, name) {
    document.getElementById('courseModalTitle').textContent = '修改课程';
    document.getElementById('editCourseId').value = id;
    document.getElementById('newCourseName').value = name;
    // We cannot easily pre-fill teacher IDs without another API, so leave it empty or as a hint
    document.getElementById('newCourseTeacherIds').value = '';
    const modal = new bootstrap.Modal(document.getElementById('createCourseModal'));
    modal.show();
}

async function submitCourse() {
    const isEdit = document.getElementById('editCourseId').value !== '';
    const editId = document.getElementById('editCourseId').value;
    const name = document.getElementById('newCourseName').value;
    const teacherStr = document.getElementById('newCourseTeacherIds').value;

    if (!name) return alert('课程名称不能为空');

    const teacherIds = teacherStr.split(',')
        .map(s => s.trim())
        .filter(s => s !== '')
        .map(Number);

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

async function loadMyProfile() {
    try {
        const res = await fetchWithAuth('/users/my/info');
        const data = await res.json();

        const container = document.getElementById('adminInfoContent');

        if (data.code === 200 && data.data) {
            const u = data.data;
            container.innerHTML = `
                <ul class="list-group list-group-flush">
                    <li class="list-group-item"><strong>管理员ID：</strong> ${u.id || '-'}</li>
                    <li class="list-group-item"><strong>管理员姓名：</strong> ${u.name || '-'}</li>
                    <li class="list-group-item"><strong>管理员级别：</strong> <span class="badge bg-primary">${u.level || '未知'}</span></li>
                    <li class="list-group-item"><strong>账户角色：</strong> ${u.role || 'ADMIN'}</li>
                </ul>
            `;
        } else {
            container.innerHTML = '<p class="text-danger">加载失败...</p>';
        }
    } catch (e) {
        console.error(e);
    }
}

// Attach event listener for tabs
document.getElementById('my-info-tab').addEventListener('shown.bs.tab', function () {
    loadMyProfile();
});
