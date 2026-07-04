-- Demo account repair script.
-- Use this after 01_init_opengauss.sql if user_account has no demo accounts
-- or demo passwords/status values are not aligned with the frontend hints.
-- Execute in database postgres. The tables are expected in schema sht.

SET search_path TO sht;

INSERT INTO user_account(username, password_text, role_code, display_name, related_id, status)
VALUES
('admin', '123456', 'ADMIN', 'Academic Admin', NULL, 'enabled'),
('teacher1', '123456', 'TEACHER', 'Teacher Zhang', 1, 'enabled'),
('teacher2', '123456', 'TEACHER', 'Teacher Li', 2, 'enabled'),
('student1', '123456', 'STUDENT', 'Student Chen', 1, 'enabled'),
('student2', '123456', 'STUDENT', 'Student Liu', 2, 'enabled')
ON CONFLICT (username) DO UPDATE
SET password_text = EXCLUDED.password_text,
    role_code = EXCLUDED.role_code,
    display_name = EXCLUDED.display_name,
    related_id = EXCLUDED.related_id,
    status = EXCLUDED.status;

SELECT username, role_code, status, password_text
FROM user_account
WHERE username IN ('admin', 'teacher1', 'teacher2', 'student1', 'student2')
ORDER BY username;
