curl -X POST http://localhost:8080/api/chat/dialogue \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "creatorId": 5,
    "participantIds": [4],
    "title": "Study Group Discussion"
  }'

curl -X GET http://localhost:8080/api/chat/dialogues/5 \
  -H "Content-Type: application/json"