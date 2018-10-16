alter table ebean_order drop constraint if exists fk_ebean_order_customer_id;
drop index if exists ix_ebean_order_customer_id;

drop table if exists customer;

drop table if exists ebean_order;

