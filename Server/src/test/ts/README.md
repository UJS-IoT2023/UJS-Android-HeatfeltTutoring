# TypeScript WebSocket Chat Test

这个项目是一个用TypeScript编写的实时聊天WebSocket测试程序，用于测试服务器的聊天功能。

## 功能特性

- 使用STOMP协议连接WebSocket服务器
- 订阅指定对话的消息
- 发送测试消息到对话
- 自动接收和显示实时消息
- 包含完整的TypeScript类型支持

## 依赖项

- `@stomp/stompjs`: STOMP客户端库
- `ws`: WebSocket库
- `typescript`: TypeScript编译器
- `@types/node` 和 `@types/ws`: 类型定义

## 安装依赖

```bash
npm install
```

## 编译项目

```bash
npm run build
```

## 运行测试

确保服务器正在运行在 `http://localhost:8080`，然后执行：

### 自动测试 (发送和接收消息)
```bash
npm run test
```

### 监听模式 (只接收消息)
```bash
npm run listen
```

监听模式会持续监听指定对话的消息，不会发送消息。适合用于测试从其他客户端发送的消息是否能正确接收。按 Ctrl+C 退出监听。

## 测试流程

### 自动测试流程
1. 连接到WebSocket服务器 (`ws://localhost:8080/ws`)
2. 订阅对话ID为1的消息频道 (`/topic/dialogue/1`)
3. 每隔几秒发送一条测试消息
4. 显示接收到的所有消息（包括自己发送的回显消息）
5. 7秒后自动断开连接

### 监听模式流程
1. 连接到WebSocket服务器 (`ws://localhost:8080/ws`)
2. 订阅对话ID为1的消息频道 (`/topic/dialogue/1`)
3. 持续监听并显示所有接收到的消息
4. 格式化显示消息详情（发送者、时间、内容等）
5. 按 Ctrl+C 手动停止监听

## 配置参数

在 `index.ts` 文件中可以修改以下参数：

- `WS_URL`: WebSocket服务器地址
- `DIALOGUE_ID`: 要测试的对话ID
- `SENDER_ID`: 发送者ID
- `SENDER_USERNAME`: 发送者用户名

## 服务器端对应接口

### REST API

- `GET /api/chat/dialogues/{userId}`: 获取用户对话列表
- `POST /api/chat/dialogue`: 创建新对话
- `GET /api/chat/dialogue/{dialogueId}/messages`: 获取对话消息历史

### WebSocket

- 端点: `/ws`
- 发送消息: `/app/chat/{dialogueId}/send`
- 订阅消息: `/topic/dialogue/{dialogueId}`
- 加入对话: `/app/chat/{dialogueId}/join`

## 消息格式

发送消息格式：
```json
{
  "dialogueId": 1,
  "senderId": 1,
  "senderUsername": "TestUser",
  "content": "消息内容"
}
```

接收消息格式：
```json
{
  "id": 1,
  "dialogueId": 1,
  "senderId": 1,
  "senderUsername": "TestUser",
  "content": "消息内容",
  "createdAt": "2025-12-10T08:59:39.000Z"
}
