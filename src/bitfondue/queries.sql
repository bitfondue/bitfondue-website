-- name: get-user
SELECT username,email,firstname,lastname,password
FROM users
WHERE username = :username
LIMIT 1

-- name: insert-user
INSERT INTO users
(username,email,password)
VALUES (:username,:email,:password);
