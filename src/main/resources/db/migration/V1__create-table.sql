CREATE TABLE cleaning_event (
  event_id             VARCHAR(36) NOT NULL,
  event_date           DATE,
  event_status         VARCHAR(255),
  cleaning_type_typeId INTEGER,
  cleaning_user_userId VARCHAR(255),
  PRIMARY KEY (event_id)
);
CREATE TABLE cleaning_type (
  type_id   INTEGER     NOT NULL CHECK (type_id <= 99 AND type_id >= 0),
  cycle_day INTEGER     NOT NULL CHECK (cycle_day >= 0 AND cycle_day <= 999),
  type_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (type_id)
);
CREATE TABLE cleaning_user (
  user_id      VARCHAR(255) NOT NULL,
  display_name VARCHAR(255) NOT NULL,
  email        VARCHAR(255) NOT NULL,
  PRIMARY KEY (user_id)
);
ALTER TABLE cleaning_event ADD CONSTRAINT FK_eo5dyk0qmd8q73mv78e7qe5mj FOREIGN KEY (cleaning_type_typeId) REFERENCES cleaning_type;
ALTER TABLE cleaning_event ADD CONSTRAINT FK_5p3ugfkk070iwjab1lrl1imu1 FOREIGN KEY (cleaning_user_userId) REFERENCES cleaning_user;