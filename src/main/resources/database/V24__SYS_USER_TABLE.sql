create table sys_user
(
    id                     varchar(255) not null
        primary key,
    created_at             timestamp(6) with time zone,
    created_by             varchar(255),
    updated_at             timestamp(6) with time zone,
    updated_by             varchar(255),
    date_of_birth          date         not null,
    email                  varchar(255) not null,
    email_verified         boolean,
    fail_login_count       integer,
    language               varchar(255),
    last_login_at          timestamp(6) with time zone,
    locked_until           timestamp(6) with time zone,
    password_hash          varchar(255) not null,
    phone_number           varchar(255) not null,
    phone_verified         boolean,
    status                 varchar(255) not null
        constraint sys_user_status_check
            check ((status)::text = ANY
                   ((ARRAY ['VERIFYING'::character varying, 'ACTIVE'::character varying, 'INACTIVE'::character varying, 'LOCKED'::character varying])::text[])),
    tenant_id              varchar(255),
    time_zone              varchar(255),
    address                varchar(255),
    avatar_url             varchar(255),
    gender                 varchar(255),
    national_id            varchar(255),
    profile_completed      boolean,
    customer_membership_id varchar(255)
);