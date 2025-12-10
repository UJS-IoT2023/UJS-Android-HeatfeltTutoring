curl -X POST http://localhost:8080/api/chat/dialogue \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "participantIds": [5, 6],
    "title": "Study Group Discussion"
  }'

curl -X GET http://localhost:8080/api/chat/dialogues/6 \
  -H "Content-Type: application/json"

curl -X GET http://localhost:8080/api/chat/dialogue/1/messages

curl -X GET http://localhost:8080/api/chat
