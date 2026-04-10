create table merchant_users (
    id varchar(36) primary key,
    merchant_id varchar(36) not null,
    user_id varchar(36) not null,
    role_code varchar(50) not null,
    status     varchar(255)
        constraint merchant_users_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ACTIVE'::character varying, 'INACTIVE'::character varying, 'SUSPENDED'::character varying])::text[])),

    created_at timestamptz not null,
    updated_at timestamptz not null,
    unique (merchant_id, user_id)
);