SELECT * FROM teachers;

SELECT username, address, icon, phone_number FROM users;

SELECT
    a.id as id,
    a.subject as subject,
    a.appointment_date as appointment_date,
    u.username as username,
    t.name as teacherName
FROM appointments a
         LEFT JOIN users u ON a.user_id = u.id
         LEFT JOIN teachers t ON a.teacher_id = t.id
WHERE a.user_id = 1