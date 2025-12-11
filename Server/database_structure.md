# 数据库表结构文档

本文档基于项目的JPA实体类，描述了数据库表的结构和关系。

## 表概述

### 1. users - 用户表
基础用户信息表，包含用户认证、个人资料、教师资料、钱包关联等信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(255) | UNIQUE | 用户名 |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 邮箱 |
| password | VARCHAR(255) | NOT NULL | 密码哈希 |
| role | ENUM('STUDENT','TEACHER') | DEFAULT 'STUDENT' | 用户角色 |
| google_id | VARCHAR(255) | UNIQUE | Google OAuth ID |
| wechat_openid | VARCHAR(255) | UNIQUE | 微信OpenID |
| qq_openid | VARCHAR(255) | UNIQUE | QQ OpenID |
| teacher_profile_id | BIGINT | FOREIGN KEY(teacher_profile.id) | 教师资料ID |
| wallet_id | BIGINT | FOREIGN KEY(wallets.id) | 钱包ID |
| phone_number | VARCHAR(20) | UNIQUE | 手机号 |
| avatar_url | VARCHAR(255) | 头像URL |
| real_name | VARCHAR(255) | 真实姓名 |
| gender | VARCHAR(255) | 性别 |
| wechat_id | VARCHAR(255) | UNIQUE | 微信号 |
| qq_id | VARCHAR(255) | UNIQUE | QQ号 |
| address | VARCHAR(255) | 地址 |
| created_at | DATETIME | NOT NULL | 创建时间 |

### 2. wallets - 钱包表
用户钱包账户信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 钱包ID |
| balance | DOUBLE | NOT NULL | 账户余额 |
| points | DOUBLE | NOT NULL | 积分 |

### 3. teachers_profile - 教师资料表
教师的专业资料和教学信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 教师资料ID |
| educational_background | VARCHAR(255) | 教育背景 |
| taught_grades | VARCHAR(255) | 教授年级 |
| taught_subject | ENUM(...) | 教授科目 |
| taught_courses | VARCHAR(255) | 教授课程 |

### 4. appointments - 预约表
课程预约信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 预约ID |
| user_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 学生用户ID |
| teacher_user_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 教师用户ID |
| subject | VARCHAR(255) | NOT NULL | 科目 |
| appointment_date | DATETIME | NOT NULL | 预约时间 |

### 5. orders - 订单表
课程预约订单信息。（注意：此表结构与业务逻辑不匹配，建议检查）

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 订单ID |
| user_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 用户ID |
| bookname | VARCHAR(255) | NOT NULL | 图书名称 |
| count | INT | NOT NULL | 数量 |
| price | DOUBLE | NOT NULL | 价格 |
| state | VARCHAR(255) | NOT NULL | 状态 |

### 6. plan - 教学计划表
用户的学习或教学计划。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 计划ID |
| user_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 用户ID |
| content | VARCHAR(255) | NOT NULL | 计划内容 |
| deadline | DATETIME | NOT NULL | 截止时间 |
| is_completed | BOOLEAN | NOT NULL | 是否完成 |

### 7. comments - 评论表
用户间的评论信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 评论ID |
| from_user_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 评论者ID |
| to_user_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 被评论者ID |
| content | VARCHAR(255) | NOT NULL | 评论内容 |
| created_at | DATETIME | 创建时间 |

### 8. rewards - 奖励表
用户奖励信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 奖励ID |
| user_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 用户ID |
| account | DOUBLE | NOT NULL | 奖励金额 |

### 9. chat_dialogue - 聊天对话表
聊天对话信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 对话ID |
| dialogue_type | ENUM('ONE_ON_ONE','GROUP') | NOT NULL | 对话类型 |
| title | VARCHAR(255) | 对话标题 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| last_message_content | VARCHAR(255) | 最后一条消息 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

### 10. chat_dialogue_participant - 聊天对话参与者表
对话参与者信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 参与者ID |
| dialogue_id | BIGINT | FOREIGN KEY(chat_dialogue.id), NOT NULL | 对话ID |
| participant_user_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 参与者用户ID |
| join_at | DATETIME | NOT NULL | 加入时间 |

### 11. chat_messages - 聊天消息表
聊天消息内容。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 消息ID |
| dialogue_id | BIGINT | FOREIGN KEY(chat_dialogue.id) | 对话ID |
| sender_id | BIGINT | FOREIGN KEY(users.id), NOT NULL | 发送者ID |
| content | VARCHAR(255) | NOT NULL | 消息内容 |
| created_at | DATETIME | NOT NULL | 发送时间 |

### 12. email_verification_codes - 邮箱验证码表
邮箱验证信息。

| 字段 | 类型 | 约束 | 描述 |
|------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 验证码ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 邮箱地址 |
| code | VARCHAR(6) | NOT NULL | 验证码 |
| expires_at | DATETIME | NOT NULL | 过期时间 |
| verified | BOOLEAN | NOT NULL, DEFAULT FALSE | 是否已验证 |
| attempts | INT | NOT NULL, DEFAULT 0 | 尝试次数 |
| created_at | DATETIME | NOT NULL | 创建时间 |

## 表关系

### 一对一关系
- `users.teacher_profile_id` -> `teachers_profile.id`
- `users.wallet_id` -> `wallets.id`

### 一对多/多对一关系
- `appointments.user_id` -> `users.id` (学生)
- `appointments.teacher_user_id` -> `users.id` (教师)
- `orders.user_id` -> `users.id`
- `plan.user_id` -> `users.id`
- `comments.from_user_id` -> `users.id`
- `comments.to_user_id` -> `users.id`
- `rewards.user_id` -> `users.id`
- `chat_dialogue_participant.participant_user_id` -> `users.id`
- `chat_messages.sender_id` -> `users.id`

### 多对多关系（通过中间表实现）
- `users` <-> `chat_dialogue`（通过chat_dialogue_participant表）
- `chat_dialogue.initiator_id` -> `users.id`（如果添加发起者字段）

## 注意事项
1. `orders` 表的结构似乎与家教系统的业务逻辑不太匹配（包含bookname、count字段），可能需要确认是否为遗留代码或需要修改。
2. `plan` 表包含content、deadline等字段，可能用于学习计划或教师计划。
3. 聊天系统通过 `chat_dialogue`、`chat_dialogue_participant`、`chat_messages` 三表实现，支持一对一和群聊。
4. 用户认证支持多种登录方式（邮箱、Google、微信、QQ）。
5. 钱包系统支持余额和积分两种账户。

## 数据库约束
- 所有外键关系都有相应的级联删除或限制设置（需根据业务逻辑确定）
- 唯一约束在关键字段上设置（如邮箱、用户名等）
- 时间戳字段使用 `DATETIME` 类型
