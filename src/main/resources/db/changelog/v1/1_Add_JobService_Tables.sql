SET SCHEMA 'jobservice';

CREATE TABLE jobservice.jobs (
    id         bigserial PRIMARY KEY,
    job_id  varchar(40) UNIQUE
    last_auth_no varchar(40);
);
CREATE TABLE jobservice.field_periods (
    id            bigserial PRIMARY KEY,
    start_date    date  NOT NULL,
    end_date      date  NOT NULL,
    field_period  varchar(5)
);
CREATE TABLE jobservice.jobs_file (
    id                  bigserial PRIMARY KEY,
    file                bytea NOT NULL,
    filename            varchar  NOT NULL,
    file_timestamp      TIMESTAMP,
    received_timestamp  TIMESTAMP
);