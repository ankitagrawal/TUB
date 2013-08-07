CREATE TABLE sku_item_cart_line_item
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    sku_item_id INT NOT NULL,
    cart_line_item_id INT NOT NULL,
    unit_num INT NOT NULL,
    product_variant_id VARCHAR(20) NOT NULL,
    KEY `fk_sku_item_cart_line_item_sku_item_id1` (`sku_item_id`),
    KEY `fk_sku_item_cart_line_item_cart_line_item_id1` (`cart_line_item_id`),
    KEY `fk_sku_item_cart_line_item_product_variant_id1` (`product_variant_id`),    
    CONSTRAINT `fk_sku_item_cart_line_item_sku_item_id1` FOREIGN KEY (`sku_item_id`) REFERENCES `sku_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_sku_item_cart_line_item_cart_line_item_id1` FOREIGN KEY (`cart_line_item_id`) REFERENCES `cart_line_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_sku_item_cart_line_item_product_variant_id1` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
    
);

CREATE TABLE sku_item_line_item
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    sku_item_id INT NOT NULL,
    line_item_id INT NULL,
    unit_num INT NOT NULL,
    sku_item_cart_line_item_id INT NOT NULL,
    product_variant_id VARCHAR(20) NOT NULL,
    KEY `fk_sku_item_line_item_sku_item_id1` (`sku_item_id`),
    KEY `fk_sku_item_line_item_line_item_id1` (`line_item_id`),
    KEY `fk_sku_item_line_item_product_variant_id1` (`product_variant_id`),    
    KEY `fk_sku_item_line_item_sku_item_cart_line_item_id1` (`sku_item_cart_line_item_id`),    
    CONSTRAINT `fk_sku_item_line_item_sku_item_id1` FOREIGN KEY (`sku_item_id`) REFERENCES `sku_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_sku_item_line_item_line_item_id1` FOREIGN KEY (`line_item_id`) REFERENCES `line_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_sku_item_line_item_product_variant_id1` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_sku_item_line_item_sku_item_cart_line_item_id1` FOREIGN KEY (`sku_item_cart_line_item_id`) REFERENCES `sku_item_cart_line_item` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
    
);

CREATE TABLE `sku_item_owner` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;


ALTER TABLE sku_item ADD COLUMN sku_item_owner_id INT;
ALTER TABLE sku_item ADD  CONSTRAINT  fk_sku_item_sku_item_owner_id1 FOREIGN KEY (sku_item_owner_id) REFERENCES sku_item_owner(id) ON DELETE NO ACTION ON UPDATE NO ACTION;

