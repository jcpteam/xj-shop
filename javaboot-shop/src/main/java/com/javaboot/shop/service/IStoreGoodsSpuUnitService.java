package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsSpuUnit;
import java.util.List;

/**
 * spu单位Service接口
 * 
 * @author lqh
 * @date 2021-06-20
 */
public interface IStoreGoodsSpuUnitService {
    /**
     * 查询spu单位
     * 
     * @param spuNo spu单位ID
     * @return spu单位
     */
    public StoreGoodsSpuUnit selectStoreGoodsSpuUnitById(String spuNo);

    /**
     * 查询spu单位列表
     * 
     * @param storeGoodsSpuUnit spu单位
     * @return spu单位集合
     */
    public List<StoreGoodsSpuUnit> selectStoreGoodsSpuUnitList(StoreGoodsSpuUnit storeGoodsSpuUnit);

    /**
     * 新增spu单位
     * 
     * @param storeGoodsSpuUnit spu单位
     * @return 结果
     */
    public int insertStoreGoodsSpuUnit(StoreGoodsSpuUnit storeGoodsSpuUnit);

    /**
     * 修改spu单位
     * 
     * @param storeGoodsSpuUnit spu单位
     * @return 结果
     */
    public int updateStoreGoodsSpuUnit(StoreGoodsSpuUnit storeGoodsSpuUnit);

    /**
     * 批量删除spu单位
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuUnitByIds(String ids);

    /**
     * 删除spu单位信息
     * 
     * @param spuNo spu单位ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuUnitById(String spuNo);
}
