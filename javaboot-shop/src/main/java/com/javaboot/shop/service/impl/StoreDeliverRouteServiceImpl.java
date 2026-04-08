package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreDeliverRouteMapper;
import com.javaboot.shop.domain.StoreDeliverRoute;
import com.javaboot.shop.service.IStoreDeliverRouteService;
import com.javaboot.common.core.text.Convert;

/**
 * 线路信息Service业务层处理
 * 
 * @author javaboot
 * @date 2021-05-25
 */
@Service
public class StoreDeliverRouteServiceImpl implements IStoreDeliverRouteService {
    @Autowired
    private StoreDeliverRouteMapper storeDeliverRouteMapper;

    /**
     * 查询线路信息
     * 
     * @param routeId 线路信息ID
     * @return 线路信息
     */
    @Override
    public StoreDeliverRoute selectStoreDeliverRouteById(Long routeId) {
        return storeDeliverRouteMapper.selectStoreDeliverRouteById(routeId);
    }

    /**
     * 查询线路信息列表
     * 
     * @param storeDeliverRoute 线路信息
     * @return 线路信息
     */
    @Override
    public List<StoreDeliverRoute> selectStoreDeliverRouteList(StoreDeliverRoute storeDeliverRoute) {
        return storeDeliverRouteMapper.selectStoreDeliverRouteList(storeDeliverRoute);
    }

    @Override
    public List<StoreDeliverRoute> selectStoreDeliverRouteListForExport(StoreDeliverRoute storeDeliverRoute) {
        return storeDeliverRouteMapper.selectStoreDeliverRouteListForExport(storeDeliverRoute);
    }

    /**
     * 新增线路信息
     * 
     * @param storeDeliverRoute 线路信息
     * @return 结果
     */
    @Override
    public int insertStoreDeliverRoute(StoreDeliverRoute storeDeliverRoute) {
        storeDeliverRoute.setCreateTime(DateUtils.getNowDate());
        return storeDeliverRouteMapper.insertStoreDeliverRoute(storeDeliverRoute);
    }

    /**
     * 修改线路信息
     * 
     * @param storeDeliverRoute 线路信息
     * @return 结果
     */
    @Override
    public int updateStoreDeliverRoute(StoreDeliverRoute storeDeliverRoute) {
        return storeDeliverRouteMapper.updateStoreDeliverRoute(storeDeliverRoute);
    }

    /**
     * 删除线路信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreDeliverRouteByIds(String ids) {
        return storeDeliverRouteMapper.deleteStoreDeliverRouteByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除线路信息信息
     * 
     * @param routeId 线路信息ID
     * @return 结果
     */
    @Override
    public int deleteStoreDeliverRouteById(Long routeId) {
        return storeDeliverRouteMapper.deleteStoreDeliverRouteById(routeId);
    }
}
