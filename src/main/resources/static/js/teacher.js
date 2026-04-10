// teacher.html用到的js
document.addEventListener("DOMContentLoaded", async () => {
    console.log("教师页面加载完成");

    if (!getAuthToken() || localStorage.getItem("role") !== "teacher") {
        window.location.href = "/index.html";
        clearAuth();
    }

    const user = JSON.parse(localStorage.getItem("user"));

    document.getElementById("userNameDisplay").textContent = `欢迎! ${user.name}`;

    await loadMyCourses();
    await loadProfile();
})

function logout() {
    clearAuth();
}

// 加载主页我的教授课程
async function loadMyCourses() {
    try {
        const courseResponse = await fetchWithAuth("/teachers/my/courses");
        // 从HTTP回应取出Body, 里面就是Result类, data是List<TeacherCourseResponseDTO>
        const courseResult = await courseResponse.json();

        console.log(courseResult);

        // 在html中找到TableBody
        const tbody = document.getElementById("myCoursesTableBody");
        tbody.innerHTML = "";

        // 后端逻辑正确, Result.success返回code是200, .error返回code是400
        if (courseResult.code === 200) {
            // List中的每一个TeacherCourseResponseDTO
            courseResult.data.forEach(item => {
                // 创建一个新表格行(table row)
                const tr = document.createElement("tr");
                // 向这一行填充数据(table data)
                tr.innerHTML = `
                    <td>${item.courseId}</td>
                    <td>${item.courseName}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary" onclick="openGradesModel(${item.courseId}, '${item.courseName}')">管理成绩</button>
                    </td>
                `;
                // 把这一行追加到表格中
                tbody.appendChild(tr);
            })
            if (courseResult.data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" class="text-center text-muted">暂无教授课程</td></tr>';
            }
        }
    } catch (e) {
        console.error(e);
    }
}

// 与某门课程的管理成绩按钮绑定, 按下后弹出相应课程的模态框(弹窗)
async function openGradesModel(courseId, courseName) {
    // 找到模态框上方的标签元素, 动态渲染课程名字
    document.getElementById("gradesModalLabel").textContent = `${courseName} - 成绩管理`;
    // document.getElementById("currentCourseId");
    // 找到html里面的模态框部分
    const modalElement = document.getElementById("gradesModal");
    const bootStrapModal = new bootstrap.Modal(modalElement, { keyboard: false });
    bootStrapModal.show();

    await loadStudentGrades(courseId);
}

// 在模态框里显示学生信息
async function loadStudentGrades(courseId) {
    try {
        const gradeResponse = await fetchWithAuth(`/teachers/${courseId}/grades`);
        const gradeResult = await gradeResponse.json();
        console.log(gradeResult);

        const tbody = document.getElementById("studentsTableBody");
        tbody.innerHTML = "";

        if (gradeResult.code === 200) {
            gradeResult.data.forEach(item => {
                const grade = item.grade; // 没录入成绩就是null
                const isModify = (grade !== null); // 如果成绩不为null, 那就是修改成绩
                console.log("Grade: " + grade);
                console.log("isModify: " + isModify);

                // 为每个成绩数据添加一行(table row)
                const tr = document.createElement("tr");
                // 填充这一行每一列的数据(table data)
                tr.innerHTML = `
                    <td>${item.studentId}</td>
                    <td>${item.studentName}</td>
                    <td>${isModify ? grade : "未录入"}</td> <!--如果不为空就显示grade, 否则显示未录入-->
                    <td>
                        <div class="input-group input-group-sm mb-0 mx-auto" style="width: 250px;">
                            <input type="number" step="0.1" class="form-control text-center" id="input_grade_${item.studentId}">
                            <button class="btn btn-primary" onclick="submitGrade(${courseId}, ${item.studentId}, ${isModify})">${isModify ? "修改" : "录入"}</button> <!--修改过就显示修改-->
                        </div>
                    </td>
                `;
                tbody.appendChild(tr);
            })
            if (gradeResult.data.length === 0) {
                tbody.innerHTML= '<tr><td colspan="4" class="text-center text-muted">暂无学生选修</td></tr>';
            }
        }
    } catch (e) {
        console.log(e);
    }
}

// 录入/修改成绩按钮绑定这个函数
async function submitGrade(courseId, studentId, isModify) {
    // 拿到弹窗里对应学生input里面的值
    const inputVal = document.getElementById(`input_grade_${studentId}`).value;
    if (!inputVal) {
        alert("请输入分数!");
        return;
    }
    try {
        // 组成后端需要的GradeInputRequestDTO, 把字符串转成数字
        const payload = {
            studentId: Number(studentId),
            courseId: Number(courseId),
            grade: Number(inputVal)
        };

        // 教师录入/修改成绩返回Result<Void>, 中途出现错误会返回Result.error
        const response = await fetchWithAuth("/teachers/grades", {
            // 如果修改成绩用PUT, 不是用POST
            method: isModify ? "PUT" : "POST",
            // 把JS对象转为字符串传到后端
            body: JSON.stringify(payload)
        })

        const result = await response.json();
        console.log(result);

        if (result.code === 200) {
            alert((isModify ? "修改" : "录入") + "成绩成功");
            await loadStudentGrades(courseId);
            // 清理输入框
            document.getElementById(`input_grade_${studentId}`).value = "";
        } else {
            alert(result.message || "操作失败");
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

        const container = document.getElementById("infoContent");
        if (result.code === 200) {
            const data = result.data;
            container.innerHTML = `
                <ul class="list-group">
                    <li class="list-group-item">教师ID: ${data.id}</li>
                    <li class="list-group-item">姓名: ${data.name}</li>
                    <li class="list-group-item">部门: ${data.department}</li>
                </ul>
            `;
        } else {
            container.innerHTML = '<p>加载失败</p>';
        }
    } catch (e) {
        console.error(e);
    }
}