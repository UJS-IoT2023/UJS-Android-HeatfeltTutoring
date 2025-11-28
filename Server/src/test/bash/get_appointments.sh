curl -X GET http://localhost:8080/api/appointments/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzY0MjA3NTYyLCJleHAiOjE3NjQ4MTIzNjJ9.foacp0ycfF7PIleyWc7OdtePnh-0UaBD-Ha6NoAgAWcHs0N-7k4f0SJnse29CE9exxFRPTT9EC7t1yvc0MBadw" |
  jq