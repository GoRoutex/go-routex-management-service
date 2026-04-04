create table booking
(
    id                     varchar(255) not null
        primary key,
    created_at             timestamp(6),
    created_by             varchar(255),
    updated_at             timestamp(6),
    updated_by             varchar(255),
    booking_code           varchar(255) not null
        constraint ukgrbhxwawj7ixsr2jeto2vrx34
            unique,
    cancelled_at           timestamp(6) with time zone,
    creator                varchar(255),
    currency               varchar(255),
    customer_id            varchar(255) not null,
    held_at                timestamp(6) with time zone,
    hold_until             timestamp(6) with time zone,
    note                   varchar(255),
    route_id               varchar(255) not null,
    seat_count             integer,
    status                 varchar(255)
        constraint booking_status_check
            check ((status)::text = ANY
                   ((ARRAY ['HELD'::character varying, 'PENDING_PAYMENT'::character varying, 'CONFIRMED'::character varying, 'CANCELLED'::character varying, 'EXPIRED'::character varying])::text[])),
    total_amount           numeric(38, 2),
    cancel_reason          varchar(255),
    channel                varchar(255) not null,
    customer_email         varchar(255),
    customer_name          varchar(255),
    customer_phone         varchar(255),
    hold_token             varchar(255),
    paid_at                timestamp(6) with time zone,
    payment_method         varchar(255),
    payment_reference      varchar(255),
    payment_status         varchar(255)
        constraint booking_payment_status_check
            check ((payment_status)::text = ANY
                   ((ARRAY ['UNPAID'::character varying, 'PROCESSING'::character varying, 'PAID'::character varying, 'FAILED'::character varying, 'REFUNDED'::character varying])::text[])),
    payment_transaction_id varchar(255),
    vehicle_id             varchar(255) not null
);
