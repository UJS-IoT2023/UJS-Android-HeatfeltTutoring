-- 完整测试数据 - 基于数据库表结构文档生成
-- 包含所有12个表的测试数据，确保关系完整性

-- 插入钱包数据（需要先创建，因为users表引用）
-- INSERT INTO wallets (balance, points) VALUES
-- (1500.50, 200.00),
-- (800.25, 150.00),
-- (2200.00, 350.00),
-- (1200.75, 180.00),
-- (500.00, 50.00),
-- (3000.00, 500.00);

-- 插入教师资料数据
INSERT INTO teachers_profile (educational_background, taught_grades, taught_subject, taught_courses) VALUES
('本科教育，数学专业', '高中，中专', 'MATH', '高等数学，线性代数'),
('硕士教育，英语专业', '初中，高中', 'ENGLISH', '英语口语，英语写作'),
('物理学硕士', '高中', 'PHYSICS', '力学，电学');

-- 插入用户数据
INSERT INTO users (username, email, password, role, google_id, wechat_openid, qq_openid,
                   teacher_profile_id, wallet_id, phone_number, avatar_url, real_name,
                   gender, wechat_id, qq_id, address, created_at) VALUES
('student_wang', 'wangxiaoming@test.com', '$2a$10$dummyhash123', 'STUDENT', NULL, 'wx123456789', NULL,
  NULL, 1, '13800138001', 'uploads/avatars-student_wang.jpg', '王小明', '男',
  'wx_wangxm', NULL, '北京市海淀区', '2024-01-15 08:30:00'),
('student_li', 'liling@test.com', '$2a$10$dummyhash456', 'STUDENT', NULL, NULL, 'qq987654321',
  NULL, 2, '13800138002', 'uploads/avatars-student_li.jpg', '李玲', '女',
  NULL, 'qq_liling', '上海市浦东新区', '2024-02-20 14:20:00'),
('teacher_zhao', 'zhaoteacher@test.com', '$2a$10$dummyhash789', 'TEACHER', 'google12345', NULL, NULL,
  1, 3, '13800138003', 'uploads/avatars-teacher_zhao.jpg', '赵老师', '女',
  'wx_zhaolao', NULL, '北京市西城区', '2024-01-10 09:00:00'),
('teacher_sun', 'sunteacher@test.com', '$2a$10$dummyhash101', 'TEACHER', NULL, 'wx987654321', NULL,
  2, 4, '13800138004', 'uploads/avatars-teacher_sun.jpg', '孙老师', '男',
  'wx_sunlao', NULL, '上海市静安区', '2024-02-05 10:15:00'),
('teacher_wu', 'wuteacher@test.com', '$2a$10$dummyhash202', 'TEACHER', NULL, NULL, NULL,
  3, 5, '13800138005', 'uploads/avatars-teacher_wu.jpg', '吴老师', '男',
  NULL, NULL, '深圳市南山区', '2024-03-01 11:30:00'),
('parent_zhang', 'zhangparent@test.com', '$2a$10$dummyhash303', 'PARENT', NULL, NULL, NULL,
  NULL, 6, '13800138006', 'uploads/avatars-parent_zhang.jpg', '张女士', '女',
  NULL, 'qq_zhangma', '广州市天河区', '2024-01-25 15:45:00'),
('admin_system', 'admin@system.com', '$2a$10$dummyhash404', 'ADMIN', NULL, NULL, NULL,
  NULL, NULL, '13800138000', 'uploads/avatars-admin.jpg', '系统管理员', '未知',
  NULL, NULL, '系统内部', '2024-01-01 00:00:00');

-- 插入预约数据
INSERT INTO appointments (user_id, teacher_user_id, subject, appointment_date) VALUES
(1, 3, '数学辅导', '2024-12-20 14:00:00'),
(1, 4, '英语对话', '2024-12-22 16:00:00'),
(2, 3, '数学课后辅导', '2024-12-25 15:00:00'),
(2, 4, '英语听力训练', '2024-12-28 10:00:00'),
(1, 5, '物理实验辅导', '2024-12-30 13:30:00');

-- 插入计划数据
INSERT INTO plan (user_id, content, deadline, is_completed) VALUES
(1, '完成高中数学所有章节学习', '2024-12-31 23:59:59', false),
(1, '准备期中考试数学复习', '2024-11-30 23:59:59', true),
(2, '提高英语口语和听力', '2025-01-31 23:59:59', false),
(3, '制定下学期教学计划', '2024-12-20 18:00:00', false),
(4, '准备英语教学资料', '2024-12-15 17:00:00', true),
(5, '物理实验课前准备', '2024-12-29 12:00:00', false);

-- 插入订单数据（根据当前表结构，可能需要根据业务需求调整）
INSERT INTO orders (user_id, bookname, count, price, state) VALUES
(1, '高中数学教材', 1, 45.80, 'COMPLETED'),
(1, '英语词汇书', 2, 38.50, 'PENDING'),
(2, '英语语法教材', 1, 52.00, 'PAID'),
(3, '教学参考资料', 5, 120.00, 'COMPLETED'),
(6, '学习资料包', 3, 89.90, 'SHIPPED');

-- 插入评论数据
INSERT INTO comments (from_user_id, to_user_id, content, created_at) VALUES
(1, 3, '赵老师讲解非常清楚，很有耐心，我数学成绩提高了很多！', '2024-11-15 16:30:00'),
(1, 4, '孙老师的英语教学方法很实用，听力训练很有帮助', '2024-11-20 14:20:00'),
(2, 3, '数学思维训练的很好，推荐给其他同学', '2024-11-18 09:15:00'),
(2, 4, '英语发音纠正很准确，进步明显', '2024-11-22 11:45:00'),
(1, 5, '吴老师的物理实验讲解深入浅出，很有趣', '2024-11-25 13:30:00'),
(6, 3, '孩子很喜欢赵老师的数学课', '2024-11-16 17:00:00');

-- 插入奖励数据
INSERT INTO rewards (user_id, account) VALUES
(1, 50.00),
(1, 30.00),
(2, 45.00),
(3, 80.00),
(4, 60.00),
(5, 75.00);

-- 插入聊天对话数据（一对一聊天，类型: PRIVATE）
INSERT INTO chat_dialogue (dialogue_type, title, created_at, last_message_content, updated_at) VALUES
('PRIVATE', '王小明 - 赵老师', '2024-11-10 15:00:00', '好的，明天见', '2024-12-01 14:30:00'),
('PRIVATE', '李玲 - 孙老师', '2024-11-12 16:20:00', '谢谢指导', '2024-11-30 10:45:00'),
('PRIVATE', '王小明 - 吴老师', '2024-11-25 12:00:00', '明白了', '2024-11-28 15:20:00'),
('PRIVATE', '李玲 - 赵老师', '2024-11-14 13:15:00', '好的', '2024-11-26 09:30:00');

-- 更新chat_dialogue的last_message_content为更真实的中文
UPDATE chat_dialogue SET last_message_content = '老师，明天下午的数学课还预约吗？' WHERE id = 1;
UPDATE chat_dialogue SET last_message_content = '孙老师，这道题怎么做？' WHERE id = 2;
UPDATE chat_dialogue SET last_message_content = '好的，实验要注意安全。' WHERE id = 3;
UPDATE chat_dialogue SET last_message_content = '谢谢赵老师的讲解。' WHERE id = 4;

-- 插入聊天对话参与者数据
INSERT INTO chat_dialogue_participant (dialogue_id, participant_user_id, join_at) VALUES
(1, 1, '2024-11-10 15:00:00'),  -- 王小明
(1, 3, '2024-11-10 15:00:00'),  -- 赵老师
(2, 2, '2024-11-12 16:20:00'),  -- 李玲
(2, 4, '2024-11-12 16:20:00'),  -- 孙老师
(3, 1, '2024-11-25 12:00:00'),  -- 王小明
(3, 5, '2024-11-25 12:00:00'),  -- 吴老师
(4, 2, '2024-11-14 13:15:00'),  -- 李玲
(4, 3, '2024-11-14 13:15:00');  -- 赵老师

-- 插入聊天消息数据
INSERT INTO chat_messages (dialogue_id, sender_id, content, created_at) VALUES
-- 对话1: 王小明与赵老师
(1, 1, '赵老师，请问下周可以增加一次数学课吗？', '2024-11-20 16:00:00'),
(1, 3, '可以啊，周三下午怎么样？', '2024-11-20 16:02:00'),
(1, 1, '好的，那周三下午3点', '2024-11-20 16:03:00'),
(1, 3, '没问题，到时候见', '2024-11-20 16:05:00'),
-- 对话2: 李玲与孙老师
(2, 2, '孙老师，我发音总是不准怎么办？', '2024-11-22 14:00:00'),
(2, 4, '多听多练习，发音肌肉需要训练', '2024-11-22 14:03:00'),
(2, 2, '明白了，我会多练习的', '2024-11-22 14:05:00'),
-- 对话3: 王小明与吴老师
(3, 1, '吴老师，物理实验要注意什么？', '2024-11-28 13:00:00'),
(3, 5, '注意安全第一，不要做危险操作', '2024-11-28 13:02:00'),
(3, 1, '好的，谢谢老师', '2024-11-28 13:03:00'),
-- 对话4: 李玲与赵老师
(4, 2, '赵老师，这道数学题怎么证明？', '2024-11-26 15:00:00'),
(4, 3, '可以用反证法来证明...', '2024-11-26 15:05:00');

-- 插入邮箱验证码数据
INSERT INTO email_verification_codes (email, code, expires_at, verified, attempts, created_at) VALUES
('wangxiaoming@test.com', '123456', '2024-12-15 18:30:00', false, 0, '2024-12-14 16:30:00'),
('liling@test.com', '789012', '2024-12-16 10:20:00', false, 0, '2024-12-15 14:20:00'),
('newstudent@test.com', '345678', '2024-12-17 12:00:00', true, 1, '2024-12-15 20:00:00'),
('newteacher@test.com', '901234', '2024-12-18 15:45:00', false, 2, '2024-12-16 08:00:00');

-- 插入群聊对话示例（GROUP类型）
INSERT INTO chat_dialogue (dialogue_type, title, created_at, last_message_content, updated_at) VALUES
('GROUP', '高中数学学习小组', '2024-11-01 19:00:00', '大家明天继续加油！', '2024-12-01 17:30:00'),
('GROUP', '英语角交流群', '2024-11-05 18:00:00', '欢迎新同学加入', '2024-11-29 20:15:00');

-- 插入群聊参与者
INSERT INTO chat_dialogue_participant (dialogue_id, participant_user_id, join_at) VALUES
(5, 1, '2024-11-01 19:00:00'),  -- 王小明加入数学小组
(5, 2, '2024-11-02 10:00:00'),  -- 李玲加入数学小组
(5, 3, '2024-11-01 19:00:00'),  -- 赵老师加入数学小组
(5, 4, '2024-11-03 14:00:00'),  -- 孙老师加入数学小组
(6, 1, '2024-11-06 16:00:00'),  -- 王小明加入英语群
(6, 2, '2024-11-05 18:00:00'),  -- 李玲加入英语群
(6, 4, '2024-11-05 18:00:00'),  -- 孙老师加入英语群
(6, 6, '2024-11-10 19:00:00');  -- 张女士加入英语群

-- 插入群聊消息
INSERT INTO chat_messages (dialogue_id, sender_id, content, created_at) VALUES
-- 数学学习小组
(5, 3, '同学们，这周我们开始学习概率论', '2024-11-10 19:30:00'),
(5, 1, '老师，概率基本概念是什么？', '2024-11-10 19:35:00'),
(5, 3, '概率是对随机事件发生的可能性的度量...', '2024-11-10 19:40:00'),
(5, 2, '老师可以举个例子吗？', '2024-11-11 10:00:00'),
-- 英语角交流群
(6, 4, '大家来练习日常英语对话吧', '2024-11-15 18:30:00'),
(6, 1, 'Hello everyone! How are you today?', '2024-11-15 18:35:00'),
(6, 2, 'I am fine, thank you. And you?', '2024-11-15 18:40:00'),
(6, 4, 'Very well. Let us practice more!', '2024-11-15 18:45:00'),
(6, 6, '大家学习都很认真，家长很欣慰', '2024-11-15 19:00:00');
