create table notification
(
    id varchar(64) primary key,
    dedupe_key varchar(150) not null,
    trip_id varchar(64),
    driver_id varchar(64) not null,
    channel varchar(20) not null,
    type varchar(50) not null,
    title varchar(255) not null,
    body text not null,
    deeplink varchar(500),
    payload jsonb,
    status varchar(20) not null,
    sent_at timestamp(6) with time zone,
    created_at timestamp(6) with time zone,
    created_by varchar(255),
    updated_at timestamp(6) with time zone,
    updated_by varchar(255)
);
