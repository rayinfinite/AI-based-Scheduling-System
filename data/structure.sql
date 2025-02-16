CREATE TABLE "user" (
    "email" varchar(255) PRIMARY KEY,
    "active" bool NOT NULL,
    "created" timestamp(6),
    "last_login" timestamp(6),
    "name" varchar(255),
    "phone" varchar(255),
    "role" varchar(255)
);

CREATE TABLE "audit_log" (
    "id" SERIAL PRIMARY KEY,
    "email" varchar(255),
    "params" varchar(255),
    "path" varchar(255),
    "timestamp" timestamp(6),
    "username" varchar(255),
    FOREIGN KEY (email) REFERENCES "user" (email)
);

CREATE TABLE "classroom" (
    "id" SERIAL PRIMARY KEY,
    "name" varchar(255),
    "size" integer,
    "software" varchar(255)
);

DROP TABLE IF EXISTS "professor";
CREATE TABLE professor (
    professor_id SERIAL PRIMARY KEY,
    professor_name VARCHAR(255) NOT NULL UNIQUE 
);

CREATE TABLE "course" (
    "id" SERIAL PRIMARY KEY,
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
    "week" varchar(255),
    FOREIGN KEY (teacher1) REFERENCES professor (professor_name),
    FOREIGN KEY (teacher2) REFERENCES professor (professor_name),
    FOREIGN KEY (teacher3) REFERENCES professor (professor_name)
);

CREATE TABLE cohort (
    id SERIAL PRIMARY KEY,
    cohort_size INT NOT NULL,
    cohort_type VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type_id INT
);

CREATE TABLE timeslot (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL
);

CREATE TABLE teaching_plan (
    plan_id SERIAL PRIMARY KEY,
    cohort_id INT,
    course_id INT,
    teacher1 INT,
    teacher2 INT,
    teacher3 INT,
    timeslot_id INT,
    room_id INT,
    FOREIGN KEY (cohort_id) REFERENCES cohort (id),
    FOREIGN KEY (course_id) REFERENCES course (id),
    FOREIGN KEY (teacher1) REFERENCES professor (professor_id),
    FOREIGN KEY (teacher2) REFERENCES professor (professor_id),
    FOREIGN KEY (teacher3) REFERENCES professor (professor_id),
    FOREIGN KEY (timeslot_id) REFERENCES timeslot (id),
    FOREIGN KEY (room_id) REFERENCES classroom (id)
);