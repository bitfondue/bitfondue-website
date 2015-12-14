--
-- Table structure for table "chunks"
--

CREATE TABLE "chunks" (
  "title" text NOT NULL,
  "url" text NOT NULL,
  "description_full" text,
  "uid" text NOT NULL,
  "created_on" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "data" jsonb,
  "id" SERIAL,
  PRIMARY KEY ("id")
);
