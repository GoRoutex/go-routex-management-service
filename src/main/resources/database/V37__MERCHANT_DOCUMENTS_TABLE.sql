create table merchant_documents (
    id varchar(36) primary key,
    merchant_id varchar(36) not null,
    document_type     varchar(255)
        constraint merchant_documents_type_Check
            check ((document_type)::text = ANY
                   ((ARRAY ['BUSINESS_LICENSE'::character varying, 'TAX_CERTIFICATE'::character varying, 'REPRESENTATIVE_ID'::character varying])::text[])),
    file_url text not null,
    file_name varchar(255),
    verified_status     varchar(255)
        constraint merchant_documents_verified_status_check
            check ((verified_status)::text = ANY
                   ((ARRAY ['PENDING'::character varying, 'VERIFIED'::character varying, 'REJECTED'::character varying])::text[])),
    verified_note text,
    created_at timestamp(6) with time zone,
    created_by varchar(255),
    updated_at timestamp(6) with time zone,
    updated_by varchar(255)
);