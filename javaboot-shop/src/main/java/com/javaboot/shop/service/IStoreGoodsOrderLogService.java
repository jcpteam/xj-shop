package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreGoodsOrderLog;
import com.javaboot.shop.dto.StoreGoodsOrderLogQueryDTO;
import com.javaboot.shop.vo.StoreGoodsOrderLogVO;

import java.util.List;

/**
 * 记录调整时候的退货或者补货Service接口
 * 
 * @author lqh
 * @date 2021-06-03
 */
public interface IStoreGoodsOrderLogService {
    /**
     * 查询记录调整时候的退货或者补货
     * 
     * @param logId 记录调整时候的退货或者补货ID
     * @return 记录调整时候的退货或者补货
     */
    public StoreGoodsOrderLog selectStoreGoodsOrderLogById(Long logId);

    /**
     * 查询记录调整时候的退货或者补货列表
     * 
     * @param storeGoodsOrderLog 记录调整时候的退货或者补货
     * @return 记录调整时候的退货或者补货集合
     */
    public List<StoreGoodsOrderLogVO> selectStoreGoodsOrderLogList(StoreGoodsOrderLogQueryDTO storeGoodsOrderLog);

    /**
     * 新增记录调整时候的退货或者补货
     * 
     * @param storeGoodsOrderLog 记录调整时候的退货或者补货
     * @return 结果
     */
    public int insertStoreGoodsOrderLog(StoreGoodsOrderLog storeGoodsOrderLog);

    /**
     * 修改记录调整时候的退货或者补货
     * 
     * @param storeGoodsOrderLog 记录调整时候的退货或者补货
     * @return 结果
     */
    public int updateStoreGoodsOrderLog(StoreGoodsOrderLog storeGoodsOrderLog);

    /**
     * 批量删除记录调整时候的退货或者补货
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderLogByIds(String ids);

    /**
     * 删除记录调整时候的退货或者补货信息
     * 
     * @param logId 记录调整时候的退货或者补货ID
     * @return 结果
     */
    public int deleteStoreGoodsOrderLogById(Long logId);
}
