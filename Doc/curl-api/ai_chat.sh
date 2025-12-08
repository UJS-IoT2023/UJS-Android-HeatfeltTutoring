curl -X POST http://localhost:8080/api/ai/generate \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, generate a test response", "conversationId": 1}'

curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello",
    "conversationId":1
  }'

# Response:
# data:Hello

# data:!

# data: How

# data: can

# data: I

# data: assist

# data: you

# data: today

# data:?

# data: 😊