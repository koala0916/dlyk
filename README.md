# 武馆 CRM 系统（dlyk）

**武馆客户关系管理系统**，用于管理武馆的市场活动、招生线索、学员（客户）、课程交易等业务，并提供业绩统计图表分析。

## 功能模块

- **市场活动**：活动的新增、编辑、删除与列表管理
- **线索管理**：招生线索的录入、跟进、编辑日志、Excel 导出、线索转学员
- **客户（学员）管理**：学员档案，含课程类型、剩余课时、课程到期时间
- **交易管理**：课程交易记录与状态管理
- **产品管理**：课程产品信息维护
- **字典管理**：数据字典类型与字典值
- **用户管理**：用户账号、角色分配
- **系统管理**：角色管理、权限管理（菜单级权限控制）
- **图表统计**：业绩分析、数据分析（ECharts 可视化）

## 技术栈

### 后端

| 技术 | 说明 |
| --- | --- |
| Java 21 | 运行环境（JDK 21） |
| Spring Boot 3.5 | 核心框架 |
| Spring Security + JWT | 登录认证与接口鉴权 |
| MyBatis + PageHelper | ORM 与物理分页 |
| MySQL 8 | 数据库 |
| Redis | 登录 token 存储 |
| EasyExcel 4 | Excel 导入导出 |
| Hutool | 验证码、JSON、JWT 工具 |
| Lombok / AOP / Validation | 开发辅助、数据权限切面、参数校验 |

### 前端

| 技术 | 说明 |
| --- | --- |
| Vue 3.4 | 核心框架（组合式 API） |
| Vite 5 | 构建工具 |
| Vue Router 4 | 前端路由 |
| Element Plus 2.8 | UI 组件库 |
| Axios | HTTP 请求 |
| ECharts 5 | 图表统计 |
| file-saver | 前端文件下载（Excel 导出） |

## 目录说明

| 路径 | 说明 |
| --- | --- |
| 根目录 `pom.xml`、`src/` | Java 后端服务（dlyk-server） |
| `src/main/resources/db/dlyk.sql` | 数据库完整导出脚本（建表 + 数据） |
| `dlyk-front/` | 前端工程（Vue 3 + Vite） |

## 快速启动

### 环境要求

- JDK 21、Maven 3.9+
- Node.js 18+（前端）
- MySQL 8（本地 3306）
- Redis（用于登录 token 存储）

### 1. 初始化数据库

```bash
# 创建数据库并导入脚本
mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS dlyk DEFAULT CHARACTER SET utf8mb4;"
mysql -uroot -p dlyk < src/main/resources/db/dlyk.sql
```

按实际环境修改 `src/main/resources/application-dev.yml` 中的数据库账号密码与 Redis 地址。

### 2. 启动后端（端口 8088）

```bash
cd <项目根目录>
mvn spring-boot:run
```

### 3. 启动前端（端口 8080）

```bash
cd dlyk-front
npm install
npm run dev
```

浏览器访问 http://localhost:8080 登录使用。

### 生产构建

前端构建产物输出到 `dlyk-front/dist/`（已加入 `.gitignore`）：

```bash
cd dlyk-front
npm run build
```
