# Get all user
curl -X GET 'http://localhost:8080/api/users' \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicGhvbmVOdW1iZXIiOiIxMzgwMDEzODAwMCIsImlhdCI6MTc2MzUzMTM5OCwiZXhwIjoxNzY0MTM2MTk4fQ.IiUEsmOgikP29bbkXMUZNKvASxyyMb8xR1X7rmjnx8OEUwsRn_tPzhCGO6XhAMylSw0_MH6j4Bx7FE4H6nQ4Mw" | jq

# Get user with conditions
curl -X POST 'http://localhost:8080/api/users' \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicGhvbmVOdW1iZXIiOiIxMzgwMDEzODAwMCIsImlhdCI6MTc2MzUzMTM5OCwiZXhwIjoxNzY0MTM2MTk4fQ.IiUEsmOgikP29bbkXMUZNKvASxyyMb8xR1X7rmjnx8OEUwsRn_tPzhCGO6XhAMylSw0_MH6j4Bx7FE4H6nQ4Mw" \
  -d '{
    "userId": 1
  }' | jq

# Get teacher users
curl -X GET 'http://localhost:8080/api/users/teachers' \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicGhvbmVOdW1iZXIiOiIxMzgwMDEzODAwMCIsImlhdCI6MTc2MzUzMTM5OCwiZXhwIjoxNzY0MTM2MTk4fQ.IiUEsmOgikP29bbkXMUZNKvASxyyMb8xR1X7rmjnx8OEUwsRn_tPzhCGO6XhAMylSw0_MH6j4Bx7FE4H6nQ4Mw" | jq
     
# Get user by id
curl -X GET 'http://localhost:8080/api/users/1' | jq