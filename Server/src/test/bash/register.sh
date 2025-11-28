# Verification code request
curl -s -X POST http://localhost:8080/api/auth/send-verification-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "1272369577@qq.com"
    }'
    
# Verify the code
curl -X POST http://localhost:8080/api/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{
    "email": "1272369577@qq.com",
    "code": "140385"
    }'

# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Nulla",
    "email": "1272369577@qq.com",
    "password": "password123"
  }' | jq