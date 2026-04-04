create table refresh_token
(
    id         varchar(255)                not null
        primary key,
    created_at timestamp(6) with time zone,
    created_by varchar(255),
    updated_at timestamp(6) with time zone,
    updated_by varchar(255),
    expired_at timestamp(6) with time zone not null,
    issued_at  timestamp(6) with time zone not null,
    revoked_at timestamp(6) with time zone,
    status     varchar(255)
        constraint refresh_token_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ACTIVE'::character varying, 'USED'::character varying, 'REVOKED'::character varying, 'EXPIRED'::character varying])::text[])),
    token      text                        not null,
    used_at    timestamp(6) with time zone,
    user_id    varchar(255)                not null
);