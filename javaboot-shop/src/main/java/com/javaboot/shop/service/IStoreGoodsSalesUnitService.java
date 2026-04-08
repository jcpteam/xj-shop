package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsSalesUnit;

import java.util.List;

/**
 * 销售单位Service接口
 *
 * @author lqh
 * @date 2021-05-29
 */
public interface IStoreGoodsSalesUnitService {
    /**
     * 查询销售单位
     *
     * @param unitId 销售单位ID
     * @return 销售单位
     */
    public StoreGoodsSalesUnit selectStoreGoodsSalesUnitById(Long unitId);

    /**
     * 查询销售单位列表
     *
     * @param storeGoodsSalesUnit 销售单位
     * @return 销售单位集合
     */
    public List<StoreGoodsSalesUnit> selectStoreGoodsSalesUnitList(StoreGoodsSalesUnit storeGoodsSalesUnit);

    /**
     * 新增销售单位
     *
     * @param storeGoodsSalesUnit 销售单位
     * @return 结果
     */
    public int insertStoreGoodsSalesUnit(StoreGoodsSalesUnit storeGoodsSalesUnit);

    /**
     * 修改销售单位
     *
     * @param storeGoodsSalesUnit 销售单位
     * @return 结果
     */
    public int updateStoreGoodsSalesUnit(StoreGoodsSalesUnit storeGoodsSalesUnit);

    /**
     * 批量删除销售单位
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsSalesUnitByIds(String ids);

    /**
     * 删除销售单位信息
     *
     * @param unitId 销售单位ID
     * @return 结果
     */
    public int deleteStoreGoodsSalesUnitById(Long unitId);

    /**
     * 获取正常规格
     *
     * @return
     */
    List<StoreGoodsSalesUnit> getNormalSpecificationsList();

    /**
     * 获取spu商品单位
     * @return
     */
    List<StoreGoodsSalesUnit> getSpuUnit(String spuNo);
}
