CREATE TABLE tutors (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255)
);

CREATE TABLE tutor_info (
    tutor_id BIGINT PRIMARY KEY,
    bio TEXT,
    subjects VARCHAR(255) NOT NULL,
    hourly_rate NUMERIC(8, 2),
    experience_years INTEGER,
    CONSTRAINT fk_tutor_info_tutor FOREIGN KEY (tutor_id) REFERENCES tutors (id) ON DELETE CASCADE
);

CREATE TABLE lesson_slots (
    id BIGSERIAL PRIMARY KEY,
    tutor_id BIGINT NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ NOT NULL,
    status VARCHAR(32) NOT NULL,
    CONSTRAINT fk_lesson_slots_tutor FOREIGN KEY (tutor_id) REFERENCES tutors (id) ON DELETE CASCADE
);

CREATE TABLE testimonials (
    id BIGSERIAL PRIMARY KEY,
    tutor_id BIGINT NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_testimonials_tutor FOREIGN KEY (tutor_id) REFERENCES tutors (id) ON DELETE CASCADE
);
