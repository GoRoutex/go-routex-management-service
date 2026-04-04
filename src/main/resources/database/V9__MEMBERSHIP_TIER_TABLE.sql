create table membership_tier
(
    id               varchar(255) not null
        primary key,
    created_at       timestamp(6) with time zone,
    created_by       varchar(255),
    updated_at       timestamp(6) with time zone,
    updated_by       varchar(255),
    badge            varchar(255) not null
        constraint membership_tier_badge_check
            check ((badge)::text = ANY
                   ((ARRAY ['BRONZE'::character varying, 'SILVER'::character varying, 'GOLD'::character varying, 'PLATINUM'::character varying, 'VIP'::character varying])::text[])),
    discount_percent integer,
    min_points       numeric(38, 2),
    point_multiplier numeric(38, 2),
    priority_level   integer
);