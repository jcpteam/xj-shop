package com.javaboot.shop.task;

import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreOrderPassword;
import com.javaboot.shop.service.IStoreOrderPasswordService;
import com.javaboot.shop.service.IStoreSaleSettingService;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.vo.SysDeptSelectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 *  上数定时任务
 */
@Component("saleSettingTask")
public class SaleSettingTask
{

    @Autowired
    private ISysDeptService deptService;


    @Autowired
    private IStoreSaleSettingService storeSaleSettingService;

    @Autowired
    private IStoreOrderPasswordService storeOrderPasswordService;

    /**
     * 定时任务上数设置
     */
    public void initSettingTask(){
        List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
        deptList.forEach(o->{
            storeSaleSettingService.initSaleData(o.getValue(), DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        });

    }

    /**
     * 定时生成订单密码
     */
    public void initOrderPasswordTask(){
        List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
        storeOrderPasswordService.deleteStoreOrderPasswordById(0L);
        deptList.forEach(o->{
            StoreOrderPassword storeOrderPassword = new StoreOrderPassword();
            storeOrderPassword.setDeptId(o.getValue());
            storeOrderPassword.setPassword(getRandom());
            storeOrderPassword.setStatus("1");
            storeOrderPasswordService.insertStoreOrderPassword(storeOrderPassword);
        });
    }

    private String getRandom() {
        Random r = new Random();
        int ran = r.nextInt(999999);
        StringBuilder ranText=new StringBuilder(ran);
        ranText.append(ran);
        if(ranText.length()<6){
            int length=ranText.length();
            for (int i = 0; i < 6-length; i++) {
                ranText.insert(0,"0");
            }
        }
        return ranText.toString();
    }
}
