package com.javaboot.shop.mapper;

import com.javaboot.common.core.dao.BaseMapper;
import com.javaboot.shop.domain.StoreWarehouseStock;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;
import com.javaboot.shop.vo.StoreWarehouseStockVO;

import java.util.List;
import java.util.Map;

/**
 * 商品入库Mapper接口
 * 
 * @author lqh
 * @date 2021-05-20
 */
public interface StoreWarehouseStockMapper extends BaseMapper<StoreWarehouseStock> {
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
     * @param storeWarehouseStock 商品入库
     * @return 结果
     */
    public int insertStoreWarehouseStock(StoreWarehouseStock storeWarehouseStock);

    /**
     * 修改商品入库
     * 
     * @param storeWarehouseStock 商品入库
     * @return 结果
     */
    public int updateStoreWarehouseStock(StoreWarehouseStock storeWarehouseStock);

    /**
     * 删除商品入库
     * 
     * @param stockId 商品入库ID
     * @return 结果
     */
    public int deleteStoreWarehouseStockById(Long stockId);

    /**
     * 批量删除商品入库
     * 
     * @param stockIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreWarehouseStockByIds(String[] stockIds);

    int updateStockAuditStatusByIds(Map<String, Object> map);
}
