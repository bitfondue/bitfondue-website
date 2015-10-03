-- name: get-user
-- retrieve a user given a username
SELECT username,email,firstname,lastname,password
FROM users
WHERE username = :username
LIMIT 1

-- name: insert-user<!
-- insert a new user
INSERT INTO users
(username,email,password)
VALUES (:username,:email,:password);
