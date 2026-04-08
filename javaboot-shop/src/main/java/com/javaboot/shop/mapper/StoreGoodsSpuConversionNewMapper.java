package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreGoodsSpuConversionNew;
import java.util.List;

/**
 * spu转换比例Mapper接口
 * 
 * @author javaboot
 * @date 2022-03-05
 */
public interface StoreGoodsSpuConversionNewMapper {
    /**
     * 查询spu转换比例
     * 
     * @param id spu转换比例ID
     * @return spu转换比例
     */
    public StoreGoodsSpuConversionNew selectStoreGoodsSpuConversionNewById(Long id);

    /**
     * 查询spu转换比例列表
     * 
     * @param storeGoodsSpuConversionNew spu转换比例
     * @return spu转换比例集合
     */
    public List<StoreGoodsSpuConversionNew> selectStoreGoodsSpuConversionNewList(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew);

    /**
     * 新增spu转换比例
     * 
     * @param storeGoodsSpuConversionNew spu转换比例
     * @return 结果
     */
    public int insertStoreGoodsSpuConversionNew(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew);

    /**
     * 修改spu转换比例
     * 
     * @param storeGoodsSpuConversionNew spu转换比例
     * @return 结果
     */
    public int updateStoreGoodsSpuConversionNew(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew);

    /**
     * 删除spu转换比例
     * 
     * @param id spu转换比例ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuConversionNewById(Long id);

    /**
     * 批量删除spu转换比例
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuConversionNewByIds(String[] ids);
}
