create table customer_membership
(
    id                       varchar(255) not null
        primary key,
    created_at               timestamp(6) with time zone,
    created_by               varchar(255),
    updated_at               timestamp(6) with time zone,
    updated_by               varchar(255),
    current_available_points numeric(38, 2),
    customer_id              varchar(255),
    membership_tier_id       varchar(255),
    promoted_at              timestamp(6) with time zone,
    status                   smallint
        constraint customer_membership_status_check
            check ((status >= 0) AND (status <= 1)),
    total_points             numeric(38, 2)
);