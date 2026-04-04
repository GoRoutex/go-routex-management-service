create table authorities
(
    id          integer      not null
        primary key,
    created_at  timestamp(6) with time zone,
    created_by  varchar(255),
    updated_at  timestamp(6) with time zone,
    updated_by  varchar(255),
    code        varchar(255) not null
        constraint uko3dg4dkhlj82n0iv1i09ubtpt
            unique,
    description varchar(255),
    enabled     boolean,
    name        varchar(255) not null
);

