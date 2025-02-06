-- ----------------------------
-- Table structure for course
-- ----------------------------
CREATE TABLE "public"."course" (
  "id" SERIAL PRIMARY KEY NOT NULL,
  "cert" varchar(255),
  "classroom" varchar(255),
  "cohort" varchar(255),
  "cohort_id" integer,
  "course_code" varchar(255),
  "course_date" timestamp(6),
  "course_name" varchar(255),
  "duration" integer,
  "manager" varchar(255),
  "practice_area" varchar(255),
  "professor_num" integer,
  "run" integer,
  "software" varchar(255),
  "teacher1" varchar(255),
  "teacher2" varchar(255),
  "teacher3" varchar(255),
  "week" varchar(255)
);

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO "public"."course" VALUES (1, 'Grad Cert 1', 'Room9', 'Cohort1', 0, 'CC1', '2025-01-10 00:00:00', 'Course1', 2, 'Lecturer1', 'PA1', 1, 1, '-', 'Lecturer1', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (2, 'Grad Cert 1', 'Room9', 'Cohort1', 0, 'CC1', '2025-01-13 00:00:00', 'Course1', 2, 'Lecturer1', 'PA1', 1, 1, '-', 'Lecturer1', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (3, 'Grad Cert 1', 'Room2', 'Cohort2', 1, 'CC1', '2025-01-13 00:00:00', 'Course2', 2, 'Lecturer2', 'PA1', 3, 1, '-', 'Lecturer2', 'Lecturer12', 'Lecturer22', NULL);
INSERT INTO "public"."course" VALUES (4, 'Grad Cert 1', 'Room2', 'Cohort2', 1, 'CC1', '2025-01-14 00:00:00', 'Course2', 2, 'Lecturer2', 'PA1', 3, 1, '-', 'Lecturer2', 'Lecturer12', 'Lecturer22', NULL);
INSERT INTO "public"."course" VALUES (5, 'Grad Cert 1', 'Room2', 'Cohort3', 2, 'CC1', '2025-01-07 00:00:00', 'Course3', 2, 'Lecturer3', 'PA1', 1, 1, '-', 'Lecturer3', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (6, 'Grad Cert 1', 'Room2', 'Cohort3', 2, 'CC1', '2025-01-08 00:00:00', 'Course3', 2, 'Lecturer3', 'PA1', 1, 1, '-', 'Lecturer3', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (7, 'Grad Cert 1', 'Room8', 'Cohort4', 3, 'CC1', '2025-01-20 00:00:00', 'Course4', 2, 'Lecturer4', 'PA1', 1, 1, '-', 'Lecturer4', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (8, 'Grad Cert 1', 'Room8', 'Cohort4', 3, 'CC1', '2025-01-21 00:00:00', 'Course4', 2, 'Lecturer4', 'PA1', 1, 1, '-', 'Lecturer4', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (9, 'Grad Cert 1', 'Room13', 'Cohort5', 4, 'CC1', '2025-01-06 00:00:00', 'Course5', 2, 'Lecturer5', 'PA1', 2, 1, '-', 'Lecturer5', 'Lecturer15', NULL, NULL);
INSERT INTO "public"."course" VALUES (10, 'Grad Cert 1', 'Room13', 'Cohort5', 4, 'CC1', '2025-01-07 00:00:00', 'Course5', 2, 'Lecturer5', 'PA1', 2, 1, '-', 'Lecturer5', 'Lecturer15', NULL, NULL);
INSERT INTO "public"."course" VALUES (11, 'Grad Cert 1', 'Room3', 'Cohort6', 5, 'CC1', '2025-01-14 00:00:00', 'Course6', 5, 'Lecturer6', 'PA1', 1, 1, '-', 'Lecturer6', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (12, 'Grad Cert 1', 'Room3', 'Cohort6', 5, 'CC1', '2025-01-15 00:00:00', 'Course6', 5, 'Lecturer6', 'PA1', 1, 1, '-', 'Lecturer6', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (13, 'Grad Cert 1', 'Room3', 'Cohort6', 5, 'CC1', '2025-01-16 00:00:00', 'Course6', 5, 'Lecturer6', 'PA1', 1, 1, '-', 'Lecturer6', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (14, 'Grad Cert 1', 'Room3', 'Cohort6', 5, 'CC1', '2025-01-17 00:00:00', 'Course6', 5, 'Lecturer6', 'PA1', 1, 1, '-', 'Lecturer6', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (15, 'Grad Cert 1', 'Room3', 'Cohort6', 5, 'CC1', '2025-01-20 00:00:00', 'Course6', 5, 'Lecturer6', 'PA1', 1, 1, '-', 'Lecturer6', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (16, 'Grad Cert 1', 'Room3', 'Cohort7', 6, 'CC1', '2025-01-21 00:00:00', 'Course7', 5, 'Lecturer7', 'PA1', 1, 1, '-', 'Lecturer7', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (17, 'Grad Cert 1', 'Room3', 'Cohort7', 6, 'CC1', '2025-01-22 00:00:00', 'Course7', 5, 'Lecturer7', 'PA1', 1, 1, '-', 'Lecturer7', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (18, 'Grad Cert 1', 'Room3', 'Cohort7', 6, 'CC1', '2025-01-23 00:00:00', 'Course7', 5, 'Lecturer7', 'PA1', 1, 1, '-', 'Lecturer7', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (19, 'Grad Cert 1', 'Room3', 'Cohort7', 6, 'CC1', '2025-01-24 00:00:00', 'Course7', 5, 'Lecturer7', 'PA1', 1, 1, '-', 'Lecturer7', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (20, 'Grad Cert 1', 'Room3', 'Cohort7', 6, 'CC1', '2025-01-27 00:00:00', 'Course7', 5, 'Lecturer7', 'PA1', 1, 1, '-', 'Lecturer7', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (21, 'Grad Cert 1', 'Room16', 'Cohort8', 7, 'CC1', '2025-01-21 00:00:00', 'Course8', 5, 'Lecturer8', 'PA1', 1, 1, '-', 'Lecturer8', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (22, 'Grad Cert 1', 'Room16', 'Cohort8', 7, 'CC1', '2025-01-22 00:00:00', 'Course8', 5, 'Lecturer8', 'PA1', 1, 1, '-', 'Lecturer8', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (23, 'Grad Cert 1', 'Room16', 'Cohort8', 7, 'CC1', '2025-01-23 00:00:00', 'Course8', 5, 'Lecturer8', 'PA1', 1, 1, '-', 'Lecturer8', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (24, 'Grad Cert 1', 'Room16', 'Cohort8', 7, 'CC1', '2025-01-24 00:00:00', 'Course8', 5, 'Lecturer8', 'PA1', 1, 1, '-', 'Lecturer8', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (25, 'Grad Cert 1', 'Room16', 'Cohort8', 7, 'CC1', '2025-01-27 00:00:00', 'Course8', 5, 'Lecturer8', 'PA1', 1, 1, '-', 'Lecturer8', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (26, 'Grad Cert 1', 'Room16', 'Cohort9', 8, 'CC1', '2025-01-14 00:00:00', 'Course9', 5, 'Lecturer9', 'PA1', 1, 1, '-', 'Lecturer9', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (27, 'Grad Cert 1', 'Room16', 'Cohort9', 8, 'CC1', '2025-01-15 00:00:00', 'Course9', 5, 'Lecturer9', 'PA1', 1, 1, '-', 'Lecturer9', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (28, 'Grad Cert 1', 'Room16', 'Cohort9', 8, 'CC1', '2025-01-16 00:00:00', 'Course9', 5, 'Lecturer9', 'PA1', 1, 1, '-', 'Lecturer9', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (29, 'Grad Cert 1', 'Room16', 'Cohort9', 8, 'CC1', '2025-01-17 00:00:00', 'Course9', 5, 'Lecturer9', 'PA1', 1, 1, '-', 'Lecturer9', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (30, 'Grad Cert 1', 'Room16', 'Cohort9', 8, 'CC1', '2025-01-20 00:00:00', 'Course9', 5, 'Lecturer9', 'PA1', 1, 1, '-', 'Lecturer9', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (31, 'Grad Cert 1', 'Room7', 'Cohort10', 9, 'CC1', '2025-01-10 00:00:00', 'Course10', 5, 'Lecturer10', 'PA1', 1, 1, '-', 'Lecturer10', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (32, 'Grad Cert 1', 'Room7', 'Cohort10', 9, 'CC1', '2025-01-13 00:00:00', 'Course10', 5, 'Lecturer10', 'PA1', 1, 1, '-', 'Lecturer10', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (33, 'Grad Cert 1', 'Room7', 'Cohort10', 9, 'CC1', '2025-01-14 00:00:00', 'Course10', 5, 'Lecturer10', 'PA1', 1, 1, '-', 'Lecturer10', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (34, 'Grad Cert 1', 'Room7', 'Cohort10', 9, 'CC1', '2025-01-15 00:00:00', 'Course10', 5, 'Lecturer10', 'PA1', 1, 1, '-', 'Lecturer10', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (35, 'Grad Cert 1', 'Room7', 'Cohort10', 9, 'CC1', '2025-01-16 00:00:00', 'Course10', 5, 'Lecturer10', 'PA1', 1, 1, '-', 'Lecturer10', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (36, 'Grad Cert 1', 'Room11', 'Cohort11', 10, 'CC2', '2025-01-18 00:00:00', 'Course11', 2, 'Lecturer11', 'PA1', 1, 1, '-', 'Lecturer11', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (37, 'Grad Cert 1', 'Room11', 'Cohort11', 10, 'CC2', '2025-01-24 00:00:00', 'Course11', 2, 'Lecturer11', 'PA1', 1, 1, '-', 'Lecturer11', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (38, 'Grad Cert 1', 'Room8', 'Cohort12', 11, 'CC2', '2025-01-04 00:00:00', 'Course12', 2, 'Lecturer12', 'PA1', 1, 1, '-', 'Lecturer12', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (39, 'Grad Cert 1', 'Room8', 'Cohort12', 11, 'CC2', '2025-01-10 00:00:00', 'Course12', 2, 'Lecturer12', 'PA1', 1, 1, '-', 'Lecturer12', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (40, 'Grad Cert 1', 'Room16', 'Cohort13', 12, 'CC2', '2025-01-03 00:00:00', 'Course13', 2, 'Lecturer13', 'PA1', 1, 1, '-', 'Lecturer13', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (41, 'Grad Cert 1', 'Room16', 'Cohort13', 12, 'CC2', '2025-01-04 00:00:00', 'Course13', 2, 'Lecturer13', 'PA1', 1, 1, '-', 'Lecturer13', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (42, 'Grad Cert 1', 'Room14', 'Cohort14', 13, 'CC2', '2025-01-04 00:00:00', 'Course14', 4, 'Lecturer14', 'PA1', 1, 1, '-', 'Lecturer14', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (43, 'Grad Cert 1', 'Room14', 'Cohort14', 13, 'CC2', '2025-01-10 00:00:00', 'Course14', 4, 'Lecturer14', 'PA1', 1, 1, '-', 'Lecturer14', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (44, 'Grad Cert 1', 'Room14', 'Cohort14', 13, 'CC2', '2025-01-11 00:00:00', 'Course14', 4, 'Lecturer14', 'PA1', 1, 1, '-', 'Lecturer14', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (45, 'Grad Cert 1', 'Room14', 'Cohort14', 13, 'CC2', '2025-01-17 00:00:00', 'Course14', 4, 'Lecturer14', 'PA1', 1, 1, '-', 'Lecturer14', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (46, 'Grad Cert 1', 'Room4', 'Cohort15', 14, 'CC2', '2025-01-10 00:00:00', 'Course15', 4, 'Lecturer15', 'PA1', 1, 1, '-', 'Lecturer15', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (47, 'Grad Cert 1', 'Room4', 'Cohort15', 14, 'CC2', '2025-01-11 00:00:00', 'Course15', 4, 'Lecturer15', 'PA1', 1, 1, '-', 'Lecturer15', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (48, 'Grad Cert 1', 'Room4', 'Cohort15', 14, 'CC2', '2025-01-17 00:00:00', 'Course15', 4, 'Lecturer15', 'PA1', 1, 1, '-', 'Lecturer15', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (49, 'Grad Cert 1', 'Room4', 'Cohort15', 14, 'CC2', '2025-01-18 00:00:00', 'Course15', 4, 'Lecturer15', 'PA1', 1, 1, '-', 'Lecturer15', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (50, 'Grad Cert 1', 'Room4', 'Cohort16', 15, 'CC3', '2025-01-02 00:00:00', 'Course16', 5, 'Lecturer16', 'PA1', 1, 1, '-', 'Lecturer16', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (51, 'Grad Cert 1', 'Room4', 'Cohort16', 15, 'CC3', '2025-01-03 00:00:00', 'Course16', 5, 'Lecturer16', 'PA1', 1, 1, '-', 'Lecturer16', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (52, 'Grad Cert 1', 'Room4', 'Cohort16', 15, 'CC3', '2025-01-06 00:00:00', 'Course16', 5, 'Lecturer16', 'PA1', 1, 1, '-', 'Lecturer16', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (53, 'Grad Cert 1', 'Room4', 'Cohort16', 15, 'CC3', '2025-01-07 00:00:00', 'Course16', 5, 'Lecturer16', 'PA1', 1, 1, '-', 'Lecturer16', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (54, 'Grad Cert 1', 'Room4', 'Cohort16', 15, 'CC3', '2025-01-08 00:00:00', 'Course16', 5, 'Lecturer16', 'PA1', 1, 1, '-', 'Lecturer16', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (55, 'Grad Cert 1', 'Room14', 'Cohort17', 16, 'CC3', '2025-01-03 00:00:00', 'Course17', 5, 'Lecturer17', 'PA1', 1, 1, '-', 'Lecturer17', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (56, 'Grad Cert 1', 'Room14', 'Cohort17', 16, 'CC3', '2025-01-06 00:00:00', 'Course17', 5, 'Lecturer17', 'PA1', 1, 1, '-', 'Lecturer17', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (57, 'Grad Cert 1', 'Room14', 'Cohort17', 16, 'CC3', '2025-01-07 00:00:00', 'Course17', 5, 'Lecturer17', 'PA1', 1, 1, '-', 'Lecturer17', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (58, 'Grad Cert 1', 'Room14', 'Cohort17', 16, 'CC3', '2025-01-08 00:00:00', 'Course17', 5, 'Lecturer17', 'PA1', 1, 1, '-', 'Lecturer17', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (59, 'Grad Cert 1', 'Room14', 'Cohort17', 16, 'CC3', '2025-01-09 00:00:00', 'Course17', 5, 'Lecturer17', 'PA1', 1, 1, '-', 'Lecturer17', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (60, 'Grad Cert 1', 'Room14', 'Cohort18', 17, 'CC3', '2025-01-10 00:00:00', 'Course18', 5, 'Lecturer18', 'PA1', 1, 1, '-', 'Lecturer18', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (61, 'Grad Cert 1', 'Room14', 'Cohort18', 17, 'CC3', '2025-01-13 00:00:00', 'Course18', 5, 'Lecturer18', 'PA1', 1, 1, '-', 'Lecturer18', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (62, 'Grad Cert 1', 'Room14', 'Cohort18', 17, 'CC3', '2025-01-14 00:00:00', 'Course18', 5, 'Lecturer18', 'PA1', 1, 1, '-', 'Lecturer18', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (63, 'Grad Cert 1', 'Room14', 'Cohort18', 17, 'CC3', '2025-01-15 00:00:00', 'Course18', 5, 'Lecturer18', 'PA1', 1, 1, '-', 'Lecturer18', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (64, 'Grad Cert 1', 'Room14', 'Cohort18', 17, 'CC3', '2025-01-16 00:00:00', 'Course18', 5, 'Lecturer18', 'PA1', 1, 1, '-', 'Lecturer18', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (65, 'Grad Cert 1', 'Room12', 'Cohort19', 18, 'CC3', '2025-01-13 00:00:00', 'Course19', 8, 'Lecturer19', 'PA1', 1, 1, '-', 'Lecturer19', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (66, 'Grad Cert 1', 'Room12', 'Cohort19', 18, 'CC3', '2025-01-14 00:00:00', 'Course19', 8, 'Lecturer19', 'PA1', 1, 1, '-', 'Lecturer19', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (67, 'Grad Cert 1', 'Room12', 'Cohort19', 18, 'CC3', '2025-01-15 00:00:00', 'Course19', 8, 'Lecturer19', 'PA1', 1, 1, '-', 'Lecturer19', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (68, 'Grad Cert 1', 'Room12', 'Cohort19', 18, 'CC3', '2025-01-16 00:00:00', 'Course19', 8, 'Lecturer19', 'PA1', 1, 1, '-', 'Lecturer19', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (69, 'Grad Cert 1', 'Room12', 'Cohort19', 18, 'CC3', '2025-01-17 00:00:00', 'Course19', 8, 'Lecturer19', 'PA1', 1, 1, '-', 'Lecturer19', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (70, 'Grad Cert 1', 'Room12', 'Cohort19', 18, 'CC3', '2025-01-20 00:00:00', 'Course19', 8, 'Lecturer19', 'PA1', 1, 1, '-', 'Lecturer19', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (71, 'Grad Cert 1', 'Room12', 'Cohort19', 18, 'CC3', '2025-01-21 00:00:00', 'Course19', 8, 'Lecturer19', 'PA1', 1, 1, '-', 'Lecturer19', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (72, 'Grad Cert 1', 'Room12', 'Cohort19', 18, 'CC3', '2025-01-22 00:00:00', 'Course19', 8, 'Lecturer19', 'PA1', 1, 1, '-', 'Lecturer19', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (73, 'Grad Cert 1', 'Room15', 'Cohort20', 19, 'CC3', '2025-01-20 00:00:00', 'Course20', 8, 'Lecturer20', 'PA1', 1, 1, '-', 'Lecturer20', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (74, 'Grad Cert 1', 'Room15', 'Cohort20', 19, 'CC3', '2025-01-21 00:00:00', 'Course20', 8, 'Lecturer20', 'PA1', 1, 1, '-', 'Lecturer20', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (75, 'Grad Cert 1', 'Room15', 'Cohort20', 19, 'CC3', '2025-01-22 00:00:00', 'Course20', 8, 'Lecturer20', 'PA1', 1, 1, '-', 'Lecturer20', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (76, 'Grad Cert 1', 'Room15', 'Cohort20', 19, 'CC3', '2025-01-23 00:00:00', 'Course20', 8, 'Lecturer20', 'PA1', 1, 1, '-', 'Lecturer20', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (77, 'Grad Cert 1', 'Room15', 'Cohort20', 19, 'CC3', '2025-01-24 00:00:00', 'Course20', 8, 'Lecturer20', 'PA1', 1, 1, '-', 'Lecturer20', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (78, 'Grad Cert 1', 'Room15', 'Cohort20', 19, 'CC3', '2025-01-27 00:00:00', 'Course20', 8, 'Lecturer20', 'PA1', 1, 1, '-', 'Lecturer20', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (79, 'Grad Cert 1', 'Room15', 'Cohort20', 19, 'CC3', '2025-01-28 00:00:00', 'Course20', 8, 'Lecturer20', 'PA1', 1, 1, '-', 'Lecturer20', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (80, 'Grad Cert 1', 'Room15', 'Cohort20', 19, 'CC3', '2025-01-31 00:00:00', 'Course20', 8, 'Lecturer20', 'PA1', 1, 1, '-', 'Lecturer20', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (81, 'Grad Cert 1', 'Room15', 'Cohort21', 20, 'CC4', '2025-01-07 00:00:00', 'Course21', 2, 'Lecturer21', 'PA1', 2, 1, '-', 'Lecturer21', 'Lecturer31', NULL, NULL);
INSERT INTO "public"."course" VALUES (82, 'Grad Cert 1', 'Room15', 'Cohort21', 20, 'CC4', '2025-01-08 00:00:00', 'Course21', 2, 'Lecturer21', 'PA1', 2, 1, '-', 'Lecturer21', 'Lecturer31', NULL, NULL);
INSERT INTO "public"."course" VALUES (83, 'Grad Cert 1', 'Room12', 'Cohort22', 21, 'CC4', '2025-01-07 00:00:00', 'Course22', 2, 'Lecturer22', 'PA1', 1, 1, '-', 'Lecturer22', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (84, 'Grad Cert 1', 'Room12', 'Cohort22', 21, 'CC4', '2025-01-08 00:00:00', 'Course22', 2, 'Lecturer22', 'PA1', 1, 1, '-', 'Lecturer22', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (85, 'Grad Cert 1', 'Room5', 'Cohort23', 22, 'CC4', '2025-01-03 00:00:00', 'Course23', 2, 'Lecturer23', 'PA1', 1, 1, '-', 'Lecturer23', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (86, 'Grad Cert 1', 'Room5', 'Cohort23', 22, 'CC4', '2025-01-06 00:00:00', 'Course23', 2, 'Lecturer23', 'PA1', 1, 1, '-', 'Lecturer23', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (87, 'Grad Cert 1', 'Room16', 'Cohort24', 23, 'CC4', '2025-01-06 00:00:00', 'Course24', 2, 'Lecturer24', 'PA1', 1, 1, '-', 'Lecturer24', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (88, 'Grad Cert 1', 'Room16', 'Cohort24', 23, 'CC4', '2025-01-07 00:00:00', 'Course24', 2, 'Lecturer24', 'PA1', 1, 1, '-', 'Lecturer24', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (89, 'Grad Cert 1', 'Room7', 'Cohort25', 24, 'CC4', '2025-01-02 00:00:00', 'Course25', 2, 'Lecturer25', 'PA1', 3, 1, '-', 'Lecturer25', 'Lecturer35', 'Lecturer40', NULL);
INSERT INTO "public"."course" VALUES (90, 'Grad Cert 1', 'Room7', 'Cohort25', 24, 'CC4', '2025-01-03 00:00:00', 'Course25', 2, 'Lecturer25', 'PA1', 3, 1, '-', 'Lecturer25', 'Lecturer35', 'Lecturer40', NULL);
INSERT INTO "public"."course" VALUES (91, 'Grad Cert 1', 'Room14', 'Cohort26', 25, 'CC5', '2025-01-22 00:00:00', 'Course26', 2, 'Lecturer26', 'PA1', 1, 1, '-', 'Lecturer26', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (92, 'Grad Cert 1', 'Room14', 'Cohort26', 25, 'CC5', '2025-01-23 00:00:00', 'Course26', 2, 'Lecturer26', 'PA1', 1, 1, '-', 'Lecturer26', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (93, 'Grad Cert 1', 'Room8', 'Cohort27', 26, 'CC5', '2025-01-27 00:00:00', 'Course27', 2, 'Lecturer27', 'PA1', 1, 1, '-', 'Lecturer27', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (94, 'Grad Cert 1', 'Room8', 'Cohort27', 26, 'CC5', '2025-01-28 00:00:00', 'Course27', 2, 'Lecturer27', 'PA1', 1, 1, '-', 'Lecturer27', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (95, 'Grad Cert 1', 'Room11', 'Cohort28', 27, 'CC5', '2025-01-22 00:00:00', 'Course28', 2, 'Lecturer28', 'PA1', 1, 1, '-', 'Lecturer28', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (96, 'Grad Cert 1', 'Room11', 'Cohort28', 27, 'CC5', '2025-01-23 00:00:00', 'Course28', 2, 'Lecturer28', 'PA1', 1, 1, '-', 'Lecturer28', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (97, 'Grad Cert 1', 'Room6', 'Cohort29', 28, 'CC5', '2025-01-09 00:00:00', 'Course29', 4, 'Lecturer29', 'PA1', 1, 1, '-', 'Lecturer29', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (98, 'Grad Cert 1', 'Room6', 'Cohort29', 28, 'CC5', '2025-01-10 00:00:00', 'Course29', 4, 'Lecturer29', 'PA1', 1, 1, '-', 'Lecturer29', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (99, 'Grad Cert 1', 'Room6', 'Cohort29', 28, 'CC5', '2025-01-13 00:00:00', 'Course29', 4, 'Lecturer29', 'PA1', 1, 1, '-', 'Lecturer29', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (100, 'Grad Cert 1', 'Room6', 'Cohort29', 28, 'CC5', '2025-01-14 00:00:00', 'Course29', 4, 'Lecturer29', 'PA1', 1, 1, '-', 'Lecturer29', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (101, 'Grad Cert 2', 'Room5', 'Cohort30', 29, 'CC5', '2025-01-11 00:00:00', 'Course30', 4, 'Lecturer30', 'PA1', 2, 1, '-', 'Lecturer30', 'Lecturer31', NULL, NULL);
INSERT INTO "public"."course" VALUES (102, 'Grad Cert 2', 'Room5', 'Cohort30', 29, 'CC5', '2025-01-17 00:00:00', 'Course30', 4, 'Lecturer30', 'PA1', 2, 1, '-', 'Lecturer30', 'Lecturer31', NULL, NULL);
INSERT INTO "public"."course" VALUES (103, 'Grad Cert 2', 'Room5', 'Cohort30', 29, 'CC5', '2025-01-18 00:00:00', 'Course30', 4, 'Lecturer30', 'PA1', 2, 1, '-', 'Lecturer30', 'Lecturer31', NULL, NULL);
INSERT INTO "public"."course" VALUES (104, 'Grad Cert 2', 'Room5', 'Cohort30', 29, 'CC5', '2025-01-24 00:00:00', 'Course30', 4, 'Lecturer30', 'PA1', 2, 1, '-', 'Lecturer30', 'Lecturer31', NULL, NULL);
INSERT INTO "public"."course" VALUES (105, 'Grad Cert 2', 'Room16', 'Cohort31', 30, 'CC6', '2025-01-04 00:00:00', 'Course31', 2, 'Lecturer31', 'PA1', 1, 1, 'S1', 'Lecturer31', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (106, 'Grad Cert 2', 'Room16', 'Cohort31', 30, 'CC6', '2025-01-10 00:00:00', 'Course31', 2, 'Lecturer31', 'PA1', 1, 1, 'S1', 'Lecturer31', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (107, 'Grad Cert 2', 'Room16', 'Cohort32', 31, 'CC6', '2025-01-25 00:00:00', 'Course32', 2, 'Lecturer32', 'PA1', 1, 1, 'S1', 'Lecturer32', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (108, 'Grad Cert 2', 'Room16', 'Cohort32', 31, 'CC6', '2025-01-31 00:00:00', 'Course32', 2, 'Lecturer32', 'PA1', 1, 1, 'S1', 'Lecturer32', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (109, 'Grad Cert 2', 'Room6', 'Cohort33', 32, 'CC6', '2025-01-03 00:00:00', 'Course33', 2, 'Lecturer33', 'PA1', 1, 1, 'S1', 'Lecturer33', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (110, 'Grad Cert 2', 'Room6', 'Cohort33', 32, 'CC6', '2025-01-04 00:00:00', 'Course33', 2, 'Lecturer33', 'PA1', 1, 1, 'S1', 'Lecturer33', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (111, 'Grad Cert 2', 'Room11', 'Cohort34', 33, 'CC6', '2025-01-10 00:00:00', 'Course34', 2, 'Lecturer34', 'PA1', 1, 1, 'S1', 'Lecturer34', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (112, 'Grad Cert 2', 'Room11', 'Cohort34', 33, 'CC6', '2025-01-11 00:00:00', 'Course34', 2, 'Lecturer34', 'PA1', 1, 1, 'S1', 'Lecturer34', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (113, 'Grad Cert 2', 'Room15', 'Cohort35', 34, 'CC6', '2025-01-17 00:00:00', 'Course35', 2, 'Lecturer35', 'PA1', 1, 1, 'S1', 'Lecturer35', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (114, 'Grad Cert 2', 'Room15', 'Cohort35', 34, 'CC6', '2025-01-18 00:00:00', 'Course35', 2, 'Lecturer35', 'PA1', 1, 1, 'S1', 'Lecturer35', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (115, 'Grad Cert 2', 'Room15', 'Cohort36', 35, 'CC6', '2025-01-10 00:00:00', 'Course36', 2, 'Lecturer36', 'PA1', 2, 1, 'S1', 'Lecturer36', 'Lecturer39', NULL, NULL);
INSERT INTO "public"."course" VALUES (116, 'Grad Cert 2', 'Room15', 'Cohort36', 35, 'CC6', '2025-01-11 00:00:00', 'Course36', 2, 'Lecturer36', 'PA1', 2, 1, 'S1', 'Lecturer36', 'Lecturer39', NULL, NULL);
INSERT INTO "public"."course" VALUES (117, 'Grad Cert 2', 'Room8', 'Cohort37', 36, 'CC6', '2025-01-25 00:00:00', 'Course37', 2, 'Lecturer37', 'PA1', 2, 1, 'S1', 'Lecturer37', 'Lecturer39', NULL, NULL);
INSERT INTO "public"."course" VALUES (118, 'Grad Cert 2', 'Room8', 'Cohort37', 36, 'CC6', '2025-01-31 00:00:00', 'Course37', 2, 'Lecturer37', 'PA1', 2, 1, 'S1', 'Lecturer37', 'Lecturer39', NULL, NULL);
INSERT INTO "public"."course" VALUES (119, 'Grad Cert 2', 'Room2', 'Cohort38', 37, 'CC6', '2025-01-24 00:00:00', 'Course38', 2, 'Lecturer38', 'PA1', 1, 1, 'S1', 'Lecturer38', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (120, 'Grad Cert 2', 'Room2', 'Cohort38', 37, 'CC6', '2025-01-25 00:00:00', 'Course38', 2, 'Lecturer38', 'PA1', 1, 1, 'S1', 'Lecturer38', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (121, 'Grad Cert 2', 'Room14', 'Cohort39', 38, 'CC6', '2025-01-17 00:00:00', 'Course39', 4, 'Lecturer39', 'PA1', 1, 1, 'S1', 'Lecturer39', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (122, 'Grad Cert 2', 'Room14', 'Cohort39', 38, 'CC6', '2025-01-18 00:00:00', 'Course39', 4, 'Lecturer39', 'PA1', 1, 1, 'S1', 'Lecturer39', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (123, 'Grad Cert 2', 'Room14', 'Cohort39', 38, 'CC6', '2025-01-24 00:00:00', 'Course39', 4, 'Lecturer39', 'PA1', 1, 1, 'S1', 'Lecturer39', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (124, 'Grad Cert 2', 'Room14', 'Cohort39', 38, 'CC6', '2025-01-25 00:00:00', 'Course39', 4, 'Lecturer39', 'PA1', 1, 1, 'S1', 'Lecturer39', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (125, 'Grad Cert 2', 'Room10', 'Cohort40', 39, 'CC10', '2025-01-18 00:00:00', 'Course40', 4, 'Lecturer40', 'PA1', 1, 1, 'S1', 'Lecturer40', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (126, 'Grad Cert 2', 'Room10', 'Cohort40', 39, 'CC10', '2025-01-24 00:00:00', 'Course40', 4, 'Lecturer40', 'PA1', 1, 1, 'S1', 'Lecturer40', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (127, 'Grad Cert 2', 'Room10', 'Cohort40', 39, 'CC10', '2025-01-25 00:00:00', 'Course40', 4, 'Lecturer40', 'PA1', 1, 1, 'S1', 'Lecturer40', NULL, NULL, NULL);
INSERT INTO "public"."course" VALUES (128, 'Grad Cert 2', 'Room10', 'Cohort40', 39, 'CC10', '2025-01-31 00:00:00', 'Course40', 4, 'Lecturer40', 'PA1', 1, 1, 'S1', 'Lecturer40', NULL, NULL, NULL);
