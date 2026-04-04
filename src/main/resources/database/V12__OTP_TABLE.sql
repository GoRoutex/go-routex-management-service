create table otp
(
    id            varchar(255)                not null
        primary key,
    created_at    timestamp(6) with time zone,
    created_by    varchar(255),
    updated_at    timestamp(6) with time zone,
    updated_by    varchar(255),
    attempt_count integer                     not null,
    consumed_at   timestamp(6) with time zone,
    email         varchar(255)                not null,
    expired_at    timestamp(6) with time zone not null,
    otp_hash      varchar(255)                not null,
    phone_number  varchar(255)                not null,
    produced_at   timestamp(6) with time zone not null,
    purpose       varchar(255)                not null
        constraint otp_purpose_check
            check ((purpose)::text = ANY
                   ((ARRAY ['REGISTER_VERIFY'::character varying, 'FORGOT_PASSWORD'::character varying])::text[])),
    status        varchar(255)                not null
        constraint otp_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ACTIVE'::character varying, 'USED'::character varying, 'REVOKED'::character varying])::text[])),
    user_id       varchar(255)                not null
);