create table merchants (
   id varchar(36) primary key,
   code varchar(50) not null unique,
   name varchar(255) not null,
   tax_code varchar(50),
   phone varchar(20),
   email varchar(255),
   address text,
   representative_name varchar(255),
   status     varchar(255)
       constraint merchants_status_check
           check ((status)::text = ANY
                  ((ARRAY ['ACTIVE'::character varying, 'INACTIVE'::character varying, 'PENDING'::character varying])::text[])),
   created_at timestamp(6) with time zone,
   created_by varchar(255),
   updated_at timestamp(6) with time zone,
   updated_by varchar(255)
);

