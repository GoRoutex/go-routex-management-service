create table outbox_event (
      id varchar(255) not null primary key,
      aggregate_type varchar(50) not null,
      aggregate_id varchar(64) not null,
      event_type varchar(100) not null,
      event_key varchar(100) not null,
      payload jsonb not null,
      header jsonb,
      status varchar(20) not null default 'PENDING'
          check (status in ('PENDING', 'PROCESSED', 'COMPLETED', 'FAILED')),
      retry_count int not null default 0,
      available_at timestamptz not null default now(),
      created_at             timestamp(6),
      created_by             varchar(255),
      updated_at             timestamp(6),
      updated_by             varchar(255),
      processed_at timestamptz
);

create index idx_outbox_pending
    on outbox_event(status, available_at, id);