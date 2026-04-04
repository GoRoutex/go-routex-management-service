create table payment
(
    id             varchar(255) not null
        primary key,
    created_at     timestamp(6),
    created_by     varchar(255),
    updated_at     timestamp(6),
    updated_by     varchar(255),
    amount         numeric(38, 2),
    booking_id     varchar(255) not null,
    checkout_url   varchar(255),
    code           varchar(255),
    currency       varchar(255),
    description    varchar(255),
    expired_at     timestamp(6) with time zone,
    failed_at      timestamp(6) with time zone,
    failure_reason varchar(255),
    method         varchar(255),
    paid_at        timestamp(6) with time zone,
    payment_token  varchar(255),
    status         varchar(255)
        constraint payment_status_check
            check ((status)::text = ANY
                   ((ARRAY ['UNPAID'::character varying, 'PROCESSING'::character varying, 'PAID'::character varying, 'FAILED'::character varying, 'REFUNDED'::character varying])::text[]))
);