create table ticket
(
    id              varchar(255) not null
        primary key,
    created_at      timestamp(6),
    created_by      varchar(255),
    updated_at      timestamp(6),
    updated_by      varchar(255),
    boarded_at      timestamp(6) with time zone,
    boarded_by      varchar(255),
    booking_id      varchar(255),
    booking_seat_id varchar(255),
    cancelled_at    timestamp(6) with time zone,
    cancelled_by    varchar(255),
    checked_in_at   timestamp(6) with time zone,
    checked_in_by   varchar(255),
    customer_name   varchar(255),
    customer_phone  varchar(255),
    issued_at       timestamp(6) with time zone,
    price           numeric(38, 2),
    route_id        varchar(255),
    seat_number     varchar(255),
    status          varchar(255)
        constraint ticket_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ISSUED'::character varying, 'CHECKED_IN'::character varying, 'BOARDED'::character varying, 'CANCELLED'::character varying, 'EXPIRED'::character varying])::text[])),
    ticket_code     varchar(255)
);