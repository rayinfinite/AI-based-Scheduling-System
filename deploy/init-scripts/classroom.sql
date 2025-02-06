-- ----------------------------
-- Table structure for classroom
-- ----------------------------
CREATE TABLE "classroom"
(
    "id"       SERIAL PRIMARY KEY NOT NULL,
    "name"     varchar(255),
    "size"     integer,
    "software" varchar(255)
);

-- ----------------------------
-- Records of classroom
-- ----------------------------
INSERT INTO "classroom" ("name", "size") VALUES
('Room1', 41),
('Room2', 133),
('Room3', 70),
('Room4', 110),
('Room5', 50),
('Room6', 40),
('Room7', 60),
('Room8', 126),
('Room9', 36),
('Room10', 110),
('Room11', 40),
('Room12', 40),
('Room13', 32),
('Room14', 100),
('Room15', 50),
('Room16', 40);
