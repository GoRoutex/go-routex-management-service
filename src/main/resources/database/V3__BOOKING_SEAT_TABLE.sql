create table booking_seat
(
    id         varchar(255) not null
        primary key,
    created_at timestamp(6),
    created_by varchar(255),
    updated_at timestamp(6),
    updated_by varchar(255),
    booking_id varchar(255) not null,
    creator    varchar(255),
    price      numeric(38, 2),
    route_id   varchar(255) not null,
    seat_no    varchar(255),
    status     varchar(255)
        constraint booking_seat_status_check
            check ((status)::text = ANY
                   ((ARRAY ['HELD'::character varying, 'RESERVED'::character varying, 'CANCELLED'::character varying, 'EXPIRED'::character varying])::text[])),
    ticket_id  varchar(255)
);