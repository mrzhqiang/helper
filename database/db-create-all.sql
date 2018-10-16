create table customer (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_customer primary key (id)
);

create table ebean_order (
  id                            bigint auto_increment not null,
  order_date                    date not null,
  customer_id                   bigint not null,
  constraint pk_ebean_order primary key (id)
);

create index ix_ebean_order_customer_id on ebean_order (customer_id);
alter table ebean_order add constraint fk_ebean_order_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

