/* this query find all orders placed by users of a certain offer */

select
  o.id, o.user_id, c.code, of.create_date, o.create_dt, os.name
from
  base_order o
  left join order_status os on o.order_status_id = os.id
  left join offer_instance of on o.offer_instance_id = of.id
  left join coupon c on c.id = of.coupon_id
where
  o.order_status_id in (30,40) and
  o.user_id in (select distinct o.user_id from offer_instance o left join coupon c on c.id = o.coupon_id where c.code like 'HKWIPRO%')
;

select
  count(o.id), sum(o.amount)
from
  base_order o
  left join offer_instance of on o.offer_instance_id = of.id
  left join coupon c on c.id = of.coupon_id
where
  o.order_status_id in (10, 15, 20, 30,40) and
  o.user_id in (select distinct o.user_id from offer_instance o where o.offer_id = 1238)
;

/* referral order count */
select
  count(o.id), sum(o.amount)
from
  base_order o
  left join offer_instance of on o.offer_instance_id = of.id
  left join coupon c on c.id = of.coupon_id
where
  o.order_status_id in (30,40) and
  o.user_id in (

select
  distinct o.user_id
from
  base_order o
  left join offer_instance of on o.offer_instance_id = of.id
  left join coupon c on c.id = of.coupon_id
  left join order_status os on o.order_status_id = os.id
  left join payment p on o.payment_id = p.id
where
  of.offer_id = 1128 and
  o.order_status_id in (30,40) and
  o.amount <= 425

)
;


/* find no of orders placed using a certain offer */
select
  o.id, o.amount, c.code, c.complimentary_coupon, os.name, p.payment_date
from
  base_order o
  left join offer_instance of on o.offer_instance_id = of.id
  left join coupon c on c.id = of.coupon_id
  left join order_status os on o.order_status_id = os.id
  left join payment p on p.id = o.payment_id
where
  of.offer_id = 1238;

/* query to find total value of orders places using a certain offer id */
select
  o.user_id
from
  base_order o
  left join offer_instance of on o.offer_instance_id = of.id
  left join coupon c on c.id = of.coupon_id
  left join order_status os on o.order_status_id = os.id
  left join payment p on o.payment_id = p.id
where
  of.offer_id = 1128 and
  o.order_status_id in (30,40) and
  o.amount <= 425;

and
  p.payment_date > "2011-12-01" and p.payment_date < "2012-01-01";


/* query to find lifetime value of customers who used a particular offer */
select
  sum(o.amount), count(o.id)
from
  base_order o
  left join offer_instance of on o.offer_instance_id = of.id
  left join coupon c on c.id = of.coupon_id
where
  o.order_status_id in (30,40) and
  o.user_id in (select distinct o.user_id from offer_instance o where o.offer_id = 1128)
;


/* find affiliate commission earned till a particular date (excluding already done payouts) */

WheyProtein.in

select
	(select sum(amount) from affiliate_txn where affiliate_id=10 and affiliate_txn_type_id=10 and date < "2011-11-09") -
	(select sum(amount) from affiliate_txn where affiliate_id=10 and affiliate_txn_type_id<>10 and date < "2011-11-09")
;

Desidime.com

select
	(select sum(amount) from affiliate_txn where affiliate_id=32 and affiliate_txn_type_id=10 and date < "2011-11-09") -
	(select sum(amount) from affiliate_txn where affiliate_id=32 and affiliate_txn_type_id<>10 and date < "2011-11-09")
;


/* find no of coupons used in fb viral coupon campaign */
select * from fbcoupon_coupon where coupon_use_count = 1 and fbcoupon_campaign_code = "lwdi-launch";
/*find no of new fbusers since a particular date */
select count(*) from fbcoupon_user where create_date > "2011-11-23 00:00:00";

/* find number of emails sent in email campaign */
select count(*) from emailer_history where email_campaign_id = 11;

select id, name, email, src_url, mobile, coupon_code, subscribe_mobile from discount_coupon_mailing_list where id > 2448 order by id desc limit 450;

/* Amazon feed for price update */
select id sku, hk_price price, (case out_of_stock when 0 then 1 when 1 then 0 end) quantity from product_variant where id in ();

/* callback number for counselling */
select id, name, email, src_url, mobile, coupon_code, subscribe_mobile from discount_coupon_mailing_list where id > 2903 order by id desc limit 450;

/* no of new ordering users in a given time period */
SELECT
  count(distinct bo.user_id)
FROM
  base_order bo, payment p
WHERE
  bo.order_status_id IN ( 20, 30, 40 )
  AND bo.user_id not in (
    SELECT distinct bo.user_id
    FROM base_order bo, payment p
    WHERE bo.order_status_id IN ( 20, 30, 40 ) AND p.id = bo.payment_id AND p.payment_date < '2012-01-01'
  )
  AND p.id = bo.payment_id
  AND p.payment_date >= '2012-01-01' AND p.payment_date < '2012-02-01';

/* random order selection script */
drop view bday_orders;
create view bday_orders
as
select
	o.id, o.amount, u.email, u.name, p.payment_date, hour(p.payment_date) hr
from
	base_order o
	left join user u on o.user_id = u.id
	left join payment p on p.id = o.payment_id
where
	p.payment_date >= "2012-03-01" and p.payment_date < "2012-03-02"
	and o.order_status_id in (20,30,40)
	and p.payment_mode_id in (15, 60)
order by hr
;
SELECT tmp.id, tmp.amount, tmp.email, tmp.name, tmp.hr hours
FROM bday_orders
LEFT JOIN (SELECT * FROM bday_orders ORDER BY RAND()) tmp ON (bday_orders.hr = tmp.hr)
GROUP BY tmp.hr
ORDER BY bday_orders.hr;

/* command to put list of all user email ids in a file */
mysql -h 172.16.3.50 -u hkadmin -p -e 'select u.email from healthkart_prod.user u left join healthkart_prod.user_has_role r on u.id = r.user_id where r.role_name = "HKUNVERIFIED" or r.role_name = "HK_USER"' > beautyMailerList.txt


/* new users */
SELECT count(distinct bo.user_id )
FROM base_order bo, payment p
WHERE
	bo.order_status_id IN ( 20, 30, 40 )
	AND p.id = bo.payment_id
	AND p.payment_date >= '2012-06-01'
	AND p.payment_date < '2012-06-11'
	AND bo.user_id NOT IN (
		SELECT distinct bo.user_id FROM base_order bo, payment p WHERE bo.order_status_id IN ( 20, 30, 40 )
		AND p.id = bo.payment_id AND p.payment_date < '2012-06-01'
	)


SELECT count(distinct user_id )
FROM
(
SELECT distinct bo.user_id, min(ifnull(p.payment_date, bo.create_dt)) as first_order_date
FROM base_order bo
left outer join payment p on p.id = bo.payment_id
WHERE bo.order_status_id
IN ( 20, 30, 40 )
group by user_id
) v1
where date(first_order_date)>= '2013-02-16'
AND date(first_order_date) <= '2013-02-22';


/*
 * groovy program to subtract two email lists


String largeFilePath = "C:/Dev/IdeaProjects/beautyMailerList.txt"
String smallFilePath = "C:/Dev/IdeaProjects/servicesMailer.txt"

File largeFile = new File(largeFilePath)
File smallFile = new File(smallFilePath)

List emailListLarge = [];
List emailListSmall = [];

largeFile.eachLine {emailListLarge.add(it)};
smallFile.eachLine {emailListSmall.add(it)};

List emailListFinal = emailListLarge - emailListSmall;

print emailListFinal.size();

File finalList = new File("C:/Dev/IdeaProjects/beautyMailerFinal.txt")
emailListFinal.each {
    finalList.append(it + "\n")
}



  */