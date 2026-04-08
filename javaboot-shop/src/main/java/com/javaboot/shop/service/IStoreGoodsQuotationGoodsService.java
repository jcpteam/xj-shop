package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsQueryDTO;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsSaleNumDTO;
import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;

import java.util.List;

/**
 * 商品列表Service接口
 *
 * @author lqh
 * @date 2021-05-23
 */
public interface IStoreGoodsQuotationGoodsService extends IService<StoreGoodsQuotationGoods> {
    /**
     * 查询商品列表
     *
     * @param goodsId 商品列表ID
     * @return 商品列表
     */
    public StoreGoodsQuotationGoodsVO selectStoreGoodsQuotationGoodsById(Long goodsId);

    /**
     * 查询商品列表列表
     *
     * @param storeGoodsQuotationGoods 商品列表
     * @return 商品列表集合
     */
    public List<StoreGoodsQuotationGoodsVO> selectStoreGoodsQuotationGoodsList(StoreGoodsQuotationGoodsQueryDTO storeGoodsQuotationGoods);

    /**
     * 新增商品列表
     *
     * @param storeGoodsQuotationGoods 商品列表
     * @return 结果
     */
    public int insertStoreGoodsQuotationGoods(StoreGoodsQuotationGoods storeGoodsQuotationGoods);

    /**
     * 修改商品列表
     *
     * @param storeGoodsQuotationGoods 商品列表
     * @return 结果
     */
    public int updateStoreGoodsQuotationGoods(StoreGoodsQuotationGoods storeGoodsQuotationGoods);

    /**
     * 批量删除商品列表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsQuotationGoodsByIds(String ids);

    /**
     * 删除商品列表信息
     *
     * @param goodsId 商品列表ID
     * @return 结果
     */
    public int deleteStoreGoodsQuotationGoodsById(Long goodsId);

    /**
     * 更新库存
     *
     * @param dto
     * @return
     */
    int updateSaleNum(StoreGoodsQuotationGoodsSaleNumDTO dto);
}
