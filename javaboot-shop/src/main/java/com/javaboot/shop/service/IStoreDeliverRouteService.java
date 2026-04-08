package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreDeliverRoute;
import java.util.List;

/**
 * 线路信息Service接口
 * 
 * @author javaboot
 * @date 2021-05-25
 */
public interface IStoreDeliverRouteService {
    /**
     * 查询线路信息
     * 
     * @param routeId 线路信息ID
     * @return 线路信息
     */
    public StoreDeliverRoute selectStoreDeliverRouteById(Long routeId);

    /**
     * 查询线路信息列表
     * 
     * @param storeDeliverRoute 线路信息
     * @return 线路信息集合
     */
    public List<StoreDeliverRoute> selectStoreDeliverRouteList(StoreDeliverRoute storeDeliverRoute);
    public List<StoreDeliverRoute> selectStoreDeliverRouteListForExport(StoreDeliverRoute storeDeliverRoute);

    /**
     * 新增线路信息
     * 
     * @param storeDeliverRoute 线路信息
     * @return 结果
     */
    public int insertStoreDeliverRoute(StoreDeliverRoute storeDeliverRoute);

    /**
     * 修改线路信息
     * 
     * @param storeDeliverRoute 线路信息
     * @return 结果
     */
    public int updateStoreDeliverRoute(StoreDeliverRoute storeDeliverRoute);

    /**
     * 批量删除线路信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreDeliverRouteByIds(String ids);

    /**
     * 删除线路信息信息
     * 
     * @param routeId 线路信息ID
     * @return 结果
     */
    public int deleteStoreDeliverRouteById(Long routeId);
}
