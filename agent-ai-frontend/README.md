# AI 应用中心前端

Vue3 + Vite 前端项目，用于连接 PolyModal AI Agent 后端服务。

## 功能

- **主页**：切换不同 AI 应用
- **AI 游戏大师**：聊天室风格，SSE 流式对话，调用 `doChatWithAppByStream`
- **AI 超级智能体**：同上，调用 `doChatWithWManus`

## 技术栈

- Vue 3
- Vue Router
- Axios
- Vite

## 开发

```bash
# 安装依赖
npm install

# 启动开发服务器（默认 http://localhost:5173）
npm run dev
```

确保后端运行在 `http://localhost:8023`（或修改 `vite.config.js` 中 proxy 的 target），前端会通过代理访问 `/api`。

## 环境变量

`.env` 中可配置：

- `VITE_API_BASE_URL`：后端 API 前缀，默认 `/api`（开发走代理）

## 构建

```bash
npm run build
npm run preview
```
