alter table vehicle
    add column if not exists merchant_id varchar(255);

create index if not exists idx_vehicle_merchant_id on vehicle (merchant_id);
