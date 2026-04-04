create table vehicle
(
    id            varchar(255) not null
        primary key,
    created_at    timestamp(6) with time zone,
    created_by    varchar(255),
    updated_at    timestamp(6) with time zone,
    updated_by    varchar(255),
    internal_code varchar(255),
    manufacturer  varchar(255),
    mode          varchar(255),
    seat_capacity integer,
    status        varchar(255)
        constraint vehicle_status_check
            check ((status)::text = ANY
                   ((ARRAY ['AVAILABLE'::character varying, 'IN_SERVICE'::character varying, 'MAINTENANCE'::character varying, 'BROKEN'::character varying, 'INACTIVE'::character varying])::text[])),
    type          varchar(255)
        constraint vehicle_type_check
            check ((type)::text = ANY
                   ((ARRAY ['BUS'::character varying, 'TRUCK'::character varying, 'LIMOUSINE'::character varying])::text[])),
    vehicle_plate varchar(255),
    creator       varchar(255),
    has_floor     boolean
);