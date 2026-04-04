create table customer
(
    id              varchar(255) not null
        primary key,
    created_at      timestamp(6) with time zone,
    created_by      varchar(255),
    updated_at      timestamp(6) with time zone,
    updated_by      varchar(255),
    full_name       varchar(255),
    status          varchar(255) not null
        constraint customer_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ACTIVE'::character varying, 'FORBIDDEN'::character varying, 'INACTIVE'::character varying])::text[])),
    total_spent     numeric(38, 2),
    total_trips     integer,
    user_id         varchar(255) not null,
    last_booking_at timestamp(6) with time zone,
    last_trip_at    timestamp(6) with time zone,
    trip_points     numeric(38, 2)
);