package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.StockCategory;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.AsLossStock;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.mapper.AsLossStockMapper;
import com.javaboot.shop.mapper.StoreWarehouseInventoryItemMapper;
import com.javaboot.shop.service.IAsLossStockService;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.vo.SysDeptSelectVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * lossService业务层处理
 *
 * @author javaboot
 * @date 2022-01-20
 */
@Service
public class AsLossStockServiceImpl extends ServiceImpl<AsLossStockMapper, AsLossStock> implements IAsLossStockService {

    private static final Logger logger = LoggerFactory.getLogger(AsLossStockServiceImpl.class);

    @Autowired
    private AsLossStockMapper asLossStockMapper;

    @Autowired
    private StoreWarehouseInventoryItemMapper storeWarehouseInventoryItemMapper;

    @Autowired
    private ISysDeptService deptService;
    /**
     * 查询loss
     *
     * @param spuNo lossID
     * @return loss
     */
    @Override
    public AsLossStock selectAsLossStockById(String spuNo) {
        return asLossStockMapper.selectAsLossStockById(spuNo);
    }

    /**
     * 查询loss列表
     *
     * @param asLossStock loss
     * @return loss
     */
    @Override
    public List<AsLossStock> selectAsLossStockList(AsLossStock asLossStock) {
        List<AsLossStock> list = asLossStockMapper.selectAsLossStockList(asLossStock);
        // 填充区域id
        if(CollectionUtils.isNotEmpty(list)) {

            // 获取所有的部门
            List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
            Map<String, SysDeptSelectVO> deptMap = deptList.stream().collect(Collectors.toMap(key->key.getValue(), obj->obj));

            // 获取所有商品
            List<StoreWarehouseInventoryItem>  spuList = storeWarehouseInventoryItemMapper.selectAllInventorySpus(null);
            Map<String, StoreWarehouseInventoryItem> spuMap = spuList.stream().collect(Collectors.toMap(key->key.getSpuNo(), obj->obj));

            list.forEach(obj->{
                obj.setDeptName(deptMap.get(obj.getDeptId()).getName());
                obj.setSpuName(spuMap.get(obj.getSpuNo()).getSpuName());
            });
        }
        return list;

    }

    /**
     * 新增loss
     *
     * @param asLossStock loss
     * @return 结果
     */
    @Override
    public int insertAsLossStock(AsLossStock asLossStock) {
        String qryDate = asLossStock.getQryDate();
        Date start = new Date();
        if(StringUtils.isNotBlank(qryDate)) {
            start = DateUtils.parseDate(qryDate);
        }
        Date firstDay = DateUtils.addDays(start, -30);
        Long interval = 30L;
        // 获取所有的部门
        List<SysDeptSelectVO> deptList = deptService.selectDeptListById(null);
        for(int i = 0; i < interval; i++) {
            String startDay = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(firstDay, i));

            for (SysDeptSelectVO o : deptList)
            {
                List<AsLossStock> asLossList = new ArrayList<>();

                StoreWarehouseInventoryDTO storeWarehouseInventory = new StoreWarehouseInventoryDTO();
                storeWarehouseInventory.setDeptId(o.getValue());

                // 获取盘库日期中的总损耗
                String inventoryDay = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(DateUtils.parseDate(startDay), -1));
                storeWarehouseInventory.setStartTime(inventoryDay.concat(" 00:00:00"));
                storeWarehouseInventory.setEndTime(inventoryDay.concat(" 23:59:59"));
                // 获取上一天选中仓库的盘库单主单位和副单位的数量
                List<StoreWarehouseInventoryItem> inventoryList = storeWarehouseInventoryItemMapper.selectWarehouseInventory(
                    storeWarehouseInventory);

                // 库存开始时间和结束时间
                storeWarehouseInventory.setStartTime(startDay.concat(" 00:00:00"));
                storeWarehouseInventory.setEndTime(startDay.concat(" 23:59:59"));
                // 排除调拨出库的入库单
                storeWarehouseInventory.setCategory(StockCategory.ADJUST_OUT_ORDER.getCode());
                // 获取当期月1号开始时间和当天日期，选中仓库的入库单主单位和副单位的数量
                List<StoreWarehouseInventoryItem> stockInventoryList = storeWarehouseInventoryItemMapper.selectWarehouseStockLoss(
                    storeWarehouseInventory);

                // 订单开始和结束时间
                storeWarehouseInventory.setStartTime(startDay.concat(" 09:00:00"));
                storeWarehouseInventory.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(firstDay, (i+1))).concat(" 09:00:00"));
                List<StoreWarehouseInventoryItem> orderInventoryList = storeWarehouseInventoryItemMapper.selectSpuOrderLoss(
                    storeWarehouseInventory);

                if (CollectionUtils.isEmpty(inventoryList)
                    && CollectionUtils.isEmpty(orderInventoryList)
                    && CollectionUtils.isEmpty(stockInventoryList))
                {
                    continue;
                }

                Set<String> spuNoList = new HashSet<>();
                spuNoList.addAll(inventoryList.stream().map(StoreWarehouseInventoryItem::getSpuNo).collect(
                    Collectors.toList()));
                spuNoList.addAll(orderInventoryList.stream().map(StoreWarehouseInventoryItem::getSpuNo).collect(Collectors.toList()));
                spuNoList.addAll(stockInventoryList.stream().map(StoreWarehouseInventoryItem::getSpuNo).collect(Collectors.toList()));

                spuNoList.forEach(spuNo -> {
                    AsLossStock item = new AsLossStock();
                    item.setSpuNo(spuNo);
                    item.setDeptId(o.getValue());
                    item.setStatDate(DateUtils.parseDate(startDay));
                    item.setStockLossQuanity(0.0);
                    item.setTotalLossQuanity(0.0);
                    item.setOrderLossQuanity(0.0);
                    item.setIntransitLossQuanity(0.0);
                    // 计算订单损耗
                    orderInventoryList.stream().filter(obj -> obj.getSpuNo().equals(spuNo)).forEach(order -> {
                        item.setOrderLossQuanity(order.getSalesQuantity());
                    });

                    // 计算入库损耗
                    stockInventoryList.stream().filter(obj -> obj.getSpuNo().equals(spuNo)).forEach(stock -> {
                        item.setStockLossQuanity(stock.getStockQuantity());
                    });

                    // 计算总损耗
                    inventoryList.stream().filter(obj -> obj.getSpuNo().equals(spuNo)).forEach(stock -> {
                        // 损益数量
                        if(stock.getInventoryResult() != null) {
                            item.setTotalLossQuanity(stock.getInventoryResult());
                        }
                    });

                    // 在途损耗 = 总损耗 - 入库损耗 - 订单损耗
                    Double intransNum = item.getTotalLossQuanity()  - item.getStockLossQuanity() - item.getOrderLossQuanity();
                    item.setIntransitLossQuanity(new BigDecimal(intransNum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    asLossList.add(item);
                });

                if (CollectionUtils.isNotEmpty(asLossList))
                {
                    // 清空昨天数据的数据
                    QueryWrapper<AsLossStock> delWrapper = new QueryWrapper<>();
                    delWrapper.eq("dept_id", o.getValue());
                    delWrapper.eq("stat_date", startDay);
                    int count = this.asLossStockMapper.delete(delWrapper);
                    logger.debug("删除的统计数据{}", count);

                    this.saveBatch(asLossList);
                }
            }
        }


        return 1;
    }

    /**
     * 修改loss
     *
     * @param asLossStock loss
     * @return 结果
     */
    @Override
    public int updateAsLossStock(AsLossStock asLossStock) {
        return asLossStockMapper.updateAsLossStock(asLossStock);
    }

    /**
     * 删除loss对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAsLossStockByIds(String ids) {
        return asLossStockMapper.deleteAsLossStockByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除loss信息
     *
     * @param spuNo lossID
     * @return 结果
     */
    @Override
    public int deleteAsLossStockById(String spuNo) {
        return asLossStockMapper.deleteAsLossStockById(spuNo);
    }
}
