package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreAccountFlow;
import com.javaboot.shop.dto.StoreAccountFlowQueryDTO;
import com.javaboot.shop.vo.StoreAccountFlowVO;

import java.util.List;

/**
 * 账户流水Service接口
 * 
 * @author lqh
 * @date 2021-07-09
 */
public interface IStoreAccountFlowService {
    /**
     * 查询账户流水
     * 
     * @param flowId 账户流水ID
     * @return 账户流水
     */
    public StoreAccountFlow selectStoreAccountFlowById(Long flowId);

    /**
     * 查询账户流水列表
     * 
     * @param storeAccountFlow 账户流水
     * @return 账户流水集合
     */
    public List<StoreAccountFlowVO> selectStoreAccountFlowList(StoreAccountFlowQueryDTO storeAccountFlow);

    /**
     * 新增账户流水
     * 
     * @param storeAccountFlow 账户流水
     * @return 结果
     */
    public int insertStoreAccountFlow(StoreAccountFlow storeAccountFlow);

    /**
     * 修改账户流水
     * 
     * @param storeAccountFlow 账户流水
     * @return 结果
     */
    public int updateStoreAccountFlow(StoreAccountFlow storeAccountFlow);

    /**
     * 批量删除账户流水
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreAccountFlowByIds(String ids);

    /**
     * 删除账户流水信息
     * 
     * @param flowId 账户流水ID
     * @return 结果
     */
    public int deleteStoreAccountFlowById(Long flowId);
}
