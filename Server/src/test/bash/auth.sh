# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "1272369577@qq.com",
    "password": "password123"
  }' | jq
