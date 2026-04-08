package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import com.javaboot.shop.domain.StoreSaleSetting;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsQueryDTO;
import com.javaboot.shop.dto.StoreSaleSettingDTO;
import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;

import java.util.List;

/**
 * 商品上数设置Service接口
 * 
 * @author lqh
 * @date 2021-05-19
 */
public interface IStoreSaleSettingService extends IService<StoreSaleSetting>
{
    /**
     * 查询商品上数设置
     * 
     * @param settingid 商品上数设置ID
     * @return 商品上数设置
     */
    public StoreSaleSetting selectStoreSaleSettingById(Long settingid);

    /**
     * 查询商品上数设置列表
     * 
     * @param storeSaleSetting 商品上数设置
     * @return 商品上数设置集合
     */
    List<StoreSaleSetting> selectStoreSaleSettingList(StoreSaleSetting storeSaleSetting);

    /**
     * 上数SPU商品详情
     * @param dto
     * @return
     */
    List<StoreGoodsQuotationGoodsVO>  quotationGoodsList(StoreGoodsQuotationGoodsQueryDTO dto);

    /**
     * 新增商品上数设置
     * 
     * @param storeSaleSetting 商品上数设置
     * @return 结果
     */
    public int insertStoreSaleSetting(StoreSaleSetting storeSaleSetting);

    /**
     * 修改商品上数设置
     * 
     * @param dto 商品上数设置
     * @return 结果
     */
    public int updateStoreSaleSetting(StoreSaleSettingDTO dto);

    /**
     * 批量删除商品上数设置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreSaleSettingByIds(String ids);

    /**
     * 删除商品上数设置信息
     * 
     * @param settingid 商品上数设置ID
     * @return 结果
     */
    public int deleteStoreSaleSettingById(Long settingid);

    /**
     * 初始化上数数据
     *
     * @param deptId
     * @param settingDate
     */
    int initSaleData(String deptId, String settingDate);

    /**
     * 修改浮动上数数量
     *
     * @param storeSaleSetting
     * @return
     */
    int updateSelfQuanintiy(StoreSaleSetting storeSaleSetting);


    /**
     * 查询商品上数设置列表
     *
     * @param storeSaleSetting 商品上数设置
     * @return 商品上数设置集合
     */
    List<StoreSaleSetting> selectStoreSaleSettingListForApp(StoreSaleSetting storeSaleSetting);
}
