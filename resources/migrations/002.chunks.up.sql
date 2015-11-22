--
-- Table structure for table "chunks"
--

CREATE TABLE "chunks" (
  "title" text NOT NULL,
  "url" text NOT NULL,
  "description_full" text,
  "description_summary" text,
  "created_on" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id" SERIAL,
  PRIMARY KEY ("id")
);
