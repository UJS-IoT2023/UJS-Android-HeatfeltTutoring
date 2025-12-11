curl -X POST http://localhost:8080/api/comments \
  -H "Content-Type: application/json" \
  -d '{
    "fromUserId": 6,
    "toUserId": 2,
    "content": "fuck you bitch"
  }'

