curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 7,
    "teacherUserId": 3,
    "subject": "数学",
    "appointmentDate": "2025-12-15T10:00:00"
  }'

