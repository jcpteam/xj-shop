package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.StoreGoodsSpuUnitConversion;
import java.util.List;

/**
 * spu商品单位换算关系Service接口
 * 
 * @author lqh
 * @date 2021-06-20
 */
public interface IStoreGoodsSpuUnitConversionService extends IService<StoreGoodsSpuUnitConversion>
{
    /**
     * 查询spu商品单位换算关系
     * 
     * @param warehouseId spu商品单位换算关系ID
     * @return spu商品单位换算关系
     */
    public StoreGoodsSpuUnitConversion selectStoreGoodsSpuUnitConversionById(Long warehouseId);

    /**
     * 查询spu商品单位换算关系列表
     * 
     * @param storeGoodsSpuUnitConversion spu商品单位换算关系
     * @return spu商品单位换算关系集合
     */
    public List<StoreGoodsSpuUnitConversion> selectStoreGoodsSpuUnitConversionList(StoreGoodsSpuUnitConversion storeGoodsSpuUnitConversion);

    /**
     * 新增spu商品单位换算关系
     * 
     * @param storeGoodsSpuUnitConversion spu商品单位换算关系
     * @return 结果
     */
    public int insertStoreGoodsSpuUnitConversion(StoreGoodsSpuUnitConversion storeGoodsSpuUnitConversion);

    /**
     * 修改spu商品单位换算关系
     * 
     * @param storeGoodsSpuUnitConversion spu商品单位换算关系
     * @return 结果
     */
    public int updateStoreGoodsSpuUnitConversion(StoreGoodsSpuUnitConversion storeGoodsSpuUnitConversion);

    /**
     * 批量删除spu商品单位换算关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuUnitConversionByIds(String ids);

    /**
     * 删除spu商品单位换算关系信息
     * 
     * @param warehouseId spu商品单位换算关系ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuUnitConversionById(Long warehouseId);
}
