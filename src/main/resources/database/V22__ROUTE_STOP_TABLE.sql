create table route_stop
(
    id                     varchar(255) not null
        primary key,
    created_at             timestamp(6),
    created_by             varchar(255),
    updated_at             timestamp(6),
    updated_by             varchar(255),
    actual_arrival_time    timestamp(6) with time zone,
    actual_departure_time  timestamp(6) with time zone,
    creator                varchar(255),
    note                   varchar(255),
    planned_arrival_time   timestamp(6) with time zone,
    planned_departure_time timestamp(6) with time zone,
    route_id               varchar(255),
    stop_order             varchar(255),
    stop_point_id          varchar(255),
    operation_point_id     varchar(255),
    stop_address           varchar(255),
    stop_city              varchar(255),
    stop_latitude          double precision,
    stop_longitude         double precision,
    stop_name              varchar(255)
);