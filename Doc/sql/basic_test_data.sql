-- 基本测试数据插入脚本
-- 用于快速设置测试环境的基本数据
-- 修复：使用正确的枚举值 PRIVATE 而不是 ONE_ON_ONE

-- 插入测试用户
INSERT INTO users (username, email, password, role, phone_number, created_at)
VALUES
    ('test_student', 'student@test.com', '$2a$10$dummyhashedpassword', 'STUDENT', '13800000000', NOW()),
    ('test_teacher', 'teacher@test.com', '$2a$10$dummyhashedpassword', 'TEACHER', '13800000001', NOW());

-- 插入钱包数据，为用户分配钱包
INSERT INTO wallets (balance, created_at, updated_at) VALUES
(100.00, NOW(), NOW()),
(200.00, NOW(), NOW());

-- 更新用户钱包关联
UPDATE users SET wallet_id = 1 WHERE id = 1;
UPDATE users SET wallet_id = 2 WHERE id = 2;

-- 插入老师配置数据
INSERT INTO teachers_profile (educational_background, taught_grades, taught_subject, taught_courses) VALUES
('数学本科', '高中', 'MATH', '代数，几何'),
('英语硕士', '高中，初中', 'ENGLISH', '口语，听力');

-- 更新老师用户关联
UPDATE users SET teacher_profile_id = 1 WHERE id = 2;

-- 插入聊天对话（修复：使用PRIVATE类型）
INSERT INTO chat_dialogue (dialogue_type, created_at) VALUES
('PRIVATE', NOW()),
('PRIVATE', NOW());

-- 插入聊天对话参与者
INSERT INTO chat_dialogue_participant (dialogue_id, participant_user_id, joined_at) VALUES
(1, 1, NOW()),
(1, 2, NOW()),
(2, 1, NOW()),
(2, 2, NOW());

-- 插入聊天消息
INSERT INTO chat_message (dialogue_id, user_id, content, message_type, sent_at) VALUES
(1, 1, '老师，您好，我想问一下数学问题。', 'TEXT', NOW()),
(1, 2, '你好，有什么问题可以问我。', 'TEXT', NOW()),
(2, 1, '英语单词怎么记住？', 'TEXT', NOW()),
(2, 2, '多读多写，建立联想记忆。', 'TEXT', NOW());
