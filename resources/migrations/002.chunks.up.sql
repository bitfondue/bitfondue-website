--
-- Table structure for table "chunks"
--

CREATE TABLE "chunks" (
  "title" text NOT NULL,
  "created_on" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id" SERIAL,
  PRIMARY KEY ("id")
);
