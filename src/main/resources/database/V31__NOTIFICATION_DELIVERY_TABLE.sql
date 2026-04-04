create table notification_delivery (
    id varchar(64) primary key,
    notification_id varchar(64) not null,
    provider varchar(30) not null,
    target varchar(255),
    status varchar(20) not null,
    provider_message_id varchar(255),
    error_code varchar(100),
    error_message text,
    attempted_at timestamptz not null,
    created_at timestamp(6) with time zone,
    created_by varchar(255),
    updated_at timestamp(6) with time zone,
    updated_by varchar(255)
);