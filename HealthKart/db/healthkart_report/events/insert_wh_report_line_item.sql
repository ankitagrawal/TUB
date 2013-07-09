
delimiter |
CREATE EVENT `insert_wh_report_line_item` ON SCHEDULE EVERY 8 HOUR STARTS '2013-07-05 00:15:01' ON COMPLETION NOT PRESERVE ENABLE DO

 BEGIN
truncate table wh_report_line_item;
insert into wh_report_line_item (line_item_id,shipment_charge,collection_charge,estm_shipment_charge,estm_collection_charge,extra_charge)
select
lt.id,
Round(    case when (view1.weight_of_shipment is null or view1.weight_of_shipment = 0)
     then lt.qty/view1.total_qty*st.shipment_charge
    else
    (lt.qty*pv.weight/view1.weight_of_shipment)*st.shipment_charge end ,2)
as shipping_charge,
Round(    case when (view1.price_of_shipment is null or view1.price_of_shipment = 0)

      then lt.qty/view1.total_qty*st.collection_charge
    else
    (
      ( (lt.qty*lt.hk_price)-lt.discount_on_hk_price -lt.order_level_discount - lt.reward_point_discount)/view1.price_of_shipment
    )*st.collection_charge end ,2)
as collection_charge,
Round(    case when (view1.weight_of_shipment is null or view1.weight_of_shipment = 0)
     then lt.qty/view1.total_qty*st.estm_shipment_charge
    else
    (lt.qty*pv.weight/view1.weight_of_shipment)*st.estm_shipment_charge end ,2)
as estm_shipment_charge,
Round(    case when (view1.price_of_shipment is null or view1.price_of_shipment = 0)

      then lt.qty/view1.total_qty*st.estm_collection_charge
    else
    (
      ((lt.qty*lt.hk_price)-lt.discount_on_hk_price -lt.order_level_discount- lt.reward_point_discount)
      /view1.price_of_shipment
    )*st.estm_collection_charge end,2)

as estm_collection_charge,
Round(lt.qty/view1.total_qty*st.extra_charge,2) as extra_charges

from line_item lt inner join
(select so.id,sum(lt.qty*pv.weight) as weight_of_shipment,
sum((lt.qty*lt.hk_price)-lt.discount_on_hk_price -lt.order_level_discount-lt.reward_point_discount) as price_of_shipment,
sum(lt.qty)as total_qty
 from shipping_order so
inner join base_order bo on bo.id = so.base_order_id
inner join line_item lt on lt.shipping_order_id= so.id
inner join sku on sku.id = lt.sku_id
inner join product_variant pv on pv.id = sku.product_variant_id
group by so.id) view1
on view1.id = lt.shipping_order_id
inner join sku on sku.id = lt.sku_id
inner join shipping_order so on so.id = lt.shipping_order_id
inner join shipment st on st.id = so.shipment_id
inner join product_variant pv on pv.id = sku.product_variant_id;
END  |
delimiter ;

