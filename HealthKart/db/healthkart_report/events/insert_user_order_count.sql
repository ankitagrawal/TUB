delimiter |
CREATE EVENT `insert_user_order_count` ON SCHEDULE EVERY '1' DAY STARTS '2013-07-05 00:15:01' ON COMPLETION NOT PRESERVE ENABLE DO


BEGIN
truncate base_order_report;

Insert into base_order_report (order_id,user_id,order_index)

select id,user_id,0 from base_order where order_status_id in (15,20,25,30,40) ;

update base_order_report bor,

(select v1.order_id,v1.user_id,v1.counter from
(select user_id ,id as order_id,
@previous_user := @current_user_id,
@current_user_id := bo.user_id  ,
if(@current_user_id = @previous_user ,@counter := @counter + 1  ,@counter:=1) as counter,
@previous_user := @current_user_id as u1
from base_order bo where order_status_id in (15,20,25,30,40)
group by user_id,id
order by user_id )v1)v2
set bor.order_index = v2.counter where v2.order_id = bor.order_id and v2.user_id = bor.user_id
;

truncate table user_report;

Insert into user_report (user_id)
select id from user;

update user_report ur,
(select bo.user_id,count(distinct id)as orders from base_order bo where bo.order_status_id in(15,20,25,30,40)
group by bo.user_id)v1
set ur.number_of_orders_by_user = v1.orders where v1.user_id = ur.user_id;

END |
delimiter ;

