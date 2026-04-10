CREATE TABLE recent_activity (
   id              VARCHAR(255) PRIMARY KEY, -- eventId
   event_type      VARCHAR(255) NOT NULL,
   aggregate_id    VARCHAR(255),
   event_key       VARCHAR(255),
   occurred_at     TIMESTAMP WITH TIME ZONE NOT NULL,
   title           VARCHAR(500),
   message         TEXT,
   actor_user_id   VARCHAR(255),
   actor_name      VARCHAR(255),
   entity_type     VARCHAR(255),
   entity_id       VARCHAR(255),
   merchant_id     VARCHAR(255),
   header          JSONB,
   payload         JSONB,
   created_at timestamp(6) with time zone,
   created_by varchar(255),
   updated_at timestamp(6) with time zone,
   updated_by varchar(255)
);