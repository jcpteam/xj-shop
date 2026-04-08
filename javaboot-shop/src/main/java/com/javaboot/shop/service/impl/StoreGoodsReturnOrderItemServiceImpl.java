package com.javaboot.shop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.service.IStoreGoodsSalesUnitService;
import com.javaboot.shop.vo.StoreGoodsReturnOrderItemVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsReturnOrderItemMapper;
import com.javaboot.shop.domain.StoreGoodsReturnOrderItem;
import com.javaboot.shop.service.IStoreGoodsReturnOrderItemService;
import com.javaboot.common.core.text.Convert;

/**
 * 退货单明细Service业务层处理
 * 
 * @author lqh
 * @date 2021-06-26
 */
@Service
public class StoreGoodsReturnOrderItemServiceImpl implements IStoreGoodsReturnOrderItemService {
    @Autowired
    private StoreGoodsReturnOrderItemMapper storeGoodsReturnOrderItemMapper;
    @Autowired
    private IStoreGoodsSalesUnitService iStoreGoodsSalesUnitService;
    /**
     * 查询退货单明细
     * 
     * @param itemId 退货单明细ID
     * @return 退货单明细
     */
    @Override
    public StoreGoodsReturnOrderItem selectStoreGoodsReturnOrderItemById(Long itemId) {
        return storeGoodsReturnOrderItemMapper.selectStoreGoodsReturnOrderItemById(itemId);
    }

    /**
     * 查询退货单明细列表
     * 
     * @param storeGoodsReturnOrderItem 退货单明细
     * @return 退货单明细
     */
    @Override
    public List<StoreGoodsReturnOrderItemVO> selectStoreGoodsReturnOrderItemList(StoreGoodsReturnOrderItem storeGoodsReturnOrderItem) {
        List<StoreGoodsReturnOrderItemVO>  list= storeGoodsReturnOrderItemMapper.selectStoreGoodsReturnOrderItemList(storeGoodsReturnOrderItem);
        if(CollectionUtils.isNotEmpty(list)){
            StoreGoodsSalesUnit query = new StoreGoodsSalesUnit();
            List<Long> unitIdIds=list.stream().map(StoreGoodsReturnOrderItemVO::getUnitId).collect(Collectors.toList());
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
     * 新增退货单明细
     * 
     * @param storeGoodsReturnOrderItem 退货单明细
     * @return 结果
     */
    @Override
    public int insertStoreGoodsReturnOrderItem(StoreGoodsReturnOrderItem storeGoodsReturnOrderItem) {
        storeGoodsReturnOrderItem.setCreateTime(DateUtils.getNowDate());
        return storeGoodsReturnOrderItemMapper.insertStoreGoodsReturnOrderItem(storeGoodsReturnOrderItem);
    }

    /**
     * 修改退货单明细
     * 
     * @param storeGoodsReturnOrderItem 退货单明细
     * @return 结果
     */
    @Override
    public int updateStoreGoodsReturnOrderItem(StoreGoodsReturnOrderItem storeGoodsReturnOrderItem) {
        return storeGoodsReturnOrderItemMapper.updateStoreGoodsReturnOrderItem(storeGoodsReturnOrderItem);
    }

    /**
     * 删除退货单明细对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsReturnOrderItemByIds(String ids) {
        return storeGoodsReturnOrderItemMapper.deleteStoreGoodsReturnOrderItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除退货单明细信息
     * 
     * @param itemId 退货单明细ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsReturnOrderItemById(Long itemId) {
        return storeGoodsReturnOrderItemMapper.deleteStoreGoodsReturnOrderItemById(itemId);
    }

    /**
     * 删除退货单明细信息
     * @param returnOrderId
     * @return
     */
    @Override
    public int deleteStoreGoodsReturnOrderItemByReturnOrderId(Long returnOrderId) {
        return storeGoodsReturnOrderItemMapper.deleteStoreGoodsReturnOrderItemByReturnOrderId(returnOrderId);
    }
}
