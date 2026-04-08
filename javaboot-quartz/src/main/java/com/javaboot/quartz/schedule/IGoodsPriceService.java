package com.javaboot.quartz.schedule;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * DATA-Group
 * 2021/7/15 1:03
 **/


@Mapper
public interface IGoodsPriceService {

    @Delete(" delete from store_price_setting where opt_date =DATE_FORMAT(NOW(),'%Y-%m-%d')")
    int  deleteTodayPriceSetting();


    @Select(" select count(1) from store_price_setting where opt_date =DATE_FORMAT(NOW(),'%Y-%m-%d') and status=2")
    int  selectTodayPriceSetting();

    @Delete(" delete from store_price_setting_detail where setting_date =DATE_FORMAT(NOW(),'%Y-%m-%d')")
    int  deleteTodayPriceSettingDetail();

    @Insert("insert into store_price_setting(dept_id,opt_date,status,submit_status)  select  dept_id ,DATE_FORMAT(NOW(),'%Y-%m-%d') as opt_date,'0' as status,'0' as submit_status from sys_dept  where dept_id  in ( select dept_id from store_goods_quotation)")
    int  insertTodayPriceSetting();


    @Insert(" insert into store_price_setting_detail (price_id,spu_id,dept_id,unit_id ,price,setting_date,goods_id) " +
            "                 select a.setting_id as price_id ,c.spu_no as spu_id,a.dept_id ,sgsu.main_unit_id as unit_id ,ifnull( t5.price,c.price) as price ,a.opt_date as setting_date ,c.goods_id from store_price_setting a  " +
            "                 left join store_goods_quotation b on a.dept_id =b.dept_id   " +
            "                 left join store_goods_quotation_goods c on b.quotation_id=c.quotation_id  " +
            "                 left join store_goods_spu_unit sgsu on c.spu_no =sgsu.spu_no  " +
            "                 left join (select p2.price_id,p2.price,p2.goods_id from store_price_setting p1     " +
            "                 left join store_price_setting_detail p2 on p1.setting_id =p2.price_id where  p1.status='2') t5  " +
            "                 on t5.goods_id=c.goods_id   and b.status=1 and c.status !='0'  " +
            "                 where a.opt_date =DATE_FORMAT(NOW(),'%Y-%m-%d') and c.spu_no is not null and c.status!='0'")
    int  insertTodayPriceSettingDetail();
}
