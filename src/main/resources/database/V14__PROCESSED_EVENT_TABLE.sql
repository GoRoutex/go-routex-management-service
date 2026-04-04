create table processed_event
(
    event_id      varchar(255)                not null
        primary key,
    consumer_name varchar(255)                not null,
    processed_at  timestamp(6) with time zone not null
);