-- Insert test users for WebSocket testing
INSERT INTO users (username, email, password, role, phone_number, created_at)
VALUES
    ('test_student', 'student@test.com', '$2a$10$dummyhashedpassword', 'STUDENT', '13800000000', NOW()),
    ('test_teacher', 'teacher@test.com', '$2a$10$dummyhashedpassword', 'TEACHER', '13800000001', NOW());
