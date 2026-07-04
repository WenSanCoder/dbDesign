-- 高校智慧教务与校园管理系统 openGauss 初始化脚本
-- 执行方式示例：
-- gsql -h <host> -p <port> -d <database> -U <username> -f sql/01_init_opengauss.sql
--
-- 第一版采用标准英文表名和字段名，便于代码维护。
-- 课程提交版可在此基础上生成个性化命名 SQL。

DROP TABLE IF EXISTS selection_request_log CASCADE;
DROP TABLE IF EXISTS operation_log CASCADE;
DROP TABLE IF EXISTS notice CASCADE;
DROP TABLE IF EXISTS course_review CASCADE;
DROP TABLE IF EXISTS credit_summary CASCADE;
DROP TABLE IF EXISTS grade_record CASCADE;
DROP TABLE IF EXISTS selection_waitlist CASCADE;
DROP TABLE IF EXISTS student_course_selection CASCADE;
DROP TABLE IF EXISTS course_selection_round CASCADE;
DROP TABLE IF EXISTS teaching_class_admin_class CASCADE;
DROP TABLE IF EXISTS class_schedule CASCADE;
DROP TABLE IF EXISTS teaching_class CASCADE;
DROP TABLE IF EXISTS teaching_plan CASCADE;
DROP TABLE IF EXISTS course_prerequisite CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS term CASCADE;
DROP TABLE IF EXISTS user_account CASCADE;
DROP TABLE IF EXISTS teacher CASCADE;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS admin_class CASCADE;
DROP TABLE IF EXISTS major CASCADE;
DROP TABLE IF EXISTS college CASCADE;
DROP TABLE IF EXISTS region CASCADE;

CREATE TABLE college (
    college_id BIGSERIAL PRIMARY KEY,
    college_code VARCHAR(32) NOT NULL UNIQUE,
    college_name VARCHAR(100) NOT NULL,
    contact_phone VARCHAR(30),
    status VARCHAR(20) NOT NULL DEFAULT 'enabled' CHECK (status IN ('enabled', 'disabled')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE major (
    major_id BIGSERIAL PRIMARY KEY,
    major_code VARCHAR(32) NOT NULL UNIQUE,
    major_name VARCHAR(100) NOT NULL,
    college_id BIGINT NOT NULL REFERENCES college(college_id),
    duration_years INT NOT NULL CHECK (duration_years > 0),
    degree_type VARCHAR(30) NOT NULL,
    min_graduate_credit NUMERIC(5,1) NOT NULL CHECK (min_graduate_credit > 0),
    status VARCHAR(20) NOT NULL DEFAULT 'enabled' CHECK (status IN ('enabled', 'disabled'))
);

CREATE TABLE admin_class (
    admin_class_id BIGSERIAL PRIMARY KEY,
    class_code VARCHAR(32) NOT NULL UNIQUE,
    class_name VARCHAR(100) NOT NULL,
    major_id BIGINT NOT NULL REFERENCES major(major_id),
    grade_year INT NOT NULL CHECK (grade_year BETWEEN 2000 AND 2100),
    head_teacher_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'enabled' CHECK (status IN ('enabled', 'disabled'))
);

CREATE TABLE region (
    region_id BIGSERIAL PRIMARY KEY,
    region_code VARCHAR(32) NOT NULL UNIQUE,
    region_name VARCHAR(100) NOT NULL
);

CREATE TABLE student (
    student_id BIGSERIAL PRIMARY KEY,
    student_no VARCHAR(32) NOT NULL UNIQUE,
    student_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('male', 'female')),
    age INT CHECK (age BETWEEN 15 AND 60),
    phone VARCHAR(30),
    admin_class_id BIGINT NOT NULL REFERENCES admin_class(admin_class_id),
    region_id BIGINT REFERENCES region(region_id),
    total_credits NUMERIC(6,1) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'suspended', 'graduated'))
);

CREATE TABLE teacher (
    teacher_id BIGSERIAL PRIMARY KEY,
    teacher_no VARCHAR(32) NOT NULL UNIQUE,
    teacher_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('male', 'female')),
    age INT CHECK (age BETWEEN 20 AND 80),
    title VARCHAR(60),
    phone VARCHAR(30),
    college_id BIGINT NOT NULL REFERENCES college(college_id),
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive'))
);

ALTER TABLE admin_class
    ADD CONSTRAINT fk_admin_class_head_teacher
    FOREIGN KEY (head_teacher_id) REFERENCES teacher(teacher_id);

CREATE TABLE user_account (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password_text VARCHAR(128) NOT NULL,
    role_code VARCHAR(20) NOT NULL CHECK (role_code IN ('ADMIN', 'TEACHER', 'STUDENT')),
    display_name VARCHAR(100) NOT NULL,
    related_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'enabled' CHECK (status IN ('enabled', 'disabled')),
    last_login_at TIMESTAMP
);

CREATE TABLE term (
    term_id BIGSERIAL PRIMARY KEY,
    academic_year VARCHAR(20) NOT NULL,
    semester INT NOT NULL CHECK (semester IN (1, 2, 3)),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_term UNIQUE (academic_year, semester)
);

CREATE TABLE course (
    course_id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(32) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    college_id BIGINT NOT NULL REFERENCES college(college_id),
    credit NUMERIC(4,1) NOT NULL CHECK (credit > 0),
    hours INT NOT NULL CHECK (hours > 0),
    exam_type VARCHAR(20) NOT NULL CHECK (exam_type IN ('exam', 'check')),
    course_type VARCHAR(30) NOT NULL CHECK (course_type IN ('required', 'elective', 'general')),
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'enabled' CHECK (status IN ('enabled', 'disabled'))
);

CREATE TABLE course_prerequisite (
    course_id BIGINT NOT NULL REFERENCES course(course_id),
    prerequisite_course_id BIGINT NOT NULL REFERENCES course(course_id),
    PRIMARY KEY (course_id, prerequisite_course_id),
    CHECK (course_id <> prerequisite_course_id)
);

CREATE TABLE teaching_plan (
    plan_id BIGSERIAL PRIMARY KEY,
    major_id BIGINT NOT NULL REFERENCES major(major_id),
    grade_year INT NOT NULL CHECK (grade_year BETWEEN 2000 AND 2100),
    term_id BIGINT NOT NULL REFERENCES term(term_id),
    course_id BIGINT NOT NULL REFERENCES course(course_id),
    course_nature VARCHAR(30) NOT NULL CHECK (course_nature IN ('required', 'elective', 'general')),
    suggested_semester INT,
    UNIQUE (major_id, grade_year, term_id, course_id)
);

CREATE TABLE teaching_class (
    teaching_class_id BIGSERIAL PRIMARY KEY,
    class_code VARCHAR(32) NOT NULL UNIQUE,
    class_name VARCHAR(100) NOT NULL,
    course_id BIGINT NOT NULL REFERENCES course(course_id),
    teacher_id BIGINT NOT NULL REFERENCES teacher(teacher_id),
    term_id BIGINT NOT NULL REFERENCES term(term_id),
    capacity INT NOT NULL CHECK (capacity > 0),
    selected_count INT NOT NULL DEFAULT 0 CHECK (selected_count >= 0),
    waitlist_count INT NOT NULL DEFAULT 0 CHECK (waitlist_count >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'open' CHECK (status IN ('draft', 'open', 'closed')),
    CHECK (selected_count <= capacity)
);

CREATE TABLE class_schedule (
    schedule_id BIGSERIAL PRIMARY KEY,
    teaching_class_id BIGINT NOT NULL REFERENCES teaching_class(teaching_class_id) ON DELETE CASCADE,
    weekday INT NOT NULL CHECK (weekday BETWEEN 1 AND 7),
    start_period INT NOT NULL CHECK (start_period BETWEEN 1 AND 12),
    end_period INT NOT NULL CHECK (end_period BETWEEN 1 AND 12),
    classroom VARCHAR(100) NOT NULL,
    weeks VARCHAR(80) NOT NULL,
    CHECK (start_period <= end_period)
);

CREATE TABLE teaching_class_admin_class (
    teaching_class_id BIGINT NOT NULL REFERENCES teaching_class(teaching_class_id) ON DELETE CASCADE,
    admin_class_id BIGINT NOT NULL REFERENCES admin_class(admin_class_id) ON DELETE CASCADE,
    PRIMARY KEY (teaching_class_id, admin_class_id)
);

CREATE TABLE course_selection_round (
    round_id BIGSERIAL PRIMARY KEY,
    term_id BIGINT NOT NULL REFERENCES term(term_id),
    round_name VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('not_started', 'open', 'ended')),
    waitlist_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    CHECK (start_time < end_time)
);

CREATE TABLE student_course_selection (
    selection_id BIGSERIAL PRIMARY KEY,
    request_id VARCHAR(64) UNIQUE,
    student_id BIGINT NOT NULL REFERENCES student(student_id),
    teaching_class_id BIGINT NOT NULL REFERENCES teaching_class(teaching_class_id),
    round_id BIGINT NOT NULL REFERENCES course_selection_round(round_id),
    status VARCHAR(20) NOT NULL CHECK (status IN ('processing', 'selected', 'dropped', 'failed')),
    selected_at TIMESTAMP,
    dropped_at TIMESTAMP,
    fail_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    -- 选课历史允许保留多条退课记录，活跃唯一性通过部分唯一索引保证。
    -- 同一课程只能选一个教学班由业务事务校验。
);

CREATE TABLE selection_waitlist (
    waitlist_id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES student(student_id),
    teaching_class_id BIGINT NOT NULL REFERENCES teaching_class(teaching_class_id),
    round_id BIGINT NOT NULL REFERENCES course_selection_round(round_id),
    queue_no INT NOT NULL CHECK (queue_no > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('waiting', 'promoted', 'cancelled', 'expired')),
    waited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    promoted_at TIMESTAMP,
    UNIQUE (student_id, teaching_class_id)
);

CREATE TABLE grade_record (
    grade_id BIGSERIAL PRIMARY KEY,
    selection_id BIGINT NOT NULL UNIQUE REFERENCES student_course_selection(selection_id),
    student_id BIGINT NOT NULL REFERENCES student(student_id),
    teaching_class_id BIGINT NOT NULL REFERENCES teaching_class(teaching_class_id),
    usual_score NUMERIC(5,2) CHECK (usual_score BETWEEN 0 AND 100),
    exam_score NUMERIC(5,2) CHECK (exam_score BETWEEN 0 AND 100),
    final_score NUMERIC(5,2) CHECK (final_score BETWEEN 0 AND 100),
    grade_point NUMERIC(3,2),
    submitted BOOLEAN NOT NULL DEFAULT FALSE,
    submitted_at TIMESTAMP,
    remark VARCHAR(255),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE credit_summary (
    student_id BIGINT PRIMARY KEY REFERENCES student(student_id),
    required_credits NUMERIC(6,1) NOT NULL DEFAULT 0,
    elective_credits NUMERIC(6,1) NOT NULL DEFAULT 0,
    total_credits NUMERIC(6,1) NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course_review (
    review_id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES student(student_id),
    teaching_class_id BIGINT NOT NULL REFERENCES teaching_class(teaching_class_id),
    teacher_id BIGINT NOT NULL REFERENCES teacher(teacher_id),
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    difficulty INT NOT NULL CHECK (difficulty BETWEEN 1 AND 5),
    workload INT NOT NULL CHECK (workload BETWEEN 1 AND 5),
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, teaching_class_id)
);

CREATE TABLE notice (
    notice_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES user_account(user_id),
    notice_type VARCHAR(30) NOT NULL,
    title VARCHAR(120) NOT NULL,
    content TEXT NOT NULL,
    read_flag BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE operation_log (
    log_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    operation_type VARCHAR(30) NOT NULL,
    target_table VARCHAR(80) NOT NULL,
    target_id VARCHAR(80),
    old_value TEXT,
    new_value TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE selection_request_log (
    request_id VARCHAR(64) PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES student(student_id),
    teaching_class_id BIGINT NOT NULL REFERENCES teaching_class(teaching_class_id),
    round_id BIGINT NOT NULL REFERENCES course_selection_round(round_id),
    request_status VARCHAR(30) NOT NULL CHECK (request_status IN ('accepted', 'success', 'failed')),
    mq_status VARCHAR(30) NOT NULL DEFAULT 'reserved',
    retry_count INT NOT NULL DEFAULT 0,
    error_message VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_major_college ON major(college_id);
CREATE INDEX idx_admin_class_major ON admin_class(major_id);
CREATE INDEX idx_student_class ON student(admin_class_id);
CREATE INDEX idx_teacher_college ON teacher(college_id);
CREATE INDEX idx_teaching_class_term_course ON teaching_class(term_id, course_id);
CREATE INDEX idx_schedule_teaching_class ON class_schedule(teaching_class_id);
CREATE INDEX idx_selection_student_status ON student_course_selection(student_id, status);
CREATE INDEX idx_selection_class_status ON student_course_selection(teaching_class_id, status);
CREATE UNIQUE INDEX uk_active_student_class ON student_course_selection(student_id, teaching_class_id)
WHERE status IN ('processing', 'selected');
CREATE INDEX idx_grade_student ON grade_record(student_id);
CREATE INDEX idx_notice_user ON notice(user_id, read_flag);
CREATE INDEX idx_operation_log_created ON operation_log(created_at);

CREATE OR REPLACE VIEW v_student_schedule AS
SELECT
    s.student_id,
    s.student_no,
    s.student_name,
    t.term_id,
    t.academic_year,
    t.semester,
    c.course_code,
    c.course_name,
    tc.teaching_class_id,
    tc.class_name AS teaching_class_name,
    te.teacher_name,
    cs.weekday,
    cs.start_period,
    cs.end_period,
    cs.classroom,
    cs.weeks
FROM student_course_selection scs
JOIN student s ON s.student_id = scs.student_id
JOIN teaching_class tc ON tc.teaching_class_id = scs.teaching_class_id
JOIN course c ON c.course_id = tc.course_id
JOIN teacher te ON te.teacher_id = tc.teacher_id
JOIN term t ON t.term_id = tc.term_id
JOIN class_schedule cs ON cs.teaching_class_id = tc.teaching_class_id
WHERE scs.status = 'selected';

CREATE OR REPLACE VIEW v_teacher_classes AS
SELECT
    te.teacher_id,
    te.teacher_no,
    te.teacher_name,
    tc.teaching_class_id,
    tc.class_code,
    tc.class_name,
    c.course_code,
    c.course_name,
    c.credit,
    t.academic_year,
    t.semester,
    tc.capacity,
    tc.selected_count,
    tc.waitlist_count,
    tc.status
FROM teaching_class tc
JOIN teacher te ON te.teacher_id = tc.teacher_id
JOIN course c ON c.course_id = tc.course_id
JOIN term t ON t.term_id = tc.term_id;

CREATE OR REPLACE VIEW v_course_grade_statistics AS
SELECT
    tc.teaching_class_id,
    tc.class_name,
    c.course_name,
    COUNT(gr.grade_id) AS grade_count,
    ROUND(AVG(gr.final_score), 2) AS avg_score,
    MAX(gr.final_score) AS max_score,
    MIN(gr.final_score) AS min_score,
    ROUND(SUM(CASE WHEN gr.final_score >= 60 THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(gr.grade_id), 0), 2) AS pass_rate,
    ROUND(SUM(CASE WHEN gr.final_score >= 90 THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(gr.grade_id), 0), 2) AS excellent_rate
FROM teaching_class tc
JOIN course c ON c.course_id = tc.course_id
LEFT JOIN grade_record gr ON gr.teaching_class_id = tc.teaching_class_id AND gr.submitted = TRUE
GROUP BY tc.teaching_class_id, tc.class_name, c.course_name;

CREATE OR REPLACE FUNCTION fn_grade_point(p_score NUMERIC)
RETURNS NUMERIC AS $$
BEGIN
    IF p_score IS NULL THEN
        RETURN NULL;
    ELSIF p_score >= 90 THEN
        RETURN 4.0;
    ELSIF p_score >= 85 THEN
        RETURN 3.7;
    ELSIF p_score >= 80 THEN
        RETURN 3.3;
    ELSIF p_score >= 75 THEN
        RETURN 3.0;
    ELSIF p_score >= 70 THEN
        RETURN 2.7;
    ELSIF p_score >= 65 THEN
        RETURN 2.3;
    ELSIF p_score >= 60 THEN
        RETURN 2.0;
    ELSE
        RETURN 0.0;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION trg_grade_before_save()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.usual_score IS NOT NULL AND NEW.exam_score IS NOT NULL THEN
        NEW.final_score := ROUND(NEW.usual_score * 0.3 + NEW.exam_score * 0.7, 2);
        NEW.grade_point := fn_grade_point(NEW.final_score);
    END IF;
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER grade_before_save
BEFORE INSERT OR UPDATE ON grade_record
FOR EACH ROW EXECUTE PROCEDURE trg_grade_before_save();

CREATE OR REPLACE FUNCTION trg_grade_credit_after_save()
RETURNS TRIGGER AS $$
DECLARE
    v_required NUMERIC(6,1);
    v_elective NUMERIC(6,1);
BEGIN
    SELECT
        COALESCE(SUM(CASE WHEN c.course_type = 'required' THEN c.credit ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN c.course_type <> 'required' THEN c.credit ELSE 0 END), 0)
    INTO v_required, v_elective
    FROM grade_record gr
    JOIN teaching_class tc ON tc.teaching_class_id = gr.teaching_class_id
    JOIN course c ON c.course_id = tc.course_id
    WHERE gr.student_id = NEW.student_id
      AND gr.submitted = TRUE
      AND gr.final_score >= 60;

    UPDATE credit_summary
    SET required_credits = v_required,
        elective_credits = v_elective,
        total_credits = v_required + v_elective,
        updated_at = CURRENT_TIMESTAMP
    WHERE student_id = NEW.student_id;

    IF NOT FOUND THEN
        INSERT INTO credit_summary(student_id, required_credits, elective_credits, total_credits, updated_at)
        VALUES(NEW.student_id, v_required, v_elective, v_required + v_elective, CURRENT_TIMESTAMP);
    END IF;

    UPDATE student
    SET total_credits = v_required + v_elective
    WHERE student_id = NEW.student_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER grade_credit_after_save
AFTER INSERT OR UPDATE ON grade_record
FOR EACH ROW EXECUTE PROCEDURE trg_grade_credit_after_save();

CREATE OR REPLACE FUNCTION trg_grade_audit_before_update()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.usual_score IS DISTINCT FROM NEW.usual_score
       OR OLD.exam_score IS DISTINCT FROM NEW.exam_score
       OR OLD.final_score IS DISTINCT FROM NEW.final_score
       OR OLD.submitted IS DISTINCT FROM NEW.submitted THEN
        INSERT INTO operation_log(operation_type, target_table, target_id, old_value, new_value)
        VALUES(
            'UPDATE',
            'grade_record',
            OLD.grade_id::TEXT,
            'usual=' || COALESCE(OLD.usual_score::TEXT, '') || ',exam=' || COALESCE(OLD.exam_score::TEXT, '') || ',final=' || COALESCE(OLD.final_score::TEXT, '') || ',submitted=' || OLD.submitted::TEXT,
            'usual=' || COALESCE(NEW.usual_score::TEXT, '') || ',exam=' || COALESCE(NEW.exam_score::TEXT, '') || ',final=' || COALESCE(NEW.final_score::TEXT, '') || ',submitted=' || NEW.submitted::TEXT
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER grade_audit_before_update
BEFORE UPDATE ON grade_record
FOR EACH ROW EXECUTE PROCEDURE trg_grade_audit_before_update();

CREATE OR REPLACE PROCEDURE proc_student_year_grade(IN p_student_id BIGINT, IN p_academic_year VARCHAR)
AS
BEGIN
    DROP TABLE IF EXISTS tmp_student_year_grade_result;
    CREATE TEMP TABLE tmp_student_year_grade_result ON COMMIT DROP AS
    SELECT
        s.student_no,
        s.student_name,
        t.academic_year,
        t.semester,
        c.course_code,
        c.course_name,
        c.credit,
        gr.final_score,
        gr.grade_point,
        RANK() OVER (PARTITION BY tc.teaching_class_id ORDER BY gr.final_score DESC NULLS LAST) AS class_rank
    FROM grade_record gr
    JOIN student s ON s.student_id = gr.student_id
    JOIN teaching_class tc ON tc.teaching_class_id = gr.teaching_class_id
    JOIN course c ON c.course_id = tc.course_id
    JOIN term t ON t.term_id = tc.term_id
    WHERE gr.student_id = p_student_id
      AND t.academic_year = p_academic_year
      AND gr.submitted = TRUE;
END;

CREATE OR REPLACE PROCEDURE proc_course_grade_distribution(IN p_teaching_class_id BIGINT)
AS
BEGIN
    DROP TABLE IF EXISTS tmp_course_grade_distribution_result;
    CREATE TEMP TABLE tmp_course_grade_distribution_result ON COMMIT DROP AS
    SELECT
        tc.teaching_class_id,
        tc.class_name,
        COUNT(gr.grade_id) AS total_count,
        ROUND(AVG(gr.final_score), 2) AS avg_score,
        MAX(gr.final_score) AS max_score,
        MIN(gr.final_score) AS min_score,
        SUM(CASE WHEN gr.final_score < 60 THEN 1 ELSE 0 END) AS score_0_59,
        SUM(CASE WHEN gr.final_score >= 60 AND gr.final_score < 70 THEN 1 ELSE 0 END) AS score_60_69,
        SUM(CASE WHEN gr.final_score >= 70 AND gr.final_score < 80 THEN 1 ELSE 0 END) AS score_70_79,
        SUM(CASE WHEN gr.final_score >= 80 AND gr.final_score < 90 THEN 1 ELSE 0 END) AS score_80_89,
        SUM(CASE WHEN gr.final_score >= 90 THEN 1 ELSE 0 END) AS score_90_100
    FROM teaching_class tc
    LEFT JOIN grade_record gr ON gr.teaching_class_id = tc.teaching_class_id AND gr.submitted = TRUE
    WHERE tc.teaching_class_id = p_teaching_class_id
    GROUP BY tc.teaching_class_id, tc.class_name;
END;

INSERT INTO college(college_code, college_name, contact_phone) VALUES
('CS', '计算机科学与技术学院', '0571-88880001'),
('MATH', '理学院', '0571-88880002');

INSERT INTO major(major_code, major_name, college_id, duration_years, degree_type, min_graduate_credit) VALUES
('CS2024', '计算机科学与技术', 1, 4, '本科', 160),
('SE2024', '软件工程', 1, 4, '本科', 160);

INSERT INTO teacher(teacher_no, teacher_name, gender, age, title, phone, college_id) VALUES
('T2026001', '张明', 'male', 42, '副教授', '13800000001', 1),
('T2026002', '李敏', 'female', 38, '讲师', '13800000002', 1),
('T2026003', '王强', 'male', 45, '教授', '13800000003', 1);

INSERT INTO admin_class(class_code, class_name, major_id, grade_year, head_teacher_id) VALUES
('CS2401', '计科2401', 1, 2024, 1),
('CS2402', '计科2402', 1, 2024, 1),
('SE2401', '软件2401', 2, 2024, 2);

INSERT INTO region(region_code, region_name) VALUES
('ZJ', '浙江省'),
('JS', '江苏省'),
('AH', '安徽省');

INSERT INTO student(student_no, student_name, gender, age, phone, admin_class_id, region_id) VALUES
('20240001', '陈一', 'male', 20, '13900000001', 1, 1),
('20240002', '刘二', 'female', 20, '13900000002', 1, 2),
('20240003', '周三', 'male', 21, '13900000003', 2, 3),
('20240004', '吴四', 'female', 20, '13900000004', 3, 1);

INSERT INTO credit_summary(student_id) SELECT student_id FROM student;

INSERT INTO user_account(username, password_text, role_code, display_name, related_id) VALUES
('admin', '123456', 'ADMIN', '教务管理员', NULL),
('teacher1', '123456', 'TEACHER', '张明', 1),
('teacher2', '123456', 'TEACHER', '李敏', 2),
('student1', '123456', 'STUDENT', '陈一', 1),
('student2', '123456', 'STUDENT', '刘二', 2);

INSERT INTO term(academic_year, semester, start_date, end_date, is_current) VALUES
('2025-2026', 3, '2026-07-01', '2026-08-31', TRUE);

INSERT INTO course(course_code, course_name, college_id, credit, hours, exam_type, course_type, description) VALUES
('DBS001', '数据库系统', 1, 3.0, 48, 'exam', 'required', '关系数据库、SQL、事务、索引、数据库设计。'),
('JAVA001', 'Java Web 开发', 1, 3.0, 48, 'exam', 'elective', 'Spring Boot 与前后端分离开发。'),
('OS001', '操作系统', 1, 3.5, 56, 'exam', 'required', '进程、线程、内存管理、文件系统。');

INSERT INTO teaching_plan(major_id, grade_year, term_id, course_id, course_nature, suggested_semester) VALUES
(1, 2024, 1, 1, 'required', 3),
(1, 2024, 1, 2, 'elective', 3),
(1, 2024, 1, 3, 'required', 3),
(2, 2024, 1, 1, 'required', 3),
(2, 2024, 1, 2, 'elective', 3);

INSERT INTO teaching_class(class_code, class_name, course_id, teacher_id, term_id, capacity, selected_count, status) VALUES
('DB2026-001', '数据库系统001班', 1, 1, 1, 2, 0, 'open'),
('DB2026-002', '数据库系统002班', 1, 2, 1, 2, 0, 'open'),
('JAVA2026-001', 'Java Web开发001班', 2, 2, 1, 2, 0, 'open'),
('OS2026-001', '操作系统001班', 3, 3, 1, 2, 0, 'open');

INSERT INTO class_schedule(teaching_class_id, weekday, start_period, end_period, classroom, weeks) VALUES
(1, 1, 1, 2, '广A101', '1-16周'),
(2, 2, 3, 4, '广A102', '1-16周'),
(3, 3, 1, 2, '广B201', '1-16周'),
(4, 1, 1, 2, '广C301', '1-16周');

INSERT INTO teaching_class_admin_class(teaching_class_id, admin_class_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 1),
(3, 3),
(4, 1),
(4, 2);

INSERT INTO course_selection_round(term_id, round_name, start_time, end_time, status, waitlist_enabled) VALUES
(1, '第一轮选课', '2026-07-01 08:00:00', '2026-08-01 23:59:59', 'open', TRUE);

INSERT INTO student_course_selection(request_id, student_id, teaching_class_id, round_id, status, selected_at) VALUES
('DEFAULT-20240001-DB001', 1, 1, 1, 'selected', CURRENT_TIMESTAMP),
('DEFAULT-20240002-DB001', 2, 1, 1, 'selected', CURRENT_TIMESTAMP),
('DEFAULT-20240003-DB002', 3, 2, 1, 'selected', CURRENT_TIMESTAMP);

UPDATE teaching_class
SET selected_count = (
    SELECT COUNT(*) FROM student_course_selection scs
    WHERE scs.teaching_class_id = teaching_class.teaching_class_id
      AND scs.status = 'selected'
);

INSERT INTO notice(user_id, notice_type, title, content) VALUES
(NULL, 'system', '选课系统开放通知', '2025-2026 学年第三学期第一轮选课已开放，请在规定时间内完成选课。'),
(NULL, 'system', '成绩录入提醒', '任课教师请在课程结束后及时完成成绩录入。');

-- 验证 SQL 示例：
-- 1. 查询学生课表视图
-- SELECT * FROM v_student_schedule WHERE student_no = '20240001';
-- 2. 查询教师任课视图
-- SELECT * FROM v_teacher_classes WHERE teacher_no = 'T2026001';
-- 3. 查询课程成绩统计视图
-- SELECT * FROM v_course_grade_statistics;
-- 4. 调用存储过程后查询临时结果
-- CALL proc_course_grade_distribution(1);
-- SELECT * FROM tmp_course_grade_distribution_result;
