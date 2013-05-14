USE `healthkart_busy`;


ALTER TABLE  `transaction_body` ADD  `description` VARCHAR( 120 ) NULL DEFAULT NULL;

ALTER TABLE  `transaction_header` ADD  `goods_receiving_date` DATETIME NULL DEFAULT NULL;

ALTER TABLE  `supplier` ADD  `credit_days` INT NULL DEFAULT NULL;

UPDATE healthkart_busy.supplier bs, healthkart_stag.supplier hks
SET bs.credit_days = hks.credit_days
where hks.tin_number = bs.tin_number;

UPDATE transaction_header th, healthkart_stag.purchase_invoice pi, healthkart_stag.goods_received_note grn, healthkart_stag.purchase_invoice_has_grn pigrn
SET th.goods_receiving_date = min(grn.create_dt)
WHERE pi.id = th.hk_ref_no
AND th.vch_type=2
AND pigrn.purchase_invoice_id = pi.id
AND pigrn.goods_received_note_id = grn.id
GROUP by pi.id;