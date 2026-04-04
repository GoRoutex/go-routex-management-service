create table user_roles
(
    role_id     varchar(255) not null,
    user_id     varchar(255) not null,
    assigned_at timestamp(6) with time zone,
    primary key (role_id, user_id)
);