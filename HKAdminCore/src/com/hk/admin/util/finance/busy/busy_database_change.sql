USE `healthkart_busy`;


ALTER TABLE  `transaction_body` ADD  `description` VARCHAR( 120 ) NULL DEFAULT NULL;

ALTER TABLE  `transaction_header` ADD  `goods_receiving_date` DATETIME NULL DEFAULT NULL;

ALTER TABLE  `supplier` ADD  `credit_days` INT NULL DEFAULT NULL;

UPDATE healthkart_busy.supplier bs, healthkart_prod.supplier hks
SET bs.credit_days = hks.credit_days
where hks.tin_number = bs.tin_number;


create table pi_grn_date_temp
SELECT pi.id as pi_id, min(grn.grn_date) as grn_date FROM
 healthkart_prod.purchase_invoice pi, healthkart_prod.goods_received_note grn, healthkart_prod.purchase_invoice_has_grn pigrn
WHERE pigrn.purchase_invoice_id = pi.id
AND pigrn.goods_received_note_id = grn.id
GROUP by pi.id;

UPDATE transaction_header th,healthkart_prod.pi_grn_date_temp grn
SET th.goods_receiving_date = grn.grn_date
WHERE grn.pi_id = th.hk_ref_no
AND th.vch_type=2;

drop table healthkart_prod.pi_grn_date_temp;