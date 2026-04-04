create table driver_profile
(
    id                      varchar(255) not null
        primary key,
    created_at              timestamp(6),
    created_by              varchar(255),
    updated_at              timestamp(6),
    updated_by              varchar(255),
    current_route_id        varchar(255),
    emergency_contact_name  varchar(255),
    emergency_contact_phone varchar(255),
    employee_code           varchar(255),
    kyc_verified            boolean,
    license_class           varchar(255),
    license_expiry_date     date,
    license_issue_date      date,
    license_number          varchar(255),
    note                    varchar(255),
    operation_status        varchar(255) not null
        constraint driver_profile_operation_status_check
            check ((operation_status)::text = ANY
                   ((ARRAY ['ONLINE'::character varying, 'OFFLINE'::character varying, 'AVAILABLE'::character varying, 'BUSY'::character varying, 'ON_TRIP'::character varying])::text[])),
    points_delta            integer,
    points_reason           varchar(255),
    rating                  double precision,
    status                  varchar(255) not null
        constraint driver_profile_status_check
            check ((status)::text = ANY
                   ((ARRAY ['ACTIVE'::character varying, 'INACTIVE'::character varying, 'SUSPENDED'::character varying, 'DELETED'::character varying])::text[])),
    total_trips             integer,
    training_completed      boolean,
    user_id                 varchar(255)
);