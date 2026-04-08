package com.javaboot.shop.task;

import com.javaboot.shop.domain.StoreMessage;
import com.javaboot.shop.dto.AsOrderStockDTO;
import com.javaboot.shop.mapper.StoreMessageMapper;
import com.javaboot.shop.service.IAsOrderStockService;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("stockAndOrderStatTask")
public class StockAndOrderStatTask
{

    @Autowired
    private IAsOrderStockService asOrderStockService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private StoreMessageMapper storeMessageMapper;

    public void stat(String start){
        AsOrderStockDTO asOrderStock = new AsOrderStockDTO();
        asOrderStock.setQryDate(start);
        asOrderStockService.insertAsOrderStock(asOrderStock);

        // 获取所有财务人员
        List<SysUser> users = sysUserMapper.selectUserByFinance(null);
        users.forEach(user->{
            StoreMessage storeMessage = new StoreMessage();
            storeMessage.setReceiveId(user.getUserId());
            storeMessage.setContent("统计中间表操作执行完成！");
            storeMessage.setMessageKey("InventoryTaskFinish");
            storeMessage.setTitle("统计中间表操作执行完成");
            storeMessage.setMerchantId("0");
            storeMessageMapper.insertStoreMessage(storeMessage);
        });
    }
}
