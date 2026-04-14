# 黑龙江大学-软件工程综合实践A
## 成绩管理系统 (Grade Management System)

本项目是一个基于 Spring Boot 的成绩管理系统。系统提供针对学生、教师和管理员三种角色的多权限访问和管理功能。

## 功能特性

- **简易Token认证**：使用 自定义Token (格式: `token-role-id`) 实现身份验证。
- **角色权限控制**：
  - **管理员**：用户管理、课程信息管理。
  - **教师**：负责教授课程，可查询所授课程学生名单，并且具有修改、录入学生成绩权限。
  - **学生**：参与选课管理，查看个人基本信息，查询个人成绩。
- **RESTful API**：后端提供 RESTful 风格接口。
- **前端**：使用 HTML/CSS/JavaScript 进行界面数据绑定及渲染。

## 技术栈

- **后端**：Java 17, Spring Boot, Spring Data JPA, H2 数据库
- **前端**：HTML, CSS, 原生 JS
- **构建工具**：Gradle

## 部署说明

1. 确保已安装 JDK 17+。
2. 执行 `git clone https://github.com/BlackTeaOff/HLJU_GradeManagementSystem.git`
3. 运行 `gradlew bootRun` 启动项目。
4. 打开浏览器访问 `http://localhost:8080` 进入系统登录页。

## 数据初始化

详见项目目录 `config/DataInitializer.java`

### 默认用户

| ID | name   | password  | role    |
|----|--------|-----------|---------|
| 1  | admin  | 123456    | admin   |
| 2  | 张老师    | 123456    | teacher |
| 3  | 刘老师    | 123456    | teacher |
| 4  | 小明     | 123456    | student |
| 5  | 小坤     | 123456    | student |

## 数据库

本项目采用**H2数据库**

数据库控制台: `http://localhost:8080/h2-console`

IDEA Database URL: `jdbc:h2:tcp://localhost:9092/mem:testdb`

修改H2端口详见: `config/H2ServerConfig.java`

## API测试

### Swagger

本项目使用 **Swagger** 提供API文档和测试界面

浏览器访问: `http://localhost:8080/swagger-ui/index.html`

API参数详见**Swagger文档**或 `controller/` 目录

### HTTP Client

使用 IDEA 内置的 **HTTP Client** 进行API测试

详见: `src/test/http/AllApiTest.http`