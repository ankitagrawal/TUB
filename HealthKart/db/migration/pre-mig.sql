-- prod:
update product_variant pv join sku sk on sk.product_variant_id=pv.id set pv.warehouse_id=sk.warehouse_id where pv.warehouse_id is null;

-- Before going to PROD -> Order split and run create PO

-- Aqua:
-- deploy aqua branch in aqua and do ant db + master db
