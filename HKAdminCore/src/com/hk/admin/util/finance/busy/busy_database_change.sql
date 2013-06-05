USE `healthkart_busy`;

ALTER TABLE  `transaction_header` ADD  `gateway_order_id` VARCHAR( 20 ) NULL DEFAULT NULL;

ALTER TABLE  `transaction_header` ADD  `awb_number` VARCHAR( 120 ) NULL DEFAULT NULL;


