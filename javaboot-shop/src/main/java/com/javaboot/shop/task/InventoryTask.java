package com.javaboot.shop.task;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.domain.StoreMessage;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.mapper.StoreMessageMapper;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.service.IStoreWarehouseInventoryService;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("inventoryTask")
public class InventoryTask
{
    @Autowired
    private IStoreWarehouseInventoryService storeWarehouseInventoryService;

    @Autowired
    private IStoreHouseService storeHouseService;

    @Autowired
    private StoreMessageMapper storeMessageMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 定时任务更新昨天的上数
     */
    public void initInventory(String startAndEnd) {
        StoreHouse storeHouse=new StoreHouse();
        storeHouse.setStatus(Constants.NORMAL);
        List<StoreHouse> houseList = storeHouseService.selectStoreHouseList(storeHouse);
        houseList.forEach(house->{

            if(StringUtils.isNotBlank(startAndEnd)) {
               //if(house.getStoreId() == 14) {
                    Date start = DateUtils.parseDate(startAndEnd.split("~")[0]);
                    String end  = startAndEnd.split("~")[1];
                    // 计算两个时间的间隔
                    for(int i = 0; i < 31; i++){
                        String inventoryDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(start, i));
                        if(inventoryDate.equals(end)) {
                            break;
                        }
                        StoreWarehouseInventoryDTO dtoOne = new StoreWarehouseInventoryDTO();
                        dtoOne.setDeptId(house.getDeptId());
                        dtoOne.setWarehouseId(house.getStoreId());
                        dtoOne.setIntentoryDate(DateUtils.parseDate(inventoryDate));
                        storeWarehouseInventoryService.autoInventory(dtoOne);
                    }
              //}
            } else {
                StoreWarehouseInventoryDTO dto = new StoreWarehouseInventoryDTO();
                dto.setDeptId(house.getDeptId());
                dto.setWarehouseId(house.getStoreId());
                String yesterDay = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(new Date(), -1));
                dto.setIntentoryDate(DateUtils.parseDate(yesterDay));
                storeWarehouseInventoryService.autoInventory(dto);
            }
        });

        // 获取所有财务人员
        List<SysUser>  users = sysUserMapper.selectUserByFinance(null);
        users.forEach(user->{
            StoreMessage storeMessage = new StoreMessage();
            storeMessage.setReceiveId(user.getUserId());
            storeMessage.setContent("盘库操作执行完成！");
            storeMessage.setMessageKey("InventoryTaskFinish");
            storeMessage.setTitle("盘库操作执行完成");
            storeMessage.setMerchantId("0");
            storeMessageMapper.insertStoreMessage(storeMessage);
        });
    }

}
