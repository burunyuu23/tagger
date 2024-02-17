CREATE TYPE user_role AS ENUM ('ADMIN', 'GROUP', 'TEST');

CREATE TYPE lesson_time AS ENUM (
    'FIRST_LESSON',
    'SECOND_LESSON',
    'THIRD_LESSON',
    'FOURTH_LESSON',
    'FIFTH_LESSON',
    'SIXTH_LESSON',
    'SEVENTH_LESSON',
    'EIGHTH_LESSON'
    );

INSERT INTO users (id, group_id) VALUES (289070067, null);
INSERT INTO user_roles (user_id, role) VALUES (289070067, 'ADMIN');
INSERT INTO users (id, group_id) VALUES (328597719, null);
INSERT INTO user_roles (user_id, role) VALUES (328597719, 'ADMIN');
