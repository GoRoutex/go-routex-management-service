alter table driver_profile
    add column if not exists merchant_id varchar(255);

alter table operation_point
    add column if not exists merchant_id varchar(255);

alter table route
    add column if not exists merchant_id varchar(255);

alter table route_assignment
    add column if not exists merchant_id varchar(255);

create index if not exists idx_driver_profile_merchant_id on driver_profile (merchant_id);
create index if not exists idx_operation_point_merchant_id on operation_point (merchant_id);
create index if not exists idx_route_merchant_id on route (merchant_id);
create index if not exists idx_route_assignment_merchant_id on route_assignment (merchant_id);
