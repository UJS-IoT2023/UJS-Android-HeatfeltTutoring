# Get user plan
curl -X GET http://localhost:8080/api/plans/user/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicGhvbmVOdW1iZXIiOiIxMzgwMDEzODAwMCIsImlhdCI6MTc2MzUzMTM5OCwiZXhwIjoxNzY0MTM2MTk4fQ.IiUEsmOgikP29bbkXMUZNKvASxyyMb8xR1X7rmjnx8OEUwsRn_tPzhCGO6XhAMylSw0_MH6j4Bx7FE4H6nQ4Mw" |
  jq

curl -X PUT http://localhost:8080/api/plans/2 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicGhvbmVOdW1iZXIiOiIxMzgwMDEzODAwMCIsImlhdCI6MTc2MzUzMTM5OCwiZXhwIjoxNzY0MTM2MTk4fQ.IiUEsmOgikP29bbkXMUZNKvASxyyMb8xR1X7rmjnx8OEUwsRn_tPzhCGO6XhAMylSw0_MH6j4Bx7FE4H6nQ4Mw" \
  -d '{
    "content": "date shanghai teacher",
    "deadline": "2025-11-22T21:13:12",
    "id": 2,
    "isCompleted": true,
    "userId": 0
  }'