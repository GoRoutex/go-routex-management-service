create table role_authorities
(
    role_id        varchar(255) not null
        constraint fkffl6vh23qrnrld2sxfhtjhbkm
            references roles,
    authorities_id integer      not null
        constraint fk6638rxtfvgs26pi4ypm2b75ab
            references authorities,
    primary key (role_id, authorities_id)
);