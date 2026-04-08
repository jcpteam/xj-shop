package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreWarehouseStock;
import com.javaboot.shop.dto.StoreWarehouseStockDTO;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;
import com.javaboot.shop.vo.StoreWarehouseStockVO;

import java.util.List;

/**
 * 商品入库Service接口
 * 
 * @author lqh
 * @date 2021-05-20
 */
public interface IStoreWarehouseStockService {
    /**
     * 查询商品入库
     *
     * @param stockId 商品入库ID
     * @return 商品入库
     */
    public StoreWarehouseStockVO selectStoreWarehouseStockById(Long stockId);

    /**
     * 查询商品入库列表
     *
     * @param storeWarehouseStock 商品入库
     * @return 商品入库集合
     */
    public List<StoreWarehouseStockVO> selectStoreWarehouseStockList(StoreWarehouseStockQueryDTO storeWarehouseStock);

    /**
     * 新增商品入库
     *
     * @param dto 商品入库
     * @return 结果
     */
    public int insertStoreWarehouseStock(StoreWarehouseStockDTO dto);

    /**
     * EAS新增商品入库
     *
     * @param dto 商品入库
     * @return 结果
     */
    int insertEASStock(StoreWarehouseStockDTO dto);

    /**
     * 修改商品入库
     *
     * @param dto 商品入库
     * @return 结果
     */
    public int updateStoreWarehouseStock(StoreWarehouseStockDTO dto, boolean isReplace);

    /**
     * 批量删除商品入库
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreWarehouseStockByIds(String ids);

    /**
     * 删除商品入库信息
     *
     * @param stockId 商品入库ID
     * @return 结果
     */
    public int deleteStoreWarehouseStockById(Long stockId);

    public int auditStock(StoreWarehouseStockDTO dto);
}
