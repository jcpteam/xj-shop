package com.javaboot.shop.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsSpuUnitConversionMapper;
import com.javaboot.shop.domain.StoreGoodsSpuUnitConversion;
import com.javaboot.shop.service.IStoreGoodsSpuUnitConversionService;
import com.javaboot.common.core.text.Convert;

/**
 * spu商品单位换算关系Service业务层处理
 * 
 * @author lqh
 * @date 2021-06-20
 */
@Service
public class StoreGoodsSpuUnitConversionServiceImpl extends ServiceImpl<StoreGoodsSpuUnitConversionMapper, StoreGoodsSpuUnitConversion> implements IStoreGoodsSpuUnitConversionService {
    @Autowired
    private StoreGoodsSpuUnitConversionMapper storeGoodsSpuUnitConversionMapper;

    /**
     * 查询spu商品单位换算关系
     * 
     * @param warehouseId spu商品单位换算关系ID
     * @return spu商品单位换算关系
     */
    @Override
    public StoreGoodsSpuUnitConversion selectStoreGoodsSpuUnitConversionById(Long warehouseId) {
        return storeGoodsSpuUnitConversionMapper.selectStoreGoodsSpuUnitConversionById(warehouseId);
    }

    /**
     * 查询spu商品单位换算关系列表
     * 
     * @param storeGoodsSpuUnitConversion spu商品单位换算关系
     * @return spu商品单位换算关系
     */
    @Override
    public List<StoreGoodsSpuUnitConversion> selectStoreGoodsSpuUnitConversionList(StoreGoodsSpuUnitConversion storeGoodsSpuUnitConversion) {
        return storeGoodsSpuUnitConversionMapper.selectStoreGoodsSpuUnitConversionList(storeGoodsSpuUnitConversion);
    }

    /**
     * 新增spu商品单位换算关系
     * 
     * @param storeGoodsSpuUnitConversion spu商品单位换算关系
     * @return 结果
     */
    @Override
    public int insertStoreGoodsSpuUnitConversion(StoreGoodsSpuUnitConversion storeGoodsSpuUnitConversion) {
        return storeGoodsSpuUnitConversionMapper.insertStoreGoodsSpuUnitConversion(storeGoodsSpuUnitConversion);
    }

    /**
     * 修改spu商品单位换算关系
     * 
     * @param storeGoodsSpuUnitConversion spu商品单位换算关系
     * @return 结果
     */
    @Override
    public int updateStoreGoodsSpuUnitConversion(StoreGoodsSpuUnitConversion storeGoodsSpuUnitConversion) {
        return storeGoodsSpuUnitConversionMapper.updateStoreGoodsSpuUnitConversion(storeGoodsSpuUnitConversion);
    }

    /**
     * 删除spu商品单位换算关系对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSpuUnitConversionByIds(String ids) {
        return storeGoodsSpuUnitConversionMapper.deleteStoreGoodsSpuUnitConversionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除spu商品单位换算关系信息
     * 
     * @param warehouseId spu商品单位换算关系ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSpuUnitConversionById(Long warehouseId) {
        return storeGoodsSpuUnitConversionMapper.deleteStoreGoodsSpuUnitConversionById(warehouseId);
    }
}
