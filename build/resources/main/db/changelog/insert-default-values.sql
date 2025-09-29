DO $$
BEGIN

    IF NOT EXISTS (SELECT 1 FROM roles WHERE role_name = 'ROLE_ADMIN') THEN
        INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM roles WHERE role_name = 'ROLE_USER') THEN
        INSERT INTO roles (role_name) VALUES ('ROLE_USER');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'code.core.crafter@gmail.com') THEN
        INSERT INTO users (first_name, last_name, email, password, created_at, updated_at)
        VALUES ('Aniket', 'Bedarkar', 'code.core.crafter@gmail.com', '$2a$10$TodM0pBtPJGrzXoSrmBxWedyHnTGBGHp6BvoGHXGxDmo8nY7c7Qp.', now(), now());
    END IF;

    -- Assign ROLE_ADMIN if not already assigned
    IF NOT EXISTS (
        SELECT 1 FROM user_roles
        WHERE user_id = (SELECT id FROM users WHERE email = 'code.core.crafter@gmail.com')
          AND role_id = (SELECT id FROM roles WHERE role_name = 'ROLE_ADMIN')
    ) THEN
        INSERT INTO user_roles (user_id, role_id)
        VALUES (
            (SELECT id FROM users WHERE email = 'code.core.crafter@gmail.com'),
            (SELECT id FROM roles WHERE role_name = 'ROLE_ADMIN')
        );
    END IF;

END
$$;
