create table coin (
                      id bigserial not null,
                      coin_id varchar(255),
                      coin_reached_desired_price boolean,
                      created_date timestamp(6),
                      desired_price float(53),
                      image varchar(255),
                      seen boolean,
                      send_on_email boolean,
                      start_price float(53),
                      updated_date timestamp(6),
                      user_id bigint,
                      primary key (id)
);

create table confirmation (
                              id bigint not null,
                              created_date timestamp(6),
                              token varchar(255),
                              user_id bigint not null,
                              primary key (id)
);

create table token (
                       id integer not null,
                       expired boolean not null,
                       revoked boolean not null,
                       token varchar(255),
                       token_type varchar(255),
                       user_id bigint,
                       primary key (id)
);

create table user_role (
                           user_id bigint not null,
                           roles varchar(255)
);

create table usr (
                     id bigserial not null,
                     email varchar(255),
                     email_confirmed boolean,
                     password varchar(255),
                     username varchar(255),
                     primary key (id)
);

alter table if exists token
    drop constraint if exists UK_pddrhgwxnms2aceeku9s2ewy5;

alter table if exists token
    add constraint UK_pddrhgwxnms2aceeku9s2ewy5 unique (token);
create sequence confirmation_seq start with 1 increment by 50;
create sequence token_seq start with 1 increment by 50;


alter table if exists coin
    add constraint FKc6p2q778gwpu6ud1w1pxcdkn
        foreign key (user_id)
            references usr;


alter table if exists confirmation
    add constraint FK98klac6ktt2d25c3nvau7rxcg
        foreign key (user_id)
            references usr;


alter table if exists token
    add constraint FKssf1kt08wvjrqg5x50facgn4g
        foreign key (user_id)
            references usr;


alter table if exists user_role
    add constraint FKfpm8swft53ulq2hl11yplpr5
        foreign key (user_id)
            references usr