# Heartfelt Tutoring 安卓客户端

这是一个基于后端API的安卓客户端应用，实现了学习计划管理功能。

## 功能特性

- ✅ 用户认证管理
- ✅ 学习计划CRUD操作
- ✅ 计划完成状态管理
- ✅ 响应式UI设计
- ✅ 网络请求封装

## API接口

基于后端API规范实现：

### 计划管理接口
- `GET /api/plans/user/{userId}` - 获取用户计划
- `GET /api/plans` - 获取所有计划
- `GET /api/plans/{id}` - 根据ID获取计划
- `POST /api/plans` - 创建计划
- `PUT /api/plans/{id}` - 更新计划
- `DELETE /api/plans/{id}` - 删除计划

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/verify` - Token验证

## 项目结构

```
app/src/main/java/cn/arorms/android/ht/client/
├── models/           # 数据模型
│   └── Plan.kt
├── network/          # 网络层
│   ├── ApiService.kt
│   └── AuthManager.kt
├── repository/       # 数据仓库
│   └── PlanRepository.kt
└── ui/plans/         # UI层
    ├── PlansFragment.kt
    ├── PlansViewModel.kt
    └── PlansAdapter.kt
```

## 技术栈

- **架构**: MVVM + Repository
- **网络**: Retrofit + OkHttp
- **异步**: Kotlin Coroutines + Flow
- **UI**: Jetpack Compose (Data Binding)
- **导航**: Navigation Component
- **依赖注入**: ViewModel

## 使用说明

1. 启动后端服务器 (localhost:8080)
2. 构建并运行安卓应用
3. 在侧边栏导航中选择"学习计划"
4. 点击右下角FAB按钮添加新计划
5. 点击复选框标记计划完成状态
6. 点击删除按钮删除计划

## 构建说明

```bash
./gradlew build
```

## 注意事项

- 确保后端服务器正在运行
- 需要网络权限
- 使用JWT Token进行认证
- 支持离线数据缓存
