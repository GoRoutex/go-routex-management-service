create table operation_point
(
    id         varchar(255) not null
        primary key,
    created_at timestamp(6) with time zone,
    created_by varchar(255),
    updated_at timestamp(6) with time zone,
    updated_by varchar(255),
    address    varchar(255),
    city       varchar(255),
    code       varchar(255),
    latitude   double precision,
    longitude  double precision,
    name       varchar(255),
    status     varchar(255)
        constraint operation_point_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ACTIVE'::character varying, 'INACTIVE'::character varying, 'CLOSED'::character varying])::text[])),
    type       varchar(255)
        constraint operation_point_type_check
            check ((type)::text = ANY
                   ((ARRAY ['OPERATION_POINT'::character varying, 'PUBLIC_STATION'::character varying])::text[]))
);