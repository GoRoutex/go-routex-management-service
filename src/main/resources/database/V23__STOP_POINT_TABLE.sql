create table stop_point
(
    id         varchar(255) not null
        primary key,
    created_at timestamp(6) with time zone,
    created_by varchar(255),
    updated_at timestamp(6) with time zone,
    updated_by varchar(255),
    address    varchar(255),
    latitude   numeric(38, 2),
    longitude numeric(38, 2),
    name       varchar(255),
    type       smallint
        constraint stop_point_type_check
            check ((type >= 0) AND (type <= 3))
);