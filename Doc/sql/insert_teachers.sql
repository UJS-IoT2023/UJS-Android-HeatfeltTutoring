-- INSERT INTO teachers
--     (address, educational_background, icon, name, phone_number, sex, taught_grades) 
-- VALUES
--     (null, '本科',null, '李华', '123456789999', '男', '高中'),
--     (null, '本科',null, '雷军', '123456723999', '男', '高中');
-- 
-- 
-- INSERT INTO teachers
--     (address, educational_background, icon, name, phone_number, sex, taught_grades)
-- VALUES
--     ('江苏省镇江市学府路', '博士',null, '郑文医', '123456755666', '女', '大学');


INSERT INTO users (username, email, password, role, phone_number, created_at)
VALUES
    ('teacher_wang', 'wang@example.com', '', 'TEACHER', '13911111111', NOW()),
    ('teacher_zhao', 'zhao@example.com', '', 'TEACHER', '13922222222', NOW()),
    ('teacher_qian', 'qian@example.com', '', 'TEACHER', '13933333333', NOW());

-- ******************************************************
-- 1. 清理现有测试数据 (可选，仅用于全新测试环境)
-- ******************************************************
-- DELETE FROM users WHERE id >= 20;
-- DELETE FROM teachers_profile WHERE id >= 20;
-- DELETE FROM wallets WHERE id >= 20;
-- -- 调整序列 (如果使用序列生成ID)
-- SELECT setval('users_id_seq', 20, false);
-- SELECT setval('teachers_profile_id_seq', 20, false);
-- SELECT setval('wallets_id_seq', 20, false);


-- ******************************************************
-- 2. 钱包 (Wallets) 表数据插入 (20 条)
-- ******************************************************
-- ID范围: 20 - 39 (作为 users.wallet_id)
INSERT INTO wallets (id, balance, points) VALUES
(20, 100.00, 500), (21, 50.00, 200), (22, 200.00, 1000), (23, 0.00, 0), (24, 300.00, 1500),
(25, 450.00, 2000), (26, 120.00, 600), (27, 80.00, 400), (28, 550.00, 2500), (29, 10.00, 50),
(30, 180.00, 900), (31, 230.00, 1100), (32, 70.00, 350), (33, 350.00, 1800), (34, 150.00, 750),
(35, 275.00, 1300), (36, 95.00, 475), (37, 420.00, 2100), (38, 5.00, 25), (39, 110.00, 550);


-- ******************************************************
-- 3. 教师档案 (Teachers_Profile) 表数据插入 (20 条)
-- ******************************************************
-- ID范围: 20 - 39 (作为 users.teacher_profile_id)
INSERT INTO teachers_profile (id, educational_background, taught_courses, taught_grades, taught_subject) VALUES
(20, '博士', '高等数学, 线性代数', '大学, 研究生', 'MATH'),
(21, '硕士研究生', '中考/高考英语', '初中, 高中', 'ENGLISH'),
(22, '本科', '基础化学', '初中', 'CHEMISTRY'),
(23, '博士', '量子物理', '高中, 大学', 'PHYSICS'),
(24, '硕士研究生', '中国古代史', '高中', 'HISTORY'),
(25, '本科', 'C++程序设计, 算法', '大学', 'COMPUTER_SCIENCE'),
(26, '硕士研究生', '现代汉语, 写作', '初中, 高中, 大学', 'CHINESE'),
(27, '博士', '微积分, 概率论', '大学', 'MATH'),
(28, '本科', '商务英语', '高中, 大学', 'ENGLISH'),
(29, '硕士研究生', '有机化学', '高中, 大学', 'CHEMISTRY'),
(30, '博士', '电磁学', '高中', 'PHYSICS'),
(31, '本科', '世界近代史', '高中', 'HISTORY'),
(32, '硕士研究生', 'Python, 数据库', '大学', 'COMPUTER_SCIENCE'),
(33, '博士', '诗词鉴赏, 文言文', '初中, 高中', 'CHINESE'),
(34, '硕士研究生', '代数几何', '大学, 研究生', 'MATH'),
(35, '本科', '新概念英语', '初中', 'ENGLISH'),
(36, '博士', '物理化学', '大学', 'CHEMISTRY'),
(37, '硕士研究生', '光学, 热学', '高中', 'PHYSICS'),
(38, '本科', '世界现代史', '初中, 高中', 'HISTORY'),
(39, '硕士研究生', '操作系统, 网络', '大学', 'COMPUTER_SCIENCE');


-- ******************************************************
-- 4. 用户 (Users) 表数据插入 (20 位教师)
-- ******************************************************
-- ID范围: 20 - 39
INSERT INTO users (id, created_at, email, password, phone_number, real_name, role, username, teacher_profile_id, wallet_id, gender, address, avatar_url) VALUES
-- 数学/物理/化学
(20, NOW() - INTERVAL '40 days', 'math_t_1@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13010000020', '孙老师', 'TEACHER', 'sun_math_phd', 20, 20, '男', '北京市海淀区', NULL),
(21, NOW() - INTERVAL '35 days', 'english_t_1@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13120000021', '周老师', 'TEACHER', 'zhou_eng_master', 21, 21, '女', '广州市天河区', NULL),
(22, NOW() - INTERVAL '30 days', 'chem_t_1@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13230000022', '吴老师', 'TEACHER', 'wu_chem_bachelor', 22, 22, '男', '武汉市洪山区', NULL),
(23, NOW() - INTERVAL '28 days', 'physics_t_1@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13340000023', '冯老师', 'TEACHER', 'feng_phys_phd', 23, 23, '女', '成都市武侯区', NULL),
-- 历史/计算机/语文
(24, NOW() - INTERVAL '25 days', 'history_t_1@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13450000024', '陈老师', 'TEACHER', 'chen_his_master', 24, 24, '女', '南京市鼓楼区', NULL),
(25, NOW() - INTERVAL '22 days', 'cs_t_1@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13560000025', '褚老师', 'TEACHER', 'chu_cs_bachelor', 25, 25, '男', '杭州市西湖区', NULL),
(26, NOW() - INTERVAL '20 days', 'chinese_t_1@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13670000026', '卫老师', 'TEACHER', 'wei_chi_master', 26, 26, '女', '西安市雁塔区', NULL),
-- 数学/英语/化学
(27, NOW() - INTERVAL '18 days', 'math_t_2@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13780000027', '蒋老师', 'TEACHER', 'jiang_math_phd', 27, 27, '男', '重庆市沙坪坝区', NULL),
(28, NOW() - INTERVAL '15 days', 'english_t_2@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13890000028', '沈老师', 'TEACHER', 'shen_eng_bachelor', 28, 28, '女', '天津市南开区', NULL),
(29, NOW() - INTERVAL '13 days', 'chem_t_2@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13900000029', '韩老师', 'TEACHER', 'han_chem_master', 29, 29, '男', '深圳市南山区', NULL),
-- 物理/历史/计算机
(30, NOW() - INTERVAL '11 days', 'physics_t_2@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13010000030', '杨老师', 'TEACHER', 'yang_phys_phd', 30, 30, '女', '青岛市市南区', NULL),
(31, NOW() - INTERVAL '9 days', 'history_t_2@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13120000031', '朱老师', 'TEACHER', 'zhu_his_bachelor', 31, 31, '男', '苏州市工业园区', NULL),
(32, NOW() - INTERVAL '7 days', 'cs_t_2@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13230000032', '秦老师', 'TEACHER', 'qin_cs_master', 32, 32, '女', '郑州市金水区', NULL),
-- 语文/数学/英语
(33, NOW() - INTERVAL '5 days', 'chinese_t_2@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13340000033', '许老师', 'TEACHER', 'xu_chi_phd', 33, 33, '男', '长沙市岳麓区', NULL),
(34, NOW() - INTERVAL '4 days', 'math_t_3@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13450000034', '何老师', 'TEACHER', 'he_math_master', 34, 34, '女', '大连市沙河口区', NULL),
(35, NOW() - INTERVAL '3 days', 'english_t_3@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13560000035', '吕老师', 'TEACHER', 'lv_eng_bachelor', 35, 35, '男', '哈尔滨市南岗区', NULL),
-- 化学/物理/历史
(36, NOW() - INTERVAL '2 days', 'chem_t_3@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13670000036', '骆老师', 'TEACHER', 'luo_chem_phd', 36, 36, '女', '福州市鼓楼区', NULL),
(37, NOW() - INTERVAL '1 days', 'physics_t_3@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13780000037', '马老师', 'TEACHER', 'ma_phys_master', 37, 37, '男', '济南市历下区', NULL),
(38, NOW(), 'history_t_3@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13890000038', '高老师', 'TEACHER', 'gao_his_bachelor', 38, 38, '女', '合肥市蜀山区', NULL),
-- 计算机 (额外一条)
(39, NOW(), 'cs_t_3@edu.cn', '$2a$10$HASHED_PASS_TEACHER', '13900000039', '魏老师', 'TEACHER', 'wei_cs_master', 39, 39, '男', '昆明市五华区', NULL);

-- COMMIT;

-- 确保在正确的数据库中执行
-- \c heartfelt_tutoring ;

-- 批量插入 20 位教师用户 (ID 20 到 39 的资料)
INSERT INTO users (
    username,
    email,
    password, 
    role,
    real_name,
    phone_number,
    address,
    sex,
    gender,
    created_at,
    teacher_profile_id,
    avatar_url,
    qq_id,
    qq_openid,
    wechat_id,
    wechat_openid,
    google_id,
    wallet_id 
) VALUES 
-- Profile ID 20 (数学)
('teacher_20', 'teacher_20@example.com', NULL, 'TEACHER', '陈老师', '13800000020', '江苏省南京市玄武区中山路', 'm', 'MALE', NOW(), 20, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 21 (英语)
('teacher_21', 'teacher_21@example.com', NULL, 'TEACHER', '林老师', '13800000021', '江苏省南京市鼓楼区汉中路', 'f', 'FEMALE', NOW(), 21, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 22 (化学)
('teacher_22', 'teacher_22@example.com', NULL, 'TEACHER', '张老师', '13800000022', '江苏省苏州市工业园区星湖街', 'm', 'MALE', NOW(), 22, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 23 (物理)
('teacher_23', 'teacher_23@example.com', NULL, 'TEACHER', '王老师', '13800000023', '江苏省苏州市姑苏区景德路', 'f', 'FEMALE', NOW(), 23, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 24 (历史)
('teacher_24', 'teacher_24@example.com', NULL, 'TEACHER', '刘老师', '13800000024', '江苏省无锡市滨湖区太湖大道', 'm', 'MALE', NOW(), 24, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 25 (计算机)
('teacher_25', 'teacher_25@example.com', NULL, 'TEACHER', '杨老师', '13800000025', '江苏省无锡市锡山区东亭街道', 'f', 'FEMALE', NOW(), 25, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 26 (语文)
('teacher_26', 'teacher_26@example.com', NULL, 'TEACHER', '黄老师', '13800000026', '江苏省常州市天宁区延陵中路', 'm', 'MALE', NOW(), 26, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 27 (数学)
('teacher_27', 'teacher_27@example.com', NULL, 'TEACHER', '周老师', '13800000027', '江苏省常州市钟楼区怀德中路', 'f', 'FEMALE', NOW(), 27, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 28 (英语)
('teacher_28', 'teacher_28@example.com', NULL, 'TEACHER', '吴老师', '13800000028', '江苏省镇江市京口区解放路', 'm', 'MALE', NOW(), 28, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 29 (化学)
('teacher_29', 'teacher_29@example.com', NULL, 'TEACHER', '许老师', '13800000029', '江苏省镇江市润州区南徐大道', 'f', 'FEMALE', NOW(), 29, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 30 (物理)
('teacher_30', 'teacher_30@example.com', NULL, 'TEACHER', '孙老师', '13800000030', '江苏省扬州市邗江区文昌中路', 'm', 'MALE', NOW(), 30, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 31 (历史)
('teacher_31', 'teacher_31@example.com', NULL, 'TEACHER', '侯老师', '13800000031', '江苏省扬州市广陵区文汇西路', 'f', 'FEMALE', NOW(), 31, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 32 (计算机)
('teacher_32', 'teacher_32@example.com', NULL, 'TEACHER', '朱老师', '13800000032', '江苏省泰州市海陵区鼓楼南路', 'm', 'MALE', NOW(), 32, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 33 (语文)
('teacher_33', 'teacher_33@example.com', NULL, 'TEACHER', '郭老师', '13800000033', '江苏省泰州市高港区永安洲镇', 'f', 'FEMALE', NOW(), 33, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 34 (数学)
('teacher_34', 'teacher_34@example.com', NULL, 'TEACHER', '高老师', '13800000034', '江苏省淮安市清江浦区淮海东路', 'm', 'MALE', NOW(), 34, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 35 (英语)
('teacher_35', 'teacher_35@example.com', NULL, 'TEACHER', '魏老师', '13800000035', '江苏省淮安市淮阴区长江东路', 'f', 'FEMALE', NOW(), 35, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 36 (化学)
('teacher_36', 'teacher_36@example.com', NULL, 'TEACHER', '郑老师', '13800000036', '江苏省盐城市亭湖区青年路', 'm', 'MALE', NOW(), 36, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 37 (物理)
('teacher_37', 'teacher_37@example.com', NULL, 'TEACHER', '谢老师', '13800000037', '江苏省盐城市盐都区新都路', 'f', 'FEMALE', NOW(), 37, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 38 (历史)
('teacher_38', 'teacher_38@example.com', NULL, 'TEACHER', '韩老师', '13800000038', '江苏省连云港市海州区通灌路', 'm', 'MALE', NOW(), 38, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
-- Profile ID 39 (计算机)
('teacher_39', 'teacher_39@example.com', NULL, 'TEACHER', '方老师', '13800000039', '江苏省连云港市连云区连云街道', 'f', 'FEMALE', NOW(), 39, NULL, NULL, NULL, NULL, NULL, NULL, NULL);