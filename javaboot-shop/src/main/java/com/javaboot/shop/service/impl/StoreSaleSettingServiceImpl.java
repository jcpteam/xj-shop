package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.json.JSONObject;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsQueryDTO;
import com.javaboot.shop.dto.StoreSaleSettingDTO;
import com.javaboot.shop.mapper.*;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品上数设置Service业务层处理
 *
 * @author lqh
 * @date 2021-05-19
 */
@Service
public class StoreSaleSettingServiceImpl extends ServiceImpl<StoreSaleSettingMapper, StoreSaleSetting> implements IStoreSaleSettingService {

    private static final Logger logger = LoggerFactory.getLogger(StoreSaleSettingServiceImpl.class);

    @Autowired
    private StoreGoodsQuotationGoodsMapper storeGoodsQuotationGoodsMapper;

    @Autowired
    private StoreSaleSettingMapper storeSaleSettingMapper;

    @Autowired
    private StoreWarehouseInventoryItemMapper storeWarehouseInventoryItemMapper;

    @Autowired
    private StoreGoodPurchaseItemMapper storeGoodPurchaseItemMapper;

    @Autowired
    private StoreWarehouseStockItemMapper storeWarehouseStockItemMapper;

    @Autowired
    private IStoreGoodsQuotationGoodsService storeGoodsQuotationGoodsService;

    @Autowired
    private IStoreSaleSettingLogService storeSaleSettingLogService;

    @Autowired
    private StoreGoodsOrderItemMapper storeGoodsOrderItemMapper;

    @Autowired
    private StoreGoodsSpuUnitMapper storeGoodsSpuUnitMapper;

    @Autowired
    private IStoreGoodsSpuUnitService storeGoodsSpuUnitService;

    @Autowired
    private IStoreGoodsSalesUnitService storeGoodsSalesUnitService;

    /**
     * 查询商品上数设置
     *
     * @param settingid 商品上数设置ID
     * @return 商品上数设置
     */
    @Override
    public StoreSaleSetting selectStoreSaleSettingById(Long settingid) {
        return this.storeSaleSettingMapper.selectById(settingid);
    }

    /**
     * 查询商品上数设置列表
     *
     * @param storeSaleSetting 商品上数设置
     * @return 商品上数设置
     */
    @Override
    public List<StoreSaleSetting> selectStoreSaleSettingList(StoreSaleSetting storeSaleSetting) {
        List<StoreSaleSetting> list = storeSaleSettingMapper.selectStoreSaleSettingList(storeSaleSetting);

        // 获取订单的数量
        if(CollectionUtils.isNotEmpty(list)) {
            Set<String> dateList = new HashSet<>();
            Set<String> deptIdList = new HashSet<>();
            List<StoreGoodsOrderItem> orderNumList = new ArrayList<>();
            StoreGoodsSpuUnit spuUnit = new StoreGoodsSpuUnit();
            spuUnit.setSpuNoList(new ArrayList<>());
            // 获取上数列表中的上数时间和区域
            list.forEach(o->{
                String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getCreateTime());
                dateList.add(dateStr);
                deptIdList.add(o.getDeptId());
                if(!spuUnit.getSpuNoList().contains(o.getSpuNo())) {
                    spuUnit.getSpuNoList().add(o.getSpuNo());
                }
            });

            // 根据区域和时间查询对应的商品SPU订单数量
            deptIdList.forEach(deptId -> {

                dateList.forEach(dateStr -> {

                    String start = dateStr.concat(" 09:00:00");
                    String end = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(DateUtils.parseDate(dateStr), 1)).concat(" 09:00:00");

                    StoreGoodsOrderItem queryObj3 = new StoreGoodsOrderItem();
                    queryObj3.setDeptId(deptId);
                    queryObj3.setStartTime(start);
                    queryObj3.setEndTime(end);

                    // 商品的订单数量
                    List<StoreGoodsOrderItem> orderItemList = storeGoodsOrderItemMapper.selectOrderSpuConversionNum(queryObj3);
                    if (CollectionUtils.isNotEmpty(orderItemList))
                    {
                        orderItemList.forEach(order->{
                            order.setStartTime(dateStr);
                            order.setDeptId(deptId);
                            orderNumList.add(order);
                        });
                    }
                });
            });

            // 设置商品单位
            List<StoreGoodsSpuUnit> spuUnits = storeGoodsSpuUnitService.selectStoreGoodsSpuUnitList(spuUnit);
            if (CollectionUtils.isNotEmpty(spuUnits)) {
                List<StoreGoodsSalesUnit> salesUnits = storeGoodsSalesUnitService.getNormalSpecificationsList();
                if (CollectionUtils.isNotEmpty(salesUnits)) {
                    spuUnits.forEach(s -> {
                        StoreGoodsSalesUnit mainUnit =  salesUnits.stream().filter(u -> u.getUnitId().equals(s.getMainUnitId())).findFirst().orElse(null);
                        StoreGoodsSalesUnit subUnit =  salesUnits.stream().filter(u -> u.getUnitId().equals(s.getSubUnitId())).findFirst().orElse(null);
                        list.stream().filter(o -> o.getSpuNo().equals(s.getSpuNo())).forEach(o->{
                            o.setSpecifications(subUnit.getName());
                            o.setMainUnitId(mainUnit.getUnitId());
                            o.setSubUnitId(subUnit.getUnitId());
                        });
                    });
                }
            }

            // 填充上数列表中订单数量
            if(CollectionUtils.isNotEmpty(orderNumList)) {
                list.forEach(o->{
                    o.setOrderQuanintiy(0.0);
                    String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, o.getCreateTime());
                    orderNumList.forEach(order->{
                        order.setQuantity(order.getQuantity() != null ? order.getQuantity() : 0.0);
                        if(order.getStartTime().equals(dateStr) && order.getDeptId().equals(o.getDeptId())
                                && order.getSpuNo().equals(o.getSpuNo())) {

                            o.setOrderQuanintiy(new BigDecimal(o.getOrderQuanintiy()).add(new BigDecimal(order.getQuantity())).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());

                        }
                    });

                });
            }


        }

        return list;
    }

    /**
     * 上数SPU商品详情
     * @param dto
     * @return
     */
    @Override
    public List<StoreGoodsQuotationGoodsVO>  quotationGoodsList(StoreGoodsQuotationGoodsQueryDTO dto) {
        List<StoreGoodsQuotationGoodsVO> list = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsList(dto);

        // 获取订单的数量
        if(CollectionUtils.isNotEmpty(list)) {

            List<StoreGoodsSpuUnit> spuUnits = storeGoodsSpuUnitMapper.selectUnitAndCoversionByDeptId(dto.getDeptId());
            Map<String, StoreGoodsSpuUnit> spuUnitMap = spuUnits.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));
            list.forEach(o-> {
                o.setMainCaseSub(spuUnitMap.get(o.getSpuNo()).getMainCaseSub());
            });

            StoreGoodsSpuUnit spuUnit = new StoreGoodsSpuUnit();
            spuUnit.setSpuNoList(new ArrayList<>());
            spuUnit.getSpuNoList().add(dto.getSpuNo());

            String deptId = dto.getDeptId();


            String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, dto.getCreateTime());
            String start = dateStr.concat(" 09:00:00");
            String end = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(dto.getCreateTime(), 1)).concat(" 09:00:00");

            // 商品的订单数量
            StoreGoodsOrderItem queryObj = new StoreGoodsOrderItem();
            queryObj.setDeptId(deptId);
            queryObj.setStartTime(start);
            queryObj.setEndTime(end);
            List<StoreGoodsOrderItem> orderItemList = storeGoodsOrderItemMapper.selectOrderGoodsConversionNum(queryObj);

            // 填充上数列表中订单数量
            if(CollectionUtils.isNotEmpty(orderItemList)) {
                list.forEach(o-> {
                    o.setInQuanintiy(0.0);
                    orderItemList.forEach(order -> {
                        order.setQuantity(order.getQuantity() != null ? order.getQuantity() : 0.0);
                        if (order.getGoodsId().equals(o.getGoodsId()))
                        {
                            o.setInQuanintiy(new BigDecimal(o.getInQuanintiy() + order.getQuantity()).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                    });
                });
            }


        }

        return list;
    }

    /**
     * 新增商品上数设置
     *
     * @param storeSaleSetting 商品上数设置
     * @return 结果
     */
    @Override
    public int insertStoreSaleSetting(StoreSaleSetting storeSaleSetting) {
        return this.storeSaleSettingMapper.insertStoreSaleSetting(storeSaleSetting);
    }

    /**
     * 修改商品上数设置
     *
     * @param dto 商品上数设置
     * @return 结果
     */
    @Override
    public int updateStoreSaleSetting(StoreSaleSettingDTO dto) {

        //记录日志
        List<StoreSaleSettingLog> inserList = new ArrayList<>();
        List<StoreGoodsQuotationGoods> updateList = new ArrayList<>();
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        // 根据settingid获取商品SPU的数量
        StoreSaleSetting saleSetting =  storeSaleSettingMapper.selectStoreSaleSettingById(dto.getSettingId());

        List<StoreGoodsSpuUnit> spuUnits = storeGoodsSpuUnitMapper.selectUnitAndCoversionByDeptId(saleSetting.getDeptId());
        Map<String, StoreGoodsSpuUnit> spuUnitMap = spuUnits.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));

        dto.getGoodsList().forEach(o->{
            if(o.getSalePercent() != null) {
                // 商品的订单数量
                String today = DateUtils.getDate();
                String start = today.concat(" 00:00:00");
                String end = today.concat(" 23:59:59");
                StoreGoodsOrderItem queryObj3 = new StoreGoodsOrderItem();
                queryObj3.setDeptId(saleSetting.getDeptId());
                queryObj3.setSpuNo(saleSetting.getSpuNo());
                queryObj3.setStartTime(start);
                queryObj3.setEndTime(end);
                List<StoreGoodsOrderItem> orderItemList = storeGoodsOrderItemMapper.selectOrderSpuConversionNum(queryObj3);
                double orderNum = 0.0;
                if(CollectionUtils.isNotEmpty(orderItemList)) {
                    orderNum = orderItemList.get(0).getQuantity();
                }

                // SPU商品上数剩余数量（上数总数-订单数量）
                BigDecimal leftNum = new BigDecimal(saleSetting.getSettingQuanintiy() - orderNum);
                StoreGoodsQuotationGoods quotationGoods = storeGoodsQuotationGoodsMapper.selectStoreGoodsQuotationGoodsById(o.getGoodsId());

                // 报价单商品上数数量（上数剩余数量 * 百分比）
                BigDecimal saleNum = new BigDecimal(o.getSalePercent() * 0.01).multiply(leftNum).setScale(0,BigDecimal.ROUND_HALF_UP);
                if(quotationGoods.getUnitIds().equals(spuUnitMap.get(quotationGoods.getSpuNo()).getMainUnitId() + "")) {
                    saleNum = saleNum.multiply(new BigDecimal(spuUnitMap.get(quotationGoods.getSpuNo()).getSubCaseMain())).setScale(0,BigDecimal.ROUND_HALF_UP);
                }

                JSONObject oldJson = new JSONObject();
                JSONObject newJson = new JSONObject();
                StoreSaleSettingLog saleLog = new StoreSaleSettingLog();
                saleLog.setSettingId(dto.getSettingId());
                saleLog.setCreatorId(user.getUserId());
                saleLog.setGoodId(o.getGoodsId());
                saleLog.setSpuNo(quotationGoods.getSpuNo());
                oldJson.set("percent", quotationGoods.getSalePercent());
                oldJson.set("num", "");
                if(quotationGoods.getSalePercent() != null) {
                    oldJson.set("num", quotationGoods.getSaleNum());
                }
                newJson.set("percent", o.getSalePercent());
                newJson.set("num", "");
                if(o.getSalePercent() != null) {
                    newJson.set("num", saleNum);
                }

                saleLog.setOldContent(oldJson.toString());
                saleLog.setNewContent(newJson.toString());
                // 设置对应的库存数量
                o.setSaleNum(saleNum.doubleValue());
                inserList.add(saleLog);
                updateList.add(o);
            }
        });
        storeSaleSettingLogService.saveBatch(inserList);

        //修改报价单中设置的比例
        storeGoodsQuotationGoodsService.updateBatchById(updateList);
        return dto.getGoodsList().size();
    }

    /**
     * 删除商品上数设置对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreSaleSettingByIds(String ids) {
        return this.storeSaleSettingMapper.deleteBatchIds(Arrays.asList(Convert.toStrArray(ids)));
    }

    /**
     * 删除商品上数设置信息
     *
     * @param settingid 商品上数设置ID
     * @return 结果
     */
    @Override
    public int deleteStoreSaleSettingById(Long settingid) {
        return this.storeSaleSettingMapper.deleteStoreSaleSettingById(settingid);
    }

    /**
     * 初始化上数数据
     * TODO
     * 1. 获取商品盘点的期末库存
     * 2. 获取当前日期采购单数据，填充在途数量
     * 3. 获取当前日期从金蝶同步的入库单数据，填充到货数据
     * 4. 根据期末库存，在途数量，到货数据，经验值，获得商品上数数量
     * 5. 记录上数数据变化值当作日志记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int initSaleData(String deptId, String settingDate) {


        Date today = DateUtils.dateTime(DateUtils.YYYY_MM_DD, settingDate);
        String yesterday = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(today, -1));

        String yesterdayStart = yesterday.concat(" 00:00:00");
        String yesterdayEnd = yesterday.concat(" 23:59:59");

        String start = settingDate.concat(" 00:00:00");
        String end = settingDate.concat(" 23:59:59");
        List<StoreSaleSetting> saleSettingList = new ArrayList<>();
        // 获取报价单中SPU商品
        List<String> goodsSpus = storeSaleSettingMapper.selectStoreSaleSpuNoList(deptId);

        // 获取盘库表中商品数量
        if(CollectionUtils.isNotEmpty(goodsSpus)) {

            // 获取商品的期末库存
            QueryWrapper<StoreWarehouseInventoryItem> inventoryItemQueryWrapper = new QueryWrapper<>();
            inventoryItemQueryWrapper.select("spu_no, sum(stock_lonely_quantity) stock_lonely_quantity");
            inventoryItemQueryWrapper.eq("inventory_id", deptId);
            inventoryItemQueryWrapper.between("create_time", start, end);
            inventoryItemQueryWrapper.groupBy("inventory_id, spu_no");
            List<StoreWarehouseInventoryItem> inventoryItemList =  storeWarehouseInventoryItemMapper.selectList(inventoryItemQueryWrapper);

            // 商品的采购数量
            StoreGoodPurchaseItem queryObj1 = new StoreGoodPurchaseItem();
            queryObj1.setDeptId(deptId);
            queryObj1.setStartTime(yesterdayStart);
            queryObj1.setEndTime(yesterdayEnd);
            List<StoreGoodPurchaseItem> purchaseItemList =  storeGoodPurchaseItemMapper.selectPurchaseItemNum(queryObj1);

            // 商品的入库数量
            StoreWarehouseStockItem queryObj2 = new StoreWarehouseStockItem();
            queryObj2.setDeptId(deptId);
            queryObj2.setStartTime(start);
            queryObj2.setEndTime(end);
            List<StoreWarehouseStockItem> storeWarehouseStockItemList = storeWarehouseStockItemMapper.selectWarehouseStockItemNum(queryObj2);

            // 商品的订单数量
            StoreGoodsOrderItem queryObj3 = new StoreGoodsOrderItem();
            queryObj3.setDeptId(deptId);
            queryObj3.setStartTime(start);
            queryObj3.setEndTime(end);
            List<StoreGoodsOrderItem> orderItemList = storeGoodsOrderItemMapper.selectOrderSpuConversionNum(queryObj3);

            // 清空昨天数据的数据
            QueryWrapper<StoreSaleSetting> tempQuery = new QueryWrapper<>();
            tempQuery.eq("dept_id", deptId);
            tempQuery.between("create_time", yesterdayStart, yesterdayEnd);
            StoreSaleSetting updateObj = new StoreSaleSetting();
            updateObj.setStatus("0");
            List<StoreSaleSetting> settingDbList = storeSaleSettingMapper.selectList(tempQuery);
            int count = this.storeSaleSettingMapper.update(updateObj, tempQuery);
            logger.debug("更新的上数设置商品条数{}", count);

            // 删除今天的数据--20211030
            tempQuery.clear();
            tempQuery.eq("dept_id", deptId);
            tempQuery.between("create_time", start, end);
            List<StoreSaleSetting> settingDbListToday = storeSaleSettingMapper.selectList(tempQuery);

//            count = this.storeSaleSettingMapper.delete(tempQuery);
//
//            logger.debug("删除的上数设置商品条数{}", count);

            for(String spuNo : goodsSpus) {

                // 当天生成过的上数不在更新
                boolean isExsit = false;
                if(CollectionUtils.isNotEmpty(settingDbListToday)) {
                    for (StoreSaleSetting temp : settingDbListToday) {
                        if(temp.getSpuNo().equals(spuNo)) {
                            isExsit = true;
                        }
                    }
                }
                if(isExsit){
                    continue;
                }

                StoreSaleSetting storeSaleSetting = new StoreSaleSetting();
                storeSaleSetting.setDeptId(deptId);
                storeSaleSetting.setSpuNo(spuNo);
                storeSaleSetting.setQuanintiy(0.0);
                storeSaleSetting.setInQuanintiy(0.0);
                storeSaleSetting.setReadyQuanintiy(0.0);

                if(CollectionUtils.isNotEmpty(inventoryItemList))
                {
                    // 期末库存数量
                    inventoryItemList.stream().filter(s->s.getSpuNo().equals(spuNo)).forEach(s->{
                        storeSaleSetting.setQuanintiy(s.getStockLonelyQuantity() != null ? s.getStockLonelyQuantity() : 0.0);
                    });
                }

                if(CollectionUtils.isNotEmpty(purchaseItemList))
                {
                    // 采购数量（在途数量）
                    purchaseItemList.stream().filter(s -> s.getSpuNo().equals(spuNo)).forEach(s -> {
                        // 2022-01-22去掉在途数量
                        //storeSaleSetting.setInQuanintiy(s.getStocksNumber() != null ? s.getStocksNumber() : 0.0);
                        storeSaleSetting.setInQuanintiy(0.0);
                    });
                }

                if(CollectionUtils.isNotEmpty(storeWarehouseStockItemList))
                {
                    // 到货数量
                    storeWarehouseStockItemList.stream().filter(s -> s.getSpuNo().equals(spuNo)).forEach(s -> {
                        storeSaleSetting.setReadyQuanintiy(s.getStocksNumber() != null ? s.getStocksNumber() : 0.0);
                    });
                }

                storeSaleSetting.setOrderQuanintiy(0.0);
                if(CollectionUtils.isNotEmpty(orderItemList))
                {
                    // 订单数量
                    orderItemList.stream().filter(s -> s.getSpuNo().equals(spuNo)).forEach(s -> {
                        storeSaleSetting.setOrderQuanintiy(s.getQuantity() != null ? s.getQuantity() : 0.0);
                    });
                }

                // 填充已经设置过的浮动价格
                storeSaleSetting.setSelfQuanintiy(20.0);
                if(CollectionUtils.isNotEmpty(settingDbList)){
                    settingDbList.stream().filter(s -> s.getSpuNo().equals(spuNo)).forEach(s -> {
                        storeSaleSetting.setSelfQuanintiy(s.getSelfQuanintiy());
                    });
                }
                storeSaleSetting.setSettingQuanintiy(storeSaleSetting.getQuanintiy() +  storeSaleSetting.getInQuanintiy() + storeSaleSetting.getReadyQuanintiy() + storeSaleSetting.getSelfQuanintiy());
                saleSettingList.add(storeSaleSetting);

                // 更新报价单商品库存数量及百分比
                updateSaleNum(storeSaleSetting);

            }

            if(CollectionUtils.isNotEmpty(saleSettingList))
            {
                this.saveBatch(saleSettingList);
            }
        }

        return saleSettingList.size();
    }

    /**
     * 修改浮动上数数量
     * @param storeSaleSetting
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSelfQuanintiy(StoreSaleSetting storeSaleSetting)
    {
        // 获取对应商品的上数详情
        StoreSaleSetting dbRecord = storeSaleSettingMapper.selectStoreSaleSettingById(storeSaleSetting.getSettingId());
        BigDecimal total = new BigDecimal(dbRecord.getQuanintiy() +  dbRecord.getInQuanintiy() + dbRecord.getReadyQuanintiy() + storeSaleSetting.getSelfQuanintiy());
        storeSaleSetting.setSettingQuanintiy(total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        storeSaleSetting.setSpuNo(dbRecord.getSpuNo());
        storeSaleSetting.setDeptId(dbRecord.getDeptId());

        // 商品的订单数量
        StoreGoodsOrderItem queryObj3 = new StoreGoodsOrderItem();
        queryObj3.setDeptId(dbRecord.getDeptId());
        String start = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,dbRecord.getCreateTime()).concat(" 09:00:00");
        String end = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(dbRecord.getCreateTime(), 1)).concat(" 09:00:00");
        queryObj3.setSpuNo(dbRecord.getSpuNo());
        queryObj3.setStartTime(start);
        queryObj3.setEndTime(end);
        List<StoreGoodsOrderItem> orderItemList = storeGoodsOrderItemMapper.selectOrderSpuConversionNum(queryObj3);
        if(CollectionUtils.isNotEmpty(orderItemList)) {
            storeSaleSetting.setSettingQuanintiy(total.subtract(new BigDecimal(orderItemList.get(0).getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        updateSaleNum(storeSaleSetting);
        return storeSaleSettingMapper.updateStoreSaleSetting(storeSaleSetting);
    }

    private void updateSaleNum(StoreSaleSetting storeSaleSetting) {
        // 获取当前区域下SPU对应的报价单商品
        StoreGoodsQuotationGoodsQueryDTO dto = new StoreGoodsQuotationGoodsQueryDTO();
        dto.setDeptId(storeSaleSetting.getDeptId());
        dto.setSpuNo(storeSaleSetting.getSpuNo());
        dto.setStatus("1");
        List<StoreGoodsQuotationGoodsVO> quotationGoodsDbList = storeGoodsQuotationGoodsMapper.selectStoreGoodsQuotationGoodsList(dto);

        BigDecimal leftNum = new BigDecimal(storeSaleSetting.getSettingQuanintiy());
        if(storeSaleSetting.getOrderQuanintiy() != null) {
            leftNum = new BigDecimal(storeSaleSetting.getSettingQuanintiy()).subtract(new BigDecimal(storeSaleSetting.getOrderQuanintiy())).setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        List<StoreGoodsQuotationGoods> updateList = new ArrayList<>();

        // 设置报价单商品对应的上数设置百分比
        if(CollectionUtils.isNotEmpty(quotationGoodsDbList))
        {
            List<StoreGoodsSpuUnit> spuUnits = storeGoodsSpuUnitMapper.selectUnitAndCoversionByDeptId(storeSaleSetting.getDeptId());
            Map<String, StoreGoodsSpuUnit> spuUnitMap = spuUnits.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));

            // 获取未设置上数的报价单商品
            List<StoreGoodsQuotationGoodsVO> unSettingList = new ArrayList<>();

            // 获取未设置上数的报价单商品
            List<StoreGoodsQuotationGoodsVO> settingList = new ArrayList<>();
            // 已经设置了上数比例之和
            double settingTotal = 0.0;
            for(StoreGoodsQuotationGoodsVO obj : quotationGoodsDbList ) {
                if(obj.getSalePercent() == null) {
                    unSettingList.add(obj);
                } else {
                    settingList.add(obj);
                    settingTotal += obj.getSalePercent();
                }
            }
            if(CollectionUtils.isNotEmpty(unSettingList)) {
                double remainder = 100.00 - settingTotal;
                remainder = (remainder > 0.0) ? remainder : 0.0;
                // 获取余数
                double mod = remainder % unSettingList.size();
                // 获取上数商品的平均比例
                double avg = (remainder - mod) / unSettingList.size();
                int i = 0;
                for(StoreGoodsQuotationGoodsVO vo : unSettingList )
                {
                    StoreGoodsQuotationGoods storeGoodsQuotationGoods = new StoreGoodsQuotationGoods();
                    BeanUtils.copyProperties(vo, storeGoodsQuotationGoods);
                    if(i == 0) {
                        storeGoodsQuotationGoods.setSalePercent(avg + mod);
                    }
                    else {
                        storeGoodsQuotationGoods.setSalePercent(avg);
                    }
                    BigDecimal saleNum = new BigDecimal(storeGoodsQuotationGoods.getSalePercent() * 0.01).multiply(leftNum).setScale(0,BigDecimal.ROUND_HALF_UP);
                    storeGoodsQuotationGoods.setSaleNum(saleNum.doubleValue());
                    // 报价单商品是主单位需要换算
                    if(storeGoodsQuotationGoods.getUnitIds().equals(spuUnitMap.get(storeGoodsQuotationGoods.getSpuNo()).getMainUnitId() + "")) {
                        storeGoodsQuotationGoods.setSaleNum(saleNum.multiply(new BigDecimal(spuUnitMap.get(storeGoodsQuotationGoods.getSpuNo()).getSubCaseMain())).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue());
                    }

                    i++;
                    updateList.add(storeGoodsQuotationGoods);
                }

            }

            if(CollectionUtils.isNotEmpty(settingList)) {
                for(StoreGoodsQuotationGoodsVO vo : settingList )
                {
                    BigDecimal saleNum = new BigDecimal(vo.getSalePercent() * 0.01).multiply(leftNum).setScale(0,BigDecimal.ROUND_HALF_UP);
                    vo.setSaleNum(saleNum.doubleValue());
                    if(vo.getUnitIds().equals(spuUnitMap.get(vo.getSpuNo()).getMainUnitId() + "")) {
                        vo.setSaleNum(saleNum.multiply(new BigDecimal(spuUnitMap.get(vo.getSpuNo()).getSubCaseMain())).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    updateList.add(vo);
                }
            }

            if(CollectionUtils.isNotEmpty(updateList)){
                // 批量更新上数值
                storeGoodsQuotationGoodsService.updateBatchById(updateList);
            }
        }
    }

    /**
     * 查询商品上数设置列表
     *
     * @param storeSaleSetting 商品上数设置
     * @return 商品上数设置
     */
    @Override
    public List<StoreSaleSetting> selectStoreSaleSettingListForApp(StoreSaleSetting storeSaleSetting) {
        return storeSaleSettingMapper.selectStoreSaleSettingListForApp(storeSaleSetting);
    }
}
