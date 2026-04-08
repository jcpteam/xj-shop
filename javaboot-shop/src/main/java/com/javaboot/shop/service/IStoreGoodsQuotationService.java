package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.dto.StoreGoodsQuotationQueryDTO;

import java.util.List;

/**
 * 报价信息Service接口
 *
 * @author lqh
 * @date 2021-05-18
 */
public interface IStoreGoodsQuotationService {
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
     * 根据登陆用户或报价单查询
     * @param storeGoodsQuotation
     * @return
     */
    List<StoreGoodsQuotation> selectStoreGoodsQuotationListOfUserOrQuotation(StoreGoodsQuotationQueryDTO storeGoodsQuotation);

    /**
     * 新增报价信息
     *
     * @param storeGoodsQuotation 报价信息
     * @return 结果
     */
    int insertStoreGoodsQuotation(StoreGoodsQuotation storeGoodsQuotation);

    /**
     * 复制报价信息
     * @param storeGoodsQuotation
     * @return
     */
    int copyStoreGoodsQuotation(StoreGoodsQuotation storeGoodsQuotation);

    /**
     * 修改报价信息
     *
     * @param storeGoodsQuotation 报价信息
     * @return 结果
     */
    int updateStoreGoodsQuotation(StoreGoodsQuotation storeGoodsQuotation);

    /**
     * 批量删除报价信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteStoreGoodsQuotationByIds(String ids);

    /**
     * 批量软删除报价信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int removeStoreGoodsQuotationByIds(String ids);

    /**
     * 删除报价信息信息
     *
     * @param quotationid 报价信息ID
     * @return 结果
     */
    int deleteStoreGoodsQuotationById(Long quotationid);
}
