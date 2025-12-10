curl -X GET http://localhost:8080/api/users/teachers | jq

curl -X POST http://localhost:8080/api/users/teachers \
  -H "Content-Type: application/json" \
  -d '{"subject": "MATH"}'


curl -X POST http://localhost:8080/api/users/teachers \
  -H "Content-Type: application/json" \
  -d '{"keyword": "wang"}'

curl -X POST http://localhost:8080/api/users/teachers \
  -H "Content-Type: application/json" \
  -d '{
    "keyword": "wang",
    "subject": "MATH"
  }'
