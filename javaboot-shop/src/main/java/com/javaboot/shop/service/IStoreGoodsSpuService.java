package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsSpu;
import com.javaboot.shop.dto.StoreGoodsSpuDTO;
import com.javaboot.shop.vo.StoreGoodsSpuVO;

import java.util.List;

/**
 * 商品SPU信息Service接口
 *
 * @author lqh
 * @date 2021-05-23
 */
public interface IStoreGoodsSpuService {
    /**
     * 查询商品SPU信息
     *
     * @param spuNo 商品SPU信息ID
     * @return 商品SPU信息
     */
    public StoreGoodsSpuVO selectStoreGoodsSpuById(String spuNo);

    /**
     * 查询商品SPU信息列表
     *
     * @param storeGoodsSpu 商品SPU信息
     * @return 商品SPU信息集合
     */
    public List<StoreGoodsSpuVO> selectStoreGoodsSpuList(StoreGoodsSpuDTO storeGoodsSpu);

    /**
     * 新增商品SPU信息
     *
     * @param storeGoodsSpu 商品SPU信息
     * @return 结果
     */
    public int insertStoreGoodsSpu(StoreGoodsSpu storeGoodsSpu);

    /**
     * 修改商品SPU信息
     *
     * @param storeGoodsSpu 商品SPU信息
     * @return 结果
     */
    public int updateStoreGoodsSpu(StoreGoodsSpu storeGoodsSpu);

    /**
     * 批量删除商品SPU信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuByIds(String ids);

    /**
     * 删除商品SPU信息信息
     *
     * @param spuNo 商品SPU信息ID
     * @return 结果
     */
    public int deleteStoreGoodsSpuById(Long spuNo);

}
