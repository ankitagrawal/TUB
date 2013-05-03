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


-- -------------------------------------------------------4th April 2013 -----------------------------------------------------

CREATE TABLE `product_variant_inventory_history` (
`sku_id` int(11) DEFAULT NULL,
`current_qty` int(11) DEFAULT NULL,
`marked_price` decimal(10,2) DEFAULT NULL,
`cost_price` decimal(10,2) DEFAULT NULL,
`hk_price` decimal(10,2) DEFAULT NULL,
`tax_id` int(11) DEFAULT NULL,
`deleted` tinyint(4) DEFAULT NULL,
`create_date` datetime DEFAULT NULL,
`avg_cost_price` decimal(10,2) DEFAULT NULL,
KEY `sku_id` (`sku_id`),
KEY `sku_id_2` (`sku_id`),
KEY `sku_id_3` (`sku_id`),
KEY `pvih_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1


-- -----------------------------------------------------------------------------------------------------------------------------------


ALTER EVENT healthkart_prod.insert_product_variant_inventory_history

ON SCHEDULE

EVERY '1' DAY
STARTS '2013-04-05 00:00:01'

-- EVERY 20 DAY_MINUTE
--   AT CURRENT_TIMESTAMP + INTERVAL 1 DAY

DO


insert into `healthkart_report`.`product_variant_inventory_history`
(sku_id, current_qty, marked_price, cost_price, hk_price, tax_id, deleted,
create_date)
SELECT s.id as sku_id,
ifnull(COUNT( si.id ),0) as inventory,
pv.marked_price,
pv.cost_price,
pv.hk_price,
s.tax_id,
pv.deleted,
now()
FROM sku_item si
  inner join sku_group sg on si.sku_group_id = sg.id and si.sku_item_status_id =10
  right outer join sku s on sg.sku_id = s.id
  inner join product_variant pv on  s.product_variant_id = pv.id
  inner join product p on p.id = pv.product_id
GROUP BY s.id;

-------------------------------------------------------------------------------------------------------------------------------------------------------

ALTER EVENT healthkart_prod.insert_product_variant_inventory_history

ON SCHEDULE

EVERY '1' DAY
STARTS '2013-04-05 01:30:01'

-- EVERY 20 DAY_MINUTE
--   AT CURRENT_TIMESTAMP + INTERVAL 1 DAY

DO


insert into `healthkart_report`.`product_variant_inventory_history`
(sku_id, current_qty, marked_price, cost_price, hk_price, tax_id, deleted,
create_date)
SELECT s.id as sku_id,
ifnull(COUNT( si.id ),0) as inventory,
pv.marked_price,
pv.cost_price,
pv.hk_price,
s.tax_id,
pv.deleted,
now()
FROM sku_item si
  inner join sku_group sg on si.sku_group_id = sg.id and si.sku_item_status_id =10
  right outer join sku s on sg.sku_id = s.id
  inner join product_variant pv on  s.product_variant_id = pv.id
  inner join product p on p.id = pv.product_id
GROUP BY s.id;

----------------------------------------------------------------------------------------------------------------------------------------

delimiter |
ALTER EVENT healthkart_prod.insert_product_variant_inventory_history

ON SCHEDULE

EVERY '1' DAY
STARTS '2013-04-05 00:20:00'
DO
BEGIN
	drop table if exists healthkart_prod.temp_pvi_history;

	create table healthkart_prod.temp_pvi_history
	SELECT s.id as sku_id,
	ifnull(COUNT( si.id ),0) as current_qty,
	pv.marked_price,
	pv.cost_price,
	pv.hk_price,
	s.tax_id,
	pv.deleted,
	now() create_date
	FROM sku_item si
	  inner join sku_group sg on si.sku_group_id = sg.id and si.sku_item_status_id =10
	  right outer join sku s on sg.sku_id = s.id
	  inner join product_variant pv on  s.product_variant_id = pv.id
	  inner join product p on p.id = pv.product_id
	GROUP BY s.id;

	insert into `healthkart_report`.`product_variant_inventory_history`
	(sku_id, current_qty, marked_price, cost_price, hk_price, tax_id, deleted,
	create_date)
	select sku_id, current_qty, marked_price, cost_price, hk_price, tax_id, deleted,
	create_date from temp_pvi_history;


END|
delimiter ;
