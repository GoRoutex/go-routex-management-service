create table route_seat
(
    id            varchar(255) not null
        primary key,
    creator    varchar(255),
    route_id   varchar(255),
    seat_no    varchar(255),
    status     varchar(255)
        constraint route_seat_status_check
            check ((status)::text = ANY
                   ((ARRAY ['AVAILABLE'::character varying, 'HELD'::character varying, 'SOLD'::character varying, 'BLOCKED'::character varying])::text[])),
    created_at timestamp(6),
    created_by varchar(255),
    updated_at timestamp(6),
    updated_by varchar(255)
);