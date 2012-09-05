USE `healthkart_busy`;

CREATE TABLE IF NOT EXISTS `item_detail` (
  `item_code` int(11) NOT NULL,
  `item_name` varchar(40) DEFAULT NULL,
  `alias` varchar(40) DEFAULT NULL,
  `print_name` varchar(40) DEFAULT NULL,
  `parent_group` varchar(40) DEFAULT NULL,
  `unit` varchar(40) DEFAULT NULL,
  `opening_stock` decimal(10,2) DEFAULT NULL,
  `opening_stock_value` decimal(10,2) DEFAULT NULL,
  `sale_price` decimal(10,2) DEFAULT NULL,
  `purchase_price` decimal(10,2) DEFAULT NULL,
  `mrp` decimal(10,2) DEFAULT NULL,
  `self_value_price` decimal(10,2) DEFAULT NULL,
  `sale_discount` decimal(10,2) DEFAULT NULL,
  `purchase_discount` decimal(10,2) DEFAULT NULL,
  `min_sale_price` decimal(10,2) DEFAULT NULL,
  `tax_rate_local` decimal(10,2) DEFAULT NULL,
  `tax_rate_central` decimal(10,2) DEFAULT NULL,
  `tax_on_mrp` tinyint(4) DEFAULT NULL,
  `item_description_1` varchar(40) DEFAULT NULL,
  `item_description_2` varchar(40) DEFAULT NULL,
  `item_description_3` varchar(40) DEFAULT NULL,
  `item_description_4` varchar(40) DEFAULT NULL,
  `imported` tinyint(4) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`item_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `supplier` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `line1` varchar(200) DEFAULT NULL,
  `line2` varchar(200) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(100) NOT NULL,
  `pincode` varchar(45) DEFAULT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `contact_number` varchar(45) DEFAULT NULL,
  `tin_number` varchar(45) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tin_number_UNIQUE` (`tin_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `transaction_header` (
  `vch_code` int(11) NOT NULL AUTO_INCREMENT,
  `series` varchar(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `vch_no` varchar(25) DEFAULT NULL,
  `vch_type` int(11) DEFAULT NULL,
  `sale_type` varchar(40) DEFAULT NULL,
  `account_name` varchar(40) DEFAULT NULL,
  `debtors` varchar(40) DEFAULT NULL,
  `address_1` varchar(40) DEFAULT NULL,
  `address_2` varchar(40) DEFAULT NULL,
  `address_3` varchar(40) DEFAULT NULL,
  `address_4` varchar(40) DEFAULT NULL,
  `tin_number` varchar(30) DEFAULT NULL,
  `material_centre` varchar(40) DEFAULT NULL,
  `narration` varchar(90) DEFAULT NULL,
  `out_of_state` tinyint(4) DEFAULT NULL,
  `against_form` varchar(30) DEFAULT NULL,
  `net_amount` decimal(10,2) DEFAULT NULL,
  `imported` tinyint(4) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `hk_ref_no` int(11),
  PRIMARY KEY (`vch_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `transaction_body` (
  `vch_code` int(11) NOT NULL,
  `s_no` int(11) NOT NULL,
  `item_code` int(11) NOT NULL,
  `qty` int(11) NOT NULL,
  `unit` varchar(40) DEFAULT NULL,
  `mrp` decimal(10,2) DEFAULT NULL,
  `rate` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `vat` decimal(10,5) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL,
  `create_date` datetime NOT NULL,
  `hk_ref_no` int(11),
  UNIQUE KEY `transaction_header_vch_s_no_unique` (`s_no`,`vch_code`),
  KEY `fk_transaction_2_item_detail1` (`item_code`),
  KEY `fk_transaction_body_transaction_header1` (`vch_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `transaction_footer` (
  `vch_code` int(11) NOT NULL,
  `s_no` int(11) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `bill_sundry_name` varchar(40) DEFAULT NULL,
  `percent` int(11) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  UNIQUE KEY `transaction_header_vch_code_s_no_unique` (`vch_code`,`s_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `transaction_footer`
  ADD CONSTRAINT `fk_transaction_footer_transaction_header1` FOREIGN KEY (`vch_code`) REFERENCES `transaction_header` (`vch_code`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `transaction_body`
  ADD CONSTRAINT `fk_transaction_2_item_detail1` FOREIGN KEY (`item_code`) REFERENCES `item_detail` (`item_code`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_transaction_body_transaction_header1` FOREIGN KEY (`vch_code`) REFERENCES `transaction_header` (`vch_code`) ON DELETE NO ACTION ON UPDATE NO ACTION;


CREATE USER 'busy'@'%' IDENTIFIED BY  PASSWORD '*705DCD757EB6D508C832EE2E7FC5D592D7A38680';

GRANT USAGE ON * . * TO  'busy'@'%' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0 ;

GRANT SELECT ON  `healthkart\_busy` . * TO  'busy'@'%';

GRANT UPDATE (
`imported`
) ON  `healthkart_busy`.`transaction_header` TO  'busy'@'%';

GRANT UPDATE (
`imported`
) ON  `healthkart_busy`.`item_detail` TO  'busy'@'%';

GRANT UPDATE (
`imported`
) ON  `healthkart_busy`.`supplier` TO  'busy'@'%';


