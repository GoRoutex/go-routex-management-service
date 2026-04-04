create table route_assignment
(
    id            varchar(255) not null
        primary key,
    created_at    timestamp(6),
    created_by    varchar(255),
    updated_at    timestamp(6),
    updated_by    varchar(255),
    assigned_at   timestamp(6) with time zone,
    creator       varchar(255),
    route_id      varchar(255),
    status        varchar(255)
        constraint route_assignment_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ASSIGNED'::character varying, 'IN_PROGRESS'::character varying, 'DONE'::character varying, 'CANCELED'::character varying])::text[])),
    unassigned_at timestamp(6) with time zone,
    vehicle_id    varchar(255),
    driver_id     varchar(255)
);