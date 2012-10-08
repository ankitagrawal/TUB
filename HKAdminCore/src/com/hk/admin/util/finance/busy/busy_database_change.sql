USE `healthkart_busy`;

ALTER TABLE  `transaction_body` ADD  `cost_price` DECIMAL( 10, 2 ) NULL;

UPDATE transaction_body tb, healthkart_dev.line_item li SET tb.cost_price = li.cost_price
WHERE tb.hk_ref_no = li.id;