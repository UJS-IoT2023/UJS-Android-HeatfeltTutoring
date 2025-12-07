# Login with email
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "loginType": "EMAIL",
    "identifier": "1272369577@qq.com",
    "password": "password123"
  }' | jq

# Login with username
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "loginType": "USERNAME",
    "identifier": "Nulla",
    "password": "password123"
  }' | jq