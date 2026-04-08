package com.javaboot.shop.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.shop.domain.StoreGoodsSpuUnit;
import com.javaboot.shop.mapper.StoreGoodsSpuUnitMapper;
import com.javaboot.shop.service.IStoreGoodsSpuUnitService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsSalesUnitMapper;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.service.IStoreGoodsSalesUnitService;
import com.javaboot.common.core.text.Convert;

/**
 * 销售单位Service业务层处理
 *
 * @author lqh
 * @date 2021-05-29
 */
@Service
public class StoreGoodsSalesUnitServiceImpl implements IStoreGoodsSalesUnitService {
    @Autowired
    private StoreGoodsSalesUnitMapper storeGoodsSalesUnitMapper;
    @Autowired
    private StoreGoodsSpuUnitMapper storeGoodsSpuUnitMapper;
    @Autowired
    private IStoreGoodsSpuUnitService storeGoodsSpuUnitService;

    /**
     * 查询销售单位
     *
     * @param unitId 销售单位ID
     * @return 销售单位
     */
    @Override
    public StoreGoodsSalesUnit selectStoreGoodsSalesUnitById(Long unitId) {
        return storeGoodsSalesUnitMapper.selectStoreGoodsSalesUnitById(unitId);
    }

    /**
     * 查询销售单位列表
     *
     * @param storeGoodsSalesUnit 销售单位
     * @return 销售单位
     */
    @Override
    public List<StoreGoodsSalesUnit> selectStoreGoodsSalesUnitList(StoreGoodsSalesUnit storeGoodsSalesUnit) {
        return storeGoodsSalesUnitMapper.selectStoreGoodsSalesUnitList(storeGoodsSalesUnit);
    }

    /**
     * 新增销售单位
     *
     * @param storeGoodsSalesUnit 销售单位
     * @return 结果
     */
    @Override
    public int insertStoreGoodsSalesUnit(StoreGoodsSalesUnit storeGoodsSalesUnit) {
        return storeGoodsSalesUnitMapper.insertStoreGoodsSalesUnit(storeGoodsSalesUnit);
    }

    /**
     * 修改销售单位
     *
     * @param storeGoodsSalesUnit 销售单位
     * @return 结果
     */
    @Override
    public int updateStoreGoodsSalesUnit(StoreGoodsSalesUnit storeGoodsSalesUnit) {
        return storeGoodsSalesUnitMapper.updateStoreGoodsSalesUnit(storeGoodsSalesUnit);
    }

    /**
     * 删除销售单位对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSalesUnitByIds(String ids) {
        //商品关联了单位不能删除
        int count = storeGoodsSpuUnitMapper.selectSpuUnitCountByIds(Convert.toStrArray(ids));
        if(count > 0) {
            throw new BusinessException("商品关联了单位不能删除!");
        }
        return storeGoodsSalesUnitMapper.deleteStoreGoodsSalesUnitByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除销售单位信息
     *
     * @param unitId 销售单位ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSalesUnitById(Long unitId) {
        return storeGoodsSalesUnitMapper.deleteStoreGoodsSalesUnitById(unitId);
    }
    /**
     * 获取正常规格
     *
     * @return
     */
    @Override
    public List<StoreGoodsSalesUnit> getNormalSpecificationsList() {
        StoreGoodsSalesUnit salesUnit = new StoreGoodsSalesUnit();
        salesUnit.setStatus(Constants.NORMAL);
        List<StoreGoodsSalesUnit> list = selectStoreGoodsSalesUnitList(salesUnit);
        if (CollectionUtils.isNotEmpty(list)) {
            list.sort(Comparator.comparing(StoreGoodsSalesUnit::getOrderNum));
        }
        return list;
    }

    @Override
    public List<StoreGoodsSalesUnit> getSpuUnit(String spuNo) {
        StoreGoodsSpuUnit spuUnit=storeGoodsSpuUnitService.selectStoreGoodsSpuUnitById(spuNo);
        if(spuUnit!=null){
            StoreGoodsSalesUnit salesUnit = new StoreGoodsSalesUnit();
            salesUnit.setStatus(Constants.NORMAL);
            salesUnit.setUnitIds(Arrays.asList(spuUnit.getMainUnitId(),spuUnit.getSubUnitId()));
            List<StoreGoodsSalesUnit> spuUnitList = selectStoreGoodsSalesUnitList(salesUnit);
            // 主单位默认排第一
            Collections.sort(spuUnitList, (o1, o2) -> {
                if(o1.getUnitId().equals(spuUnit.getMainUnitId()))
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            });
            return spuUnitList;
        }
        return null;
    }
}
