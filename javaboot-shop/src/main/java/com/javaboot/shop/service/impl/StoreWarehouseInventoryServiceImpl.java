package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreWarehouseInventory;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.mapper.StoreWarehouseInventoryItemMapper;
import com.javaboot.shop.mapper.StoreWarehouseInventoryMapper;
import com.javaboot.shop.service.IStoreWarehouseInventoryItemService;
import com.javaboot.shop.service.IStoreWarehouseInventoryService;
import com.javaboot.shop.vo.StoreWarehouseInventoryVO;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品盘库Service业务层处理
 *
 * @author lqh
 * @date 2021-05-23
 */
@Service
public class StoreWarehouseInventoryServiceImpl implements IStoreWarehouseInventoryService {
    @Autowired
    private StoreWarehouseInventoryMapper storeWarehouseInventoryMapper;

    @Autowired
    private StoreWarehouseInventoryItemMapper storeWarehouseInventoryItemMapper;

    @Autowired
    private IStoreWarehouseInventoryItemService itemService;

    /**
     * 查询商品盘库
     *
     * @param inventoryId 商品盘库ID
     * @return 商品盘库
     */
    @Override
    public StoreWarehouseInventoryVO selectStoreWarehouseInventoryById(Long inventoryId) {
        StoreWarehouseInventoryVO vo = storeWarehouseInventoryMapper.selectStoreWarehouseInventoryById(inventoryId);
        // 获取明细
        StoreWarehouseInventoryItem item = new StoreWarehouseInventoryItem();
        item.setInventoryId(inventoryId);
        item.setStatus(Constants.NORMAL);
        vo.setItemList(itemService.selectStoreWarehouseInventoryItemList(item));
        return vo;
    }

    /**
     * 新增商品盘库
     *
     * @param storeWarehouseInventory 商品盘库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreWarehouseInventory(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        storeWarehouseInventory.setCreateTime(DateUtils.getNowDate());
        storeWarehouseInventory.setInventoryNo(CodeConstants.INVENTORY+DateUtils.getRandom());
        storeWarehouseInventory.setCreatorId(user.getUserId());
        storeWarehouseInventory.setStatus(Constants.NORMAL);

        initStoreWarehouseInventory(storeWarehouseInventory);
        int result = storeWarehouseInventoryMapper.insertStoreWarehouseInventory(storeWarehouseInventory);
        storeWarehouseInventory.getItemList().forEach(o->{
            o.setInventoryId(storeWarehouseInventory.getInventoryId());
            o.setStatus(Constants.NORMAL);
            itemService.insertStoreWarehouseInventoryItem(o);
        });
        return result;
    }

    /**
     * 查询商品盘库列表
     *
     * @param storeWarehouseInventory 商品盘库
     * @return 商品盘库
     */
    @Override
    public List<StoreWarehouseInventoryVO> selectStoreWarehouseInventoryList(StoreWarehouseInventory storeWarehouseInventory) {
        return storeWarehouseInventoryMapper.selectStoreWarehouseInventoryList(storeWarehouseInventory);
    }

    /**
     * 计算盘点商品
     * 首次盘库（从金蝶同步过来）
     * 取金蝶的盘库数量 ， 盘库成本价
     * <p>
     * 后续盘库
     * 上次盘库的盘库数量（初始库存） + 入库单数量 = 库存数量
     * 报损或报溢:  库存数量 - 盘点数量 - 订单数量
     * 盘点后价格：  上次盘库的盘库数量 * 盘库成本价 + 入库单数量 * 入库单价格 = 总成本金额
     * 盘点成本单价 ： 总成本金额 / （盘点数量 + 订单数量）
     *
     * @param storeWarehouseInventory
     */
    @Override
    public  List<StoreWarehouseInventoryItem> calculationInventoryGoods(StoreWarehouseInventoryDTO storeWarehouseInventory) {

//        if("040802".equals(storeWarehouseInventory.getDeptId())) {
//            System.out.println("debug====");
//        }
        String inventoryYesterday = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(new Date(), -1));
        StoreWarehouseInventoryVO lastStoreInfo;

        if(storeWarehouseInventory.getInventoryId() != null) {
            // 修改的时候传盘库的id，获取盘库时存储到数据库的开始时间
            lastStoreInfo = storeWarehouseInventoryMapper.selectStoreWarehouseInventoryById(storeWarehouseInventory.getInventoryId());
            if(lastStoreInfo.getIntentorySrcId() != null) {
                lastStoreInfo = storeWarehouseInventoryMapper.selectStoreWarehouseInventoryById(lastStoreInfo.getIntentorySrcId());
            }
            inventoryYesterday = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, lastStoreInfo.getIntentoryDate());
        } else {
            //查询该仓库上次盘库信息
            lastStoreInfo = storeWarehouseInventoryMapper.selectLastStoreWarehouseInventoryByWarehouseId(storeWarehouseInventory.getWarehouseId(), storeWarehouseInventory.getIntentoryDate());
            if(lastStoreInfo != null) {
                inventoryYesterday = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(lastStoreInfo.getIntentoryDate(), 1));
            }
        }

        String inventoryDateEnd = storeWarehouseInventory.getIntentoryDate() == null ? inventoryYesterday : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, storeWarehouseInventory.getIntentoryDate());
        // 订单日期从9点到第二天9点
        storeWarehouseInventory.setStartTime(inventoryYesterday.concat(" 09:00:00"));
        // 从来没有进行盘库，最近的盘库记录为空，只计算最后截止时间段内的订单，入库单数据
        if(lastStoreInfo == null){
            storeWarehouseInventory.setStartTime(inventoryDateEnd.concat(" 09:00:00"));
        }
        String orderEndTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(DateUtils.parseDate(inventoryDateEnd), 1));
        storeWarehouseInventory.setEndTime(orderEndTime.concat(" 09:00:00"));
        // 获取上次盘库到当前盘库时间段内，选中仓库的订单商品主单位和副单位的数量，金额
        List<StoreWarehouseInventoryItem> orderInventoryList = storeWarehouseInventoryItemMapper.selectSpuOrderInventory(storeWarehouseInventory);

        //        // 根据主单位和副单位换算关系计算对应的数量，主副单位不存在的时候，取默认的换算关系, 默认关系在SPU商品设置单位的时候初始化进去
        //        List<StoreGoodsSpuVO> conversionList = storeWarehouseInventoryItemMapper.selectSpuUnitConversion(storeWarehouseInventory);
        //        if(CollectionUtils.isNotEmpty(orderInventoryList) && CollectionUtils.isNotEmpty(conversionList)) {
        //            orderInventoryList.forEach(o1-> conversionList.forEach(o2->{
        //                if(o1.getSpuNo().equals(o2.getSpuNo()) && o1.getUnit().equals(o2.getMainUnitId())) {
        //                    o1.setStockLonelyQuantity(o1.getStockQuantity() * o2.getMainCaseSub());
        //                }
        //                if(o1.getSpuNo().equals(o2.getSpuNo()) && o1.getUnit().equals(o2.getSubUnitId())) {
        //                    o1.setStockQuantity(o1.getStockLonelyQuantity() * o2.getSubCaseMain());
        //                }
        //            }));
        //        }

        storeWarehouseInventory.setStartTime(inventoryYesterday.concat(" 00:00:00"));
        if(lastStoreInfo == null){
            storeWarehouseInventory.setStartTime(inventoryDateEnd.concat(" 00:00:00"));
        }
        storeWarehouseInventory.setEndTime(inventoryDateEnd.concat(" 23:59:59"));
        // 获取上次盘库到当前盘库时间段内，选中仓库的入库单主单位和副单位的数量
        List<StoreWarehouseInventoryItem> warehouseStockInventoryList =  storeWarehouseInventoryItemMapper.selectWarehouseStockInventory(storeWarehouseInventory);

        List<StoreWarehouseInventoryItem> lastInventoryList = new ArrayList<>();
        if(lastStoreInfo!=null){
            //上次盘库的商品，获取商品的期末库存
            StoreWarehouseInventoryItem queryItem = new StoreWarehouseInventoryItem();
            queryItem.setStatus(Constants.NORMAL);
            queryItem.setInventoryId(lastStoreInfo.getInventoryId());
            lastInventoryList = itemService.selectStoreWarehouseInventoryItemList(queryItem);
        }


        List<StoreWarehouseInventoryItem> spuInventoryList = storeWarehouseInventory.getItemList();
        if(CollectionUtils.isEmpty(spuInventoryList))
        {
            spuInventoryList = storeWarehouseInventoryItemMapper.selectInventorySpus(storeWarehouseInventory);
        }

        if(CollectionUtils.isNotEmpty(spuInventoryList))
        {
            for(StoreWarehouseInventoryItem o : spuInventoryList) {

                o.setStockQuantity(0.0);
                o.setStockLonelyQuantity(0.0);
                o.setPrice(0.0);
                o.setInventoryId(lastStoreInfo != null ? lastStoreInfo.getInventoryId() : null);

                if(CollectionUtils.isNotEmpty(lastInventoryList)){
                    for (StoreWarehouseInventoryItem o1 : lastInventoryList)
                    {
                        if (o.getSpuNo().equals(o1.getSpuNo()))
                        {
                            o.setStockQuantity(o1.getInventoryQuantity() != null ? o1.getInventoryQuantity() : 0.0);
                            o.setStockLonelyQuantity(
                                o1.getInventoryLonelyQuantity() != null ? o1.getInventoryLonelyQuantity() : 0.0);
                            o.setPrice((o1.getPrice() != null && o1.getPrice() > 0)? o1.getPrice() : 0.0);

                        }
                    }
                }

                if(CollectionUtils.isNotEmpty(warehouseStockInventoryList))
                {
                    warehouseStockInventoryList.forEach(o1 -> {
                        if (o.getSpuNo().equals(o1.getSpuNo()))
                        {
                            o.setStockQuantity(o.getStockQuantity() + o1.getStockQuantity());
                            o.setStockLonelyQuantity(o.getStockLonelyQuantity() + o1.getStockLonelyQuantity());
                            o.setPrice(o.getPrice() + o1.getPrice());
                            o.setStorageAmount(o1.getStorageAmount());
                        }
                    });
                }

                if(CollectionUtils.isNotEmpty(orderInventoryList)){

                    orderInventoryList.forEach(o1->{
                        if(o.getSpuNo().equals(o1.getSpuNo())){
                            o.setStockQuantity(o.getStockQuantity() - o1.getSalesQuantity());
                            o.setStockLonelyQuantity(o.getStockLonelyQuantity() - o1.getSalesLonelyQuantity());
                            o.setSalesQuantity(o1.getSalesQuantity());
                            o.setSalesLonelyQuantity(o1.getSalesLonelyQuantity());
                            o.setPrice(o.getPrice() - o1.getPrice());
                            o.setSalesAmount(o1.getSalesQuantity());
                        }
                    });
                }

                // 格式化数据
                if(o.getStockQuantity() != null){
                    o.setStockQuantity(new BigDecimal(o.getStockQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if(o.getStockLonelyQuantity() != null){
                    o.setStockLonelyQuantity(new BigDecimal(o.getStockLonelyQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if(o.getSalesQuantity() != null){
                    o.setSalesQuantity(new BigDecimal(o.getSalesQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if(o.getSalesLonelyQuantity() != null){
                    o.setSalesLonelyQuantity(new BigDecimal(o.getSalesLonelyQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if(o.getStorageAmount() != null) { // 入库单平均单价
                    o.setStorageAmount(new BigDecimal(o.getStorageAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if(o.getSalesAmount() != null) { // 订单平均单价
                    o.setSalesAmount(new BigDecimal(o.getSalesAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if(o.getPrice() != null){
                    o.setPrice(new BigDecimal(o.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
        }

        return spuInventoryList;
    }

    @Override
    public List<StoreWarehouseInventoryItem> initInventorySpus(StoreWarehouseInventoryDTO storeWarehouseInventory)
    {
        List<StoreWarehouseInventoryItem> allInventorySpus = storeWarehouseInventoryItemMapper.selectAllInventorySpus(storeWarehouseInventory);
//        List<StoreWarehouseInventoryItem> inventoryItems = calculationInventoryGoods(storeWarehouseInventory);
//        allInventorySpus.forEach(o-> inventoryItems.forEach(o1->{
//            if(o.getSpuNo().equals(o1.getSpuNo()) && o.getUnit().equals(o1.getUnit())){
//                o.setStockQuantity(o1.getStockQuantity());
//            }
//        }));
        return allInventorySpus;
    }

    /**
     * 修改商品盘库
     *
     * @param storeWarehouseInventory 商品盘库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreWarehouseInventory(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        // 2022-01-22修改去掉重新计算，不在计算库存数量
        // initStoreWarehouseInventory(storeWarehouseInventory);
        // 计算盘存数量
        Double quantity = 0.0;

        // 手工修改后，自动盘库不覆盖
        storeWarehouseInventory.setReplaceFlag("0");
        itemService.deleteStoreWarehouseInventoryItemByInventoryId(storeWarehouseInventory.getInventoryId());
        for (StoreWarehouseInventoryItem e : storeWarehouseInventory.getItemList())
        {
            e.setInventoryId(storeWarehouseInventory.getInventoryId());
            e.setStatus(Constants.NORMAL);
            quantity += e.getInventoryQuantity();
            itemService.insertStoreWarehouseInventoryItem(e);
        }
        // 设置盘存数量
        storeWarehouseInventory.setQuantity(quantity);
        int result = storeWarehouseInventoryMapper.updateStoreWarehouseInventory(storeWarehouseInventory);
        return result;
    }

    /**
     * 删除商品盘库对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreWarehouseInventoryByIds(String ids) {
        return storeWarehouseInventoryMapper.deleteStoreWarehouseInventoryByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品盘库信息
     *
     * @param inventoryId 商品盘库ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreWarehouseInventoryById(Long inventoryId) {
        return storeWarehouseInventoryMapper.deleteStoreWarehouseInventoryById(inventoryId);
    }

    private StoreWarehouseInventoryDTO initStoreWarehouseInventory(StoreWarehouseInventoryDTO storeWarehouseInventory)
    {
        List<StoreWarehouseInventoryItem> dbInventoryItem = calculationInventoryGoods(storeWarehouseInventory);
        Double quantity = 0.0;
        Double lonelyNumber = 0.0;
        Double totalAmount = 0.0;
        for(StoreWarehouseInventoryItem e : storeWarehouseInventory.getItemList()){
            for(StoreWarehouseInventoryItem dbObj : dbInventoryItem) {
                if (e.getSpuNo().equals(dbObj.getSpuNo()))
                {
                    storeWarehouseInventory.setIntentorySrcId(dbObj.getInventoryId());
                    //  计算得出的库存数量
                    e.setStockQuantity(dbObj.getStockQuantity());
                    e.setStockLonelyQuantity(dbObj.getStockLonelyQuantity());
                    // 损益数量
                    e.setInventoryResult(new BigDecimal(e.getInventoryQuantity() - dbObj.getStockQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    // 盘点状态:0-盘亏,1-盘赢
                    e.setInventoryStatus("0");
                    e.setStorageAmount(dbObj.getStorageAmount());
                    e.setSalesQuantity(dbObj.getSalesQuantity());
                    e.setSalesLonelyQuantity(dbObj.getSalesLonelyQuantity());
                    e.setSalesAmount(dbObj.getSalesAmount());
                    e.setPrice(dbObj.getPrice());
                    if(e.getInventoryResult() >= 0) {
                        e.setInventoryStatus("1");
                    }
                    quantity += e.getInventoryQuantity();
                    lonelyNumber += e.getInventoryLonelyQuantity();
                    totalAmount += e.getPrice();
                }
            }
        }
        storeWarehouseInventory.setQuantity(new BigDecimal(quantity).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        storeWarehouseInventory.setLonelyNumber(new BigDecimal(lonelyNumber).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        storeWarehouseInventory.setAmount(new BigDecimal(totalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        return storeWarehouseInventory;
    }

    /**
     * 仓库最近一次盘库时间
     * @param houseId
     * @return
     */
    @Override
    public String lastInventoryDate(Long houseId) {
        //查询该仓库上次盘库信息
        StoreWarehouseInventoryVO lastStoreInfo = storeWarehouseInventoryMapper.selectLastStoreWarehouseInventoryByWarehouseId(houseId, new Date());
        String inventoryDateStart = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(new Date(), -1));

        if(lastStoreInfo != null) {
            inventoryDateStart = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(lastStoreInfo.getIntentoryDate(), 1));
        }

        return inventoryDateStart;
    }

    /**
     * 自动盘库
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoInventory(StoreWarehouseInventoryDTO dto) {

        StoreWarehouseInventoryDTO storeWarehouseInventory = new StoreWarehouseInventoryDTO();
        storeWarehouseInventory.setCreateTime(DateUtils.getNowDate());
        storeWarehouseInventory.setIntentoryDate(dto.getIntentoryDate());
        storeWarehouseInventory.setInventoryNo(CodeConstants.INVENTORY + DateUtils.getRandom());
        storeWarehouseInventory.setCreatorId("1");
        storeWarehouseInventory.setStatus(Constants.NORMAL);
        storeWarehouseInventory.setWarehouseId(dto.getWarehouseId());
        storeWarehouseInventory.setDeptId(dto.getDeptId());

        List<StoreWarehouseInventoryItem> dbInventoryItem = calculationInventoryGoods(dto);
        // 没有库存商品不自动盘库
        if(CollectionUtils.isEmpty(dbInventoryItem)) {
            return 0;
        }

        // 获取传入盘库日期手工修改后的盘库数据
        List<StoreWarehouseInventoryItem> reqInventoryDateItemList = new ArrayList<>();
        QueryWrapper<StoreWarehouseInventory> queryWrapper = new QueryWrapper();
        queryWrapper.eq("intentory_date", dto.getIntentoryDate());
        queryWrapper.eq("warehouse_id", dto.getWarehouseId());
        queryWrapper.eq("status", Constants.NORMAL);
        queryWrapper.eq("replace_flag", "0");
        List<StoreWarehouseInventory> reqInventoryDateList = storeWarehouseInventoryMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(reqInventoryDateList)){
            StoreWarehouseInventoryItem queryItem = new StoreWarehouseInventoryItem();
            queryItem.setStatus(Constants.NORMAL);
            queryItem.setInventoryId(reqInventoryDateList.get(0).getInventoryId());
            reqInventoryDateItemList = itemService.selectStoreWarehouseInventoryItemList(queryItem);

            storeWarehouseInventory.setReplaceFlag("0");
        }

        // 定时任务计算商品列表与原数据库的商品列表不一致，需要将用户新增的数据添加都定时任务计算的商品列表中
        for (StoreWarehouseInventoryItem reqObj : reqInventoryDateItemList) {
            boolean exist = false;
            for(StoreWarehouseInventoryItem dbObj : dbInventoryItem){
                if(reqObj.getSpuNo().equals(dbObj.getSpuNo())
                    && "0".equals(reqInventoryDateList.get(0).getReplaceFlag())){
                    exist = true;
                }
            }
            if(!exist) {
                reqObj.setPrice(0.0);
                dbInventoryItem.add(reqObj);
            }
        }

        Double quantity = 0.0;
        Double lonelyNumber = 0.0;
        Double totalAmount = 0.0;
        for(StoreWarehouseInventoryItem dbObj : dbInventoryItem){

            storeWarehouseInventory.setIntentorySrcId(dbObj.getInventoryId());
            //  计算得出的库存数量,填充盘库数量和只数
            if(dbObj.getInventoryQuantity() == null) {
                dbObj.setInventoryQuantity(dbObj.getStockQuantity());
            }
            if(dbObj.getInventoryLonelyQuantity() == null) {
                dbObj.setInventoryLonelyQuantity(dbObj.getStockLonelyQuantity());
            }

            // 损益数量
            dbObj.setInventoryResult(0.0);

            // 手动盘库与计算出来的不一致时，取手动修改后的数据
            reqInventoryDateItemList.forEach(inventoryObj->{
                if(inventoryObj.getSpuNo().equals(dbObj.getSpuNo())
                    && "0".equals(reqInventoryDateList.get(0).getReplaceFlag())){
                    dbObj.setInventoryQuantity(inventoryObj.getInventoryQuantity());
                    dbObj.setInventoryLonelyQuantity(inventoryObj.getInventoryLonelyQuantity());
                    // 重新计算损益主数量
                    dbObj.setInventoryResult(new BigDecimal(
                        (inventoryObj.getInventoryQuantity()== null ? 0.0 : inventoryObj.getInventoryQuantity())
                            - (dbObj.getStockQuantity() == null ? 0.0 : dbObj.getStockQuantity())).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            });

            // 盘点状态:0-盘亏,1-盘赢
            dbObj.setInventoryStatus("1");

            quantity += dbObj.getInventoryQuantity();
            lonelyNumber += dbObj.getInventoryLonelyQuantity();
            totalAmount += dbObj.getPrice();

        }
        storeWarehouseInventory.setQuantity(quantity);
        storeWarehouseInventory.setLonelyNumber(lonelyNumber);
        storeWarehouseInventory.setAmount(totalAmount);

        QueryWrapper<StoreWarehouseInventory> tempQuery = new QueryWrapper<>();
        tempQuery.eq("warehouse_id", dto.getWarehouseId());
        tempQuery.eq("intentory_date", dto.getIntentoryDate());
        StoreWarehouseInventory updateObj = new StoreWarehouseInventory();
        updateObj.setStatus("0");
        storeWarehouseInventoryMapper.update(updateObj, tempQuery);
        int result = storeWarehouseInventoryMapper.insertStoreWarehouseInventory(storeWarehouseInventory);
        dbInventoryItem.forEach(o->{
            o.setInventoryId(storeWarehouseInventory.getInventoryId());
            o.setStatus(Constants.NORMAL);
            itemService.insertStoreWarehouseInventoryItem(o);
        });

        return result;
    }

}
