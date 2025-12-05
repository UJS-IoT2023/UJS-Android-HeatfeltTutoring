# Verification code request
curl -s -X POST http://localhost:8080/api/auth/send-verification-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "2519994926@qq.com"
    }'
    
# Verify the code
curl -X POST http://localhost:8080/api/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{
    "email": "2519994926@qq.com",
    "code": "314364"
    }'

# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Mizimi",
    "email": "2519994926@qq.com",
    "password": "password123"
  }' | jq
  