-- ----------------------------
-- Table structure for app_user
-- ----------------------------
CREATE TABLE "public"."app_user" (
  "email" varchar(255) PRIMARY KEY NOT NULL,
  "active" bool NOT NULL,
  "created" timestamp(6),
  "last_login" timestamp(6),
  "name" varchar(255),
  "phone" varchar(255),
  "role" varchar(255)
);

-- ----------------------------
-- Records of app_user
-- ----------------------------
INSERT INTO "public"."app_user" VALUES ('rayxue0@outlook.com', 'f', '2025-02-11 14:03:05.62', '2025-02-11 14:03:05.62', 'Ray Xue', NULL, NULL);
INSERT INTO "public"."app_user" VALUES ('e1221830@u.nus.edu', 't', '2025-02-09 22:29:11.287', '2025-02-11 15:45:16.259', 'Xue Chenrui', NULL, NULL);
INSERT INTO "public"."app_user" VALUES ('darrylng@nus.edu.sg', 't', '2025-01-20 10:31:45.233', '2025-01-20 19:24:53.442', 'Darryl Ng', NULL, NULL);
INSERT INTO "public"."app_user" VALUES ('gireesh@nus.edu.sg', 't', NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."app_user" VALUES ('kennytkn@nus.edu.sg', 't', NULL, NULL, NULL, NULL, NULL);
INSERT INTO "public"."app_user" VALUES ('e1275110@u.nus.edu', 't', '2025-02-11 18:35:23.988', '2025-02-11 18:35:23.988', 'Li Jialiang', NULL, NULL);
INSERT INTO "public"."app_user" VALUES ('e1221832@u.nus.edu', 't', '2025-02-09 21:14:44.063', '2025-02-11 21:09:14.041', 'Wu Jiayi', NULL, NULL);
