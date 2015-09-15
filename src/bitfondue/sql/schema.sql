--
-- Table structure for table "users"
--

-- DROP TABLE IF EXISTS "users";
CREATE TABLE "users" (
  "username" varchar(255) NOT NULL,
  "email" varchar(255) NOT NULL,
  "password" varchar(100) NOT NULL,
  "lastname" varchar(255) DEFAULT NULL,
  "firstname" varchar(255) DEFAULT NULL,
  "updated_on" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "created_on" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id" SERIAL,
  PRIMARY KEY ("id")
);
