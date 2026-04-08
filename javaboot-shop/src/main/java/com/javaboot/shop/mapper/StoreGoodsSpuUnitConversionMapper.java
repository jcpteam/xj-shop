package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreGoodsSpuUnitConversion;
import java.util.List;
import java.util.Map;

/**
 * spu商品单位换算关系Mapper接口
 * 
 * @author lqh
 * @date 2021-06-20
 */
public interface StoreGoodsSpuUnitConversionMapper extends BaseMapper<StoreGoodsSpuUnitConversion>
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
     * 删除spu商品单位换算关系
     * 
     * @param warehouseId spu商品单位换算关系ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuUnitConversionById(Long warehouseId);

    /**
     * 批量删除spu商品单位换算关系
     * 
     * @param warehouseIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuUnitConversionByIds(String[] warehouseIds);

    /**
     * 根据区域和商品sup列表删除主副单位转换关系
     * @param map
     * @return
     */
    public int deleteSpuUnitConversionBySpus(Map<String, Object> map);

    /**
     * 获取商品主副单位的换算关系
     * @param map
     * @return
     */
    List<StoreGoodsSpuUnitConversion> selectSpuConversion(Map<String, Object> map);
}
