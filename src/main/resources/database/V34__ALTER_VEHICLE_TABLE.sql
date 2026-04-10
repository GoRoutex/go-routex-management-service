alter table vehicle
    add column merchant_id varchar(36),
    add column approval_status varchar(30) default 'PENDING',
    add column approved_by varchar(36),
    add column approved_at timestamptz;