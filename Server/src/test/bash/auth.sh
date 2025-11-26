# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "13915830566",
    "password": "123"
  }'


# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Nulla",
    "email": "1272369577@qq.com",
    "password": "password123"
  }'
