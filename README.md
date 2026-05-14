# DLYK 项目（后端 + 前端）

本仓库包含 **Spring Boot 后端** 与 **Vue 3 + Vite 前端**，便于同一版本管理与联调。

## 目录说明

| 路径 | 说明 |
| --- | --- |
| 根目录 `pom.xml`、`src/` | Java 后端服务 |
| `dlyk-front/` | 前端工程（与本地原同级 `dlyk-front` 为同一份源码结构） |

## 后端

在项目根目录执行 Maven 构建与运行（需本机已安装 JDK、Maven）。

## 前端

进入前端目录安装依赖并启动开发服务：

```bash
cd dlyk-front
npm install
npm run dev
```

生产构建：`npm run build`，产物在 `dlyk-front/dist/`（已加入 `.gitignore`，不提交到 Git）。
