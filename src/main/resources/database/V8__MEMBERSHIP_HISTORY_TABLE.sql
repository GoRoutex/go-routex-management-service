create table membership_history
(
    id               varchar(255)   not null
        primary key,
    created_at       timestamp(6) with time zone,
    created_by       varchar(255),
    updated_at       timestamp(6) with time zone,
    updated_by       varchar(255),
    current_tier_id  varchar(255)   not null,
    previous_tier_id varchar(255),
    reason           varchar(255)   not null,
    total_spent      numeric(38, 2) not null,
    trigger_points   integer        not null,
    type             varchar(255)   not null
        constraint membership_history_type_check
            check ((type)::text = ANY ((ARRAY ['UPGRADE'::character varying, 'DOWNGRADE'::character varying])::text[])),
    user_id          varchar(255)   not null
);