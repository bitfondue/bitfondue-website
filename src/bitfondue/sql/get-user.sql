SELECT username,email,firstname,lastname,password
FROM users
WHERE username = :username
LIMIT 1
