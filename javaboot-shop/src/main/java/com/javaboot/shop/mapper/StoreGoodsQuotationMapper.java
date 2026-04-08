package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.dto.StoreGoodsQuotationQueryDTO;

import java.util.List;

/**
 * 报价信息Mapper接口
 *
 * @author javaboot
 * @date 2021-05-18
 */
public interface StoreGoodsQuotationMapper {
    /**
     * 查询报价信息
     *
     * @param quotationid 报价信息ID
     * @return 报价信息
     */
    StoreGoodsQuotation selectStoreGoodsQuotationById(Long quotationid);

    /**
     * 查询报价信息列表
     *
     * @param storeGoodsQuotation 报价信息
     * @return 报价信息集合
     */
    List<StoreGoodsQuotation> selectStoreGoodsQuotationList(StoreGoodsQuotationQueryDTO storeGoodsQuotation);

    /**
     * 新增报价信息
     *
     * @param storeGoodsQuotation 报价信息
     * @return 结果
     */
    int insertStoreGoodsQuotation(StoreGoodsQuotation storeGoodsQuotation);

    /**
     * 修改报价信息
     *
     * @param storeGoodsQuotation 报价信息
     * @return 结果
     */
    int updateStoreGoodsQuotation(StoreGoodsQuotation storeGoodsQuotation);

    /**
     * 删除报价信息
     *
     * @param quotationid 报价信息ID
     * @return 结果
     */
    public int deleteStoreGoodsQuotationById(Long quotationid);

    /**
     * 批量删除报价信息
     *
     * @param quotationids 需要删除的数据ID
     * @return 结果
     */
    int deleteStoreGoodsQuotationByIds(String[] quotationids);

    /**
     * 批量软删除报价信息
     *
     * @param quotationids 需要删除的数据ID
     * @return 结果
     */
    int removeStoreGoodsQuotationByIds(String[] quotationids);
}
