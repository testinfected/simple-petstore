alter table items drop foreign key items_ibfk_1;
alter table orders drop foreign key orders_ibfk_1;
alter table line_items drop foreign key line_items_ibfk_1;

alter table products modify id bigint not null auto_increment;
alter table items modify id bigint not null auto_increment;
alter table order_numbers modify next_value bigint not null auto_increment;
alter table payments modify id bigint not null auto_increment;
alter table orders modify id bigint not null auto_increment;
alter table line_items modify id bigint not null auto_increment;

alter table items modify product_id bigint not null;
alter table orders modify payment_id bigint;
alter table line_items modify order_id bigint not null;

alter table items add constraint item_product foreign key(product_id) references products(id);
alter table orders add constraint order_payment foreign key(payment_id) references payments(id);
alter table line_items add constraint line_item_order foreign key(order_id) references orders(id);