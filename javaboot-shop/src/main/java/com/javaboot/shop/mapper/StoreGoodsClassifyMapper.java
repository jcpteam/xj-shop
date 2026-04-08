package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreGoodsClassify;
import java.util.List;

/**
 * 商品分类Mapper接口
 *
 * @author lqh
 * @date 2021-05-23
 */
public interface StoreGoodsClassifyMapper {
    /**
     * 查询商品分类
     *
     * @param classifyId 商品分类ID
     * @return 商品分类
     */
    public StoreGoodsClassify selectStoreGoodClassifyById(String classifyId);

    /**
     * 查询商品分类列表
     *
     * @param storeGoodClassify 商品分类
     * @return 商品分类集合
     */
    public List<StoreGoodsClassify> selectStoreGoodClassifyList(StoreGoodsClassify storeGoodClassify);

    /**
     * 新增商品分类
     *
     * @param storeGoodClassify 商品分类
     * @return 结果
     */
    public int insertStoreGoodClassify(StoreGoodsClassify storeGoodClassify);

    /**
     * 修改商品分类
     *
     * @param storeGoodClassify 商品分类
     * @return 结果
     */
    public int updateStoreGoodClassify(StoreGoodsClassify storeGoodClassify);

    /**
     * 删除商品分类
     *
     * @param classifyId 商品分类ID
     * @return 结果
     */
    public int deleteStoreGoodClassifyById(String classifyId);

    /**
     * 批量删除商品分类
     *
     * @param classifyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodClassifyByIds(String[] classifyId);
}
