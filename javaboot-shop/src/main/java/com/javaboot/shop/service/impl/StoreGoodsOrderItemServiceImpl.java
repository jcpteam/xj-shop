package com.javaboot.shop.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.domain.StoreGoodsSpuConversionNew;
import com.javaboot.shop.domain.StoreSortingBox;
import com.javaboot.shop.service.IStoreGoodsSalesUnitService;
import com.javaboot.shop.service.IStoreGoodsSpuConversionNewService;
import com.javaboot.shop.service.IStoreSortingBoxService;
import com.javaboot.shop.vo.StoreGoodsOrderItemVO;
import com.javaboot.shop.vo.StoreGoodsOrderSortingItemVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsOrderItemMapper;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.service.IStoreGoodsOrderItemService;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单明细Service业务层处理
 *
 * @author lqh
 * @date 2021-06-01
 */
@Service
public class StoreGoodsOrderItemServiceImpl implements IStoreGoodsOrderItemService {
    @Autowired
    private StoreGoodsOrderItemMapper storeGoodsOrderItemMapper;
    @Autowired
    private IStoreGoodsSalesUnitService iStoreGoodsSalesUnitService;

    @Autowired
    private IStoreSortingBoxService storeSortingBoxService;

    @Autowired
    private IStoreGoodsSpuConversionNewService storeGoodsSpuConversionNewService;

    /**
     * 查询订单明细
     *
     * @param itemId 订单明细ID
     * @return 订单明细
     */
    @Override
    public StoreGoodsOrderItem selectStoreGoodsOrderItemById(Long itemId) {
        return storeGoodsOrderItemMapper.selectStoreGoodsOrderItemById(itemId);
    }

    /**
     * 查询订单明细列表
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 订单明细
     */
    @Override
    public List<StoreGoodsOrderItemVO> selectStoreGoodsOrderItemList(StoreGoodsOrderItem storeGoodsOrderItem) {
        List<StoreGoodsOrderItemVO>  list=   storeGoodsOrderItemMapper.selectStoreGoodsOrderItemList(storeGoodsOrderItem);
        if(CollectionUtils.isNotEmpty(list)){
            StoreGoodsSalesUnit query = new StoreGoodsSalesUnit();
            List<Long> unitIdIds=list.stream().map(StoreGoodsOrderItem::getUnitId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(unitIdIds)){
                query.setUnitIds(unitIdIds);
                List<StoreGoodsSalesUnit> storeGoodsSalesUnits = iStoreGoodsSalesUnitService.selectStoreGoodsSalesUnitList(query);
                if(CollectionUtils.isNotEmpty(storeGoodsSalesUnits)){
                    list.forEach(l->{
                        l.setStoreGoodsSalesUnitList(storeGoodsSalesUnits.stream().filter(e->l.getUnitId().equals(e.getUnitId().toString())).collect(Collectors.toList()));
                    });
                }
            }
        }
        return list;
    }

    /**
     * 新增订单明细
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodsOrderItem(StoreGoodsOrderItem storeGoodsOrderItem) {
        storeGoodsOrderItem.setCreateTime(DateUtils.getNowDate());
        return storeGoodsOrderItemMapper.insertStoreGoodsOrderItem(storeGoodsOrderItem);
    }

    /**
     * 修改订单明细
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodsOrderItem(StoreGoodsOrderItem storeGoodsOrderItem) {
        return storeGoodsOrderItemMapper.updateStoreGoodsOrderItem(storeGoodsOrderItem);
    }

    /**
     * 删除订单明细对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsOrderItemByIds(String ids) {
        return storeGoodsOrderItemMapper.deleteStoreGoodsOrderItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除订单明细信息
     *
     * @param itemId 订单明细ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsOrderItemById(Long itemId) {
        return storeGoodsOrderItemMapper.deleteStoreGoodsOrderItemById(itemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsOrderItemByOrderId(Long orderId) {
        return storeGoodsOrderItemMapper.deleteStoreGoodsOrderItemByOrderId(orderId);
    }

    /**
     * 查询订单明细列表
     *
     * @param storeGoodsOrderItem 订单明细
     * @return 订单明细
     */
    @Override
    public List<StoreGoodsOrderSortingItemVO> selectStoreGoodsOrderItemListForSorting(StoreGoodsOrderItem storeGoodsOrderItem) {
        List<StoreGoodsOrderSortingItemVO>  list=   storeGoodsOrderItemMapper.selectStoreGoodsOrderItemListForSorting(storeGoodsOrderItem);
        if(CollectionUtils.isNotEmpty(list)){
            //遍历订单商品列表，查询除所有的单位id信息
            List<Long> unitIdIds = new ArrayList<>();
            for (StoreGoodsOrderSortingItemVO vo : list){
                //销售单位
                if(vo.getUnitId() != null){
                    if(!unitIdIds.contains(vo.getUnitId())){
                        unitIdIds.add(vo.getUnitId());
                    }
                }
                //主单位
                if(vo.getMainUnitId() != null){
                    if(!unitIdIds.contains(vo.getMainUnitId())){
                        unitIdIds.add(vo.getMainUnitId());
                    }
                }
                //副单位
                if(vo.getSubUnitId() != null){
                    if(!unitIdIds.contains(vo.getSubUnitId())){
                        unitIdIds.add(vo.getSubUnitId());
                    }
                }
            }

            //通过单位id，查询出单位信息
            Map<Long,StoreGoodsSalesUnit> unitId2InfoMap = null;
            if(CollectionUtils.isNotEmpty(unitIdIds)){
                StoreGoodsSalesUnit query = new StoreGoodsSalesUnit();
                query.setUnitIds(unitIdIds);
                List<StoreGoodsSalesUnit> storeGoodsSalesUnits = iStoreGoodsSalesUnitService.selectStoreGoodsSalesUnitList(query);
                if(CollectionUtils.isNotEmpty(storeGoodsSalesUnits)){
                    unitId2InfoMap = storeGoodsSalesUnits.stream().collect(Collectors.toMap(StoreGoodsSalesUnit::getUnitId,info->info));
                }
            }

            //先查询所有的商品spu对应的新的转换比例
            List<StoreGoodsSpuConversionNew> conversionNewList = storeGoodsSpuConversionNewService.selectStoreGoodsSpuConversionNewList(null);
            Map<String,Double> spuNo2ConversionMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(conversionNewList)){
                for (StoreGoodsSpuConversionNew conversion : conversionNewList){
                    spuNo2ConversionMap.put(conversion.getSpuNo(),conversion.getConversion());
                }
            }

            //查询分拣框子列表
            List<StoreSortingBox> sortingBoxList = storeSortingBoxService.selectStoreSortingBoxList(null);
            //遍历订单商品列表，设置所有单位信息：销售单位、主单位、副单位
            for (StoreGoodsOrderSortingItemVO vo : list){
                vo.setSortingBoxList(sortingBoxList);
                if(unitId2InfoMap != null && !unitId2InfoMap.isEmpty()){
                    if(vo.getUnitId() != null && unitId2InfoMap.get(vo.getUnitId()) != null){
                        vo.setUnitName(unitId2InfoMap.get(vo.getUnitId()).getName());
                    }
                    if(vo.getMainUnitId() != null && unitId2InfoMap.get(vo.getMainUnitId()) != null){
                        vo.setMainUnitName(unitId2InfoMap.get(vo.getMainUnitId()).getName());
                    }
                    if(vo.getSubUnitId() != null && unitId2InfoMap.get(vo.getSubUnitId()) != null){
                        vo.setSubUnitName(unitId2InfoMap.get(vo.getSubUnitId()).getName());
                    }
                }
                //设置新的转换比例
                if (spuNo2ConversionMap.get(vo.getSpuNo()) != null && spuNo2ConversionMap.get(vo.getSpuNo()) != 0) {
                    //副转主直接使用比例
                    vo.setSubCaseMain(spuNo2ConversionMap.get(vo.getSpuNo()));
                    //主转副需要 1/比例 计算一下
                    BigDecimal num1 = new BigDecimal(1);
                    BigDecimal num2 = new BigDecimal(spuNo2ConversionMap.get(vo.getSpuNo()));
                    // 保留2位小数，四舍五入
                    vo.setMainCaseSub(num1.divide(num2, 2, RoundingMode.HALF_UP).doubleValue());
                }

                if(vo.getMainCaseSub() == null){
                    vo.setMainCaseSub(1d);
                }
                if(vo.getSubCaseMain() == null){
                    vo.setSubCaseMain(1d);
                }
                //比较当前商品除销售单位外的其他单位
                if(vo.getUnitId() != null && vo.getMainUnitId() != null && vo.getSubUnitId() != null ){
                    //销售单位和主单位相同，则另一个单位为副单位
                    if(vo.getUnitId() == vo.getMainUnitId()){
                        vo.setUnitId1(vo.getSubUnitId());
                        vo.setUnitName1(vo.getSubUnitName());
                        //设置主转副的数量
                        if(vo.getQuantity() != null && vo.getMainCaseSub() != null){
                            Double q = vo.getQuantity()*vo.getMainCaseSub();
                            if(q >0){
                                BigDecimal bg = new BigDecimal(q);
                                double f1 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                                vo.setQuantity1(f1);
                            }else{
                                vo.setQuantity1(q);
                            }
                        }
                    } else{
                        //其他说明销售单位和副单位相同，则另一个单位为主单位
                        vo.setUnitId1(vo.getMainUnitId());
                        vo.setUnitName1(vo.getMainUnitName());
                        //设置副转主的数量
                        if(vo.getQuantity() != null && vo.getSubCaseMain() != null){
                            Double q = vo.getQuantity()*vo.getSubCaseMain();
                            if(q >0){
                                BigDecimal bg = new BigDecimal(q);
                                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                vo.setQuantity1(f1);
                            }else{
                                vo.setQuantity1(q);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

}
