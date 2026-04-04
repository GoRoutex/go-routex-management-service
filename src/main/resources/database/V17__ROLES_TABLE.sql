create table roles
(
    id          varchar(255) not null
        primary key,
    created_at  timestamp(6),
    created_by  varchar(255),
    updated_at  timestamp(6),
    updated_by  varchar(255),
    code        varchar(255) not null
        constraint ukch1113horj4qr56f91omojv8
            unique,
    description varchar(255),
    enabled     boolean,
    name        varchar(255) not null
);