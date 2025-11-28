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
