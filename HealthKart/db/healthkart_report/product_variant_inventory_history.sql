CREATE DATABASE healthkart_report ;

CREATE TABLE healthkart_report.product_variant_inventory_history (
product_variant_id varchar(20) NOT NULL,
current_qty int(11),
marked_price decimal(10,2) NOT NULL,
cost_price decimal(10,2) DEFAULT NULL,
hk_price decimal(10,2) NOT NULL,
deleted tinyint(4) NOT NULL DEFAULT '0',
create_date datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*
create an entry in /etc/my.cnf of mySql server under heading # The MySQL server
event_scheduler = on
*/

SET GLOBAL event_scheduler = ON;

-- Above command is across all the databases

CREATE EVENT healthkart_prod.insert_product_variant_inventory_history

ON SCHEDULE

EVERY '1' DAY
STARTS '2012-03-16 00:00:01'

-- EVERY 20 DAY_MINUTE
--   AT CURRENT_TIMESTAMP + INTERVAL 1 DAY

DO
Insert into
healthkart_report.product_variant_inventory_history
select     inventory_view.product_variant_id,
inventory_view.current_qty,
pv.marked_price,
pv.cost_price,
pv.hk_price,
pv.deleted,
(now()) as  create_date
from
product_variant pv
inner join
(select product_variant_id,sum(qty) as current_qty from product_variant_inventory group by  product_variant_id ) inventory_view

on inventory_view.product_variant_id=pv.id
;


----------------------------------------------------- 4th April 2012 ------------------------------------------------------

ALTER EVENT healthkart_prod.insert_product_variant_inventory_history

ON SCHEDULE

EVERY '1' DAY
STARTS '2012-04-10 00:00:01'

-- EVERY 20 DAY_MINUTE
--   AT CURRENT_TIMESTAMP + INTERVAL 1 DAY

DO
insert into `healthkart_report`.`product_variant_inventory_history`
(sku_id, current_qty, marked_price, cost_price, hk_price, tax_id, deleted,
create_date)
select sku.id,
inventory_view.current_qty,
pv.marked_price,
pv.cost_price,
pv.hk_price,
sku.tax_id,
pv.deleted,(now()) as  create_date
from  sku
left outer join product_variant pv on pv.id = sku.product_variant_id
left outer join
(select sku_id,sum(qty) as current_qty from product_variant_inventory  group by  sku_id ) inventory_view
on inventory_view.sku_id = sku.id
;
