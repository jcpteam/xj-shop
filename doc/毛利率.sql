CREATE TABLE as_order as 
SELECT
	t2.spu_no,
	t1.dept_id,
	sum( t3.sorting_weight ) mainTotalQuantiy,
	sum( t3.sorting_quantity ) subTotalQuantiy,
	sum( t2.sorting_price ) totalprice,
	avg( t2.sorting_price ) avgamount 
FROM
	store_goods_order t1,
	store_goods_order_item t2,
	store_sorting_detail t3 
WHERE
	t1.order_id = t2.order_id 
	AND t1.STATUS IN ( '3', '4' ) 
	AND t2.item_id = t3.order_item_id 
	AND t1.create_time >= '2021-09-16 00:00:00' 
	AND t1.create_time <= '2021-09-16 23:59:59' 
GROUP BY
	t2.spu_no,
	t1.dept_id,
	t3.sorting_quantity_unit
ORDER BY spu_no;


CREATE TABLE as_stock as 
SELECT
	t2.spu_no,
	t1.dept_id,

	sum( t2.quantity ) mainTotalQuantiy,
	sum( t2.stocks_number ) subTotalQuantiy,
	t2.amount price,
	sum( amount * quantity ) totalprice,
	avg( amount ) avgamount 
FROM
	store_warehouse_stock t1,
	store_warehouse_stock_item t2 
WHERE
	t1.stock_id = t2.stock_id 
	AND t2.status = '1' 
	AND t1.status = '1' 
	AND t1.create_time >= '2021-09-15 00:00:00' 
	AND t1.create_time <= '2021-09-15 23:59:59' 
	AND t1.warehouse_id = 6
GROUP BY
	spu_no,
	t1.dept_id,
	t1.warehouse_id
ORDER BY spu_no;


CREATE TABLE as_spu_no AS (
SELECT * FROM (
SELECT spu_no FROM as_order
UNION 
SELECT spu_no FROM as_stock
) TB
);


SELECT t1.spu_no, t4.`name`, IFNULL(t2.mainTotalQuantiy,0) mainTotalQuantiy,IFNULL(t2.subTotalQuantiy,0) subTotalQuantiy, IFNULL(t2.totalprice,0) totalprice, IFNULL(t3.mainTotalQuantiy, 0) stockMainTotalQuantiy, IFNULL(t3.subTotalQuantiy,0) stockSubTotalQuantiy, IFNULL(t3.price,0) price, IFNULL(t3.avgamount,0) avgprice,IFNULL(t3.totalprice,0) stockTotalPrice FROM as_spu_no T1
LEFT JOIN as_order T2 ON T1.spu_no = T2.spu_no
LEFT JOIN as_stock T3 ON T1.spu_no = T3.spu_no
JOIN store_goods_spu t4 ON t1.spu_no = t4.spu_no
ORDER BY t1.spu_no

