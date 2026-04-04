create table route
(
    id                 varchar(255) not null
        primary key,
    created_at         timestamp(6),
    created_by         varchar(255),
    updated_at         timestamp(6),
    updated_by         varchar(255),
    actual_end_time    timestamp(6) with time zone,
    actual_start_time  timestamp(6) with time zone,
    creator            varchar(255),
    destination        varchar(255),
    origin             varchar(255),
    pickup_branch      varchar(255),
    planned_end_time   timestamp(6) with time zone,
    planned_start_time timestamp(6) with time zone,
    route_code         varchar(255),
    status             varchar(255)
        constraint route_status_check
            check ((status)::text = ANY
                   ((ARRAY ['PLANNED'::character varying, 'ASSIGNED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'DELAYED'::character varying, 'CANCELED'::character varying])::text[]))
);