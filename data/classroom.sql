/*
 Navicat Premium Dump SQL

 Source Server         : 43.134.166.185_5432
 Source Server Type    : PostgreSQL
 Source Server Version : 170002 (170002)
 Source Host           : 43.134.166.185:5432
 Source Catalog        : scheduler
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170002 (170002)
 File Encoding         : 65001

 Date: 05/01/2025 18:36:15
*/


-- ----------------------------
-- Table structure for classroom
-- ----------------------------
DROP TABLE IF EXISTS "public"."classroom";
CREATE TABLE "public"."classroom"
(
    "id"       int8 NOT NULL DEFAULT nextval('classroom_id_seq'::regclass),
    "name"     varchar(255) COLLATE "pg_catalog"."default",
    "size"     int4,
    "software" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of classroom
-- ----------------------------
INSERT INTO "public"."classroom"
VALUES (1, 'Room1', 41, NULL);
INSERT INTO "public"."classroom"
VALUES (2, 'Room2', 133, NULL);
INSERT INTO "public"."classroom"
VALUES (3, 'Room3', 70, NULL);
INSERT INTO "public"."classroom"
VALUES (4, 'Room4', 110, NULL);
INSERT INTO "public"."classroom"
VALUES (5, 'Room5', 50, NULL);
INSERT INTO "public"."classroom"
VALUES (6, 'Room6', 40, NULL);
INSERT INTO "public"."classroom"
VALUES (7, 'Room7', 60, NULL);
INSERT INTO "public"."classroom"
VALUES (8, 'Room8', 126, NULL);
INSERT INTO "public"."classroom"
VALUES (9, 'Room9', 36, NULL);
INSERT INTO "public"."classroom"
VALUES (10, 'Room10', 110, NULL);
INSERT INTO "public"."classroom"
VALUES (11, 'Room11', 40, NULL);
INSERT INTO "public"."classroom"
VALUES (12, 'Room12', 40, NULL);
INSERT INTO "public"."classroom"
VALUES (13, 'Room13', 32, NULL);
INSERT INTO "public"."classroom"
VALUES (14, 'Room14', 100, NULL);
INSERT INTO "public"."classroom"
VALUES (15, 'Room15', 50, NULL);
INSERT INTO "public"."classroom"
VALUES (16, 'Room16', 40, NULL);

-- ----------------------------
-- Primary Key structure for table classroom
-- ----------------------------
ALTER TABLE "public"."classroom"
    ADD CONSTRAINT "classroom_pkey" PRIMARY KEY ("id");
