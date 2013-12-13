-- Delete sili and sicli
SET FOREIGN_KEY_CHECKS = 0;
truncate table sku_item_line_item;
truncate table sku_item_cart_line_item;

-- WH Enabled for Aqua and Disbale for Briht
update warehouse set honoring_b2c_orders=1,warehouse_type=1,active=1 where id in (10,20,402);
update warehouse set honoring_b2c_orders=0,warehouse_type=0,active=0 where id in (1,2,401);

INSERT IGNORE INTO sku_item_status VALUES ('180', 'Migrated');

insert into sku(product_variant_id, warehouse_id, cut_off_inventory,forecasted_quantity, tax_id, create_date, update_date)select product_variant_id, 402, cut_off_inventory, forecasted_quantity, tax_id, now(),now() from sku where warehouse_id = 401;
update sku sk join sku_group sg on sg.sku_id=sk.id join sku_item si on si.sku_group_id=sg.id set sku_item_status_id=180 where sk.warehouse_id=401;
update sku sk join sku_group sg on sg.sku_id=sk.id join sku_item si on si.sku_group_id=sg.id set si.barcode=concat('401-MIG-',si.barcode) where sk.warehouse_id=401;
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (20, '402', '1', 'R', 'Retail Invoice 2013');
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (21, '402', '1', 'T', 'B2B Invoice 2013');
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (22, '402', '1', 'DN', 'Debit Note 2013');

insert into sku(product_variant_id, warehouse_id, cut_off_inventory,forecasted_quantity, tax_id, create_date, update_date)select product_variant_id, 10, cut_off_inventory, forecasted_quantity, tax_id, now(),now() from sku where warehouse_id = 1;
update sku sk join sku_group sg on sg.sku_id=sk.id join sku_item si on si.sku_group_id=sg.id set sku_item_status_id=180 where sk.warehouse_id=1;
update sku sk join sku_group sg on sg.sku_id=sk.id join sku_item si on si.sku_group_id=sg.id set si.barcode=concat('1-MIG-',si.barcode) where sk.warehouse_id=1;
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (26, '10', '1', 'R', 'Retail Invoice 2013');
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (27, '10', '1', 'T', 'B2B Invoice 2013');
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (28, '10', '1', 'DN', 'Debit Note 2013');

insert into sku(product_variant_id, warehouse_id, cut_off_inventory,forecasted_quantity, tax_id, create_date, update_date)select product_variant_id, 20, cut_off_inventory, forecasted_quantity, tax_id, now(),now() from sku where warehouse_id = 2;
update sku sk join sku_group sg on sg.sku_id=sk.id join sku_item si on si.sku_group_id=sg.id set sku_item_status_id=180 where sk.warehouse_id=2;
update sku sk join sku_group sg on sg.sku_id=sk.id join sku_item si on si.sku_group_id=sg.id set si.barcode=concat('2-MIG-',si.barcode) where sk.warehouse_id=2;
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (23, '20', '1', 'R', 'Retail Invoice 2013');
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (24, '20', '1', 'T', 'B2B Invoice 2013');
INSERT IGNORE INTO seek_invoice_num (`id`, `warehouse_id`, `invoice_num`, `prefix`, `description`) VALUES (25, '20', '1', 'DN', 'Debit Note 2013');

update product_variant set warehouse_id=10 where warehouse_id=1;
update product_variant set warehouse_id=20 where warehouse_id=2;
update product_variant set warehouse_id=402 where warehouse_id=401;

UPDATE line_item li JOIN sku sk ON li.sku_id =sk.id JOIN sku sk1 ON sk.product_variant_id = sk1.product_variant_id SET li.sku_id =sk1.id WHERE sk1.warehouse_id=10 AND sk.warehouse_id=1;
UPDATE line_item li JOIN sku sk ON li.sku_id =sk.id JOIN sku sk1 ON sk.product_variant_id = sk1.product_variant_id SET li.sku_id =sk1.id WHERE sk1.warehouse_id=20 AND sk.warehouse_id=2;
UPDATE line_item li JOIN sku sk ON li.sku_id =sk.id JOIN sku sk1 ON sk.product_variant_id = sk1.product_variant_id SET li.sku_id =sk1.id WHERE sk1.warehouse_id=402 AND sk.warehouse_id=401;

update shipping_order set warehouse_id =10 where warehouse_id=1;
update shipping_order set warehouse_id =20 where warehouse_id=2;
update shipping_order set warehouse_id =402 where warehouse_id=401;

update pincode_default_courier set warehouse_id=10 where warehouse_id=1;
update pincode_default_courier set warehouse_id=20 where warehouse_id=2;
update pincode_default_courier set warehouse_id=402 where warehouse_id=401;

update pincode_region_zone set warehouse_id=10 where warehouse_id=1;
update pincode_region_zone set warehouse_id=20 where warehouse_id=2;
update pincode_region_zone set warehouse_id=402 where warehouse_id=401;

update awb set warehouse_id=10 where warehouse_id=1;
update awb set warehouse_id=20 where warehouse_id=2;
update awb set warehouse_id=402 where warehouse_id=401;


INSERT INTO hk_reach_pricing_engine ( warehouse_id, hub_id, inter_city_cost, fixed_cost, update_time, valid_from) SELECT 10, hub_id, inter_city_cost, fixed_cost, NOW(), NOW() FROM hk_reach_pricing_engine WHERE warehouse_id=1;
INSERT INTO hk_reach_pricing_engine ( warehouse_id, hub_id, inter_city_cost, fixed_cost, update_time, valid_from) SELECT 20, hub_id, inter_city_cost, fixed_cost, NOW(), NOW() FROM hk_reach_pricing_engine WHERE warehouse_id=2;
INSERT INTO hk_reach_pricing_engine ( warehouse_id, hub_id, inter_city_cost, fixed_cost, update_time, valid_from) SELECT 402, hub_id, inter_city_cost, fixed_cost, NOW(), NOW() FROM hk_reach_pricing_engine WHERE warehouse_id=401;

update sku_item set sku_item_status_id=10 WHERE sku_item_status_id= 150;
update sku_item set sku_item_status_id=10 WHERE sku_item_status_id= 160;

SET FOREIGN_KEY_CHECKS = 1;

