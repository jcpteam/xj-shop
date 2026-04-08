package com.javaboot.shop.service.impl;

import java.util.List;

import com.javaboot.common.enums.AccountFlowType;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.dto.StoreAccountFlowQueryDTO;
import com.javaboot.shop.vo.StoreAccountFlowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreAccountFlowMapper;
import com.javaboot.shop.domain.StoreAccountFlow;
import com.javaboot.shop.service.IStoreAccountFlowService;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户流水Service业务层处理
 * 
 * @author lqh
 * @date 2021-07-09
 */
@Service
public class StoreAccountFlowServiceImpl implements IStoreAccountFlowService {
    @Autowired
    private StoreAccountFlowMapper storeAccountFlowMapper;

    /**
     * 查询账户流水
     * 
     * @param flowId 账户流水ID
     * @return 账户流水
     */
    @Override
    public StoreAccountFlow selectStoreAccountFlowById(Long flowId) {
        return storeAccountFlowMapper.selectStoreAccountFlowById(flowId);
    }

    /**
     * 查询账户流水列表
     * 
     * @param storeAccountFlow 账户流水
     * @return 账户流水
     */
    @Override
    public List<StoreAccountFlowVO> selectStoreAccountFlowList(StoreAccountFlowQueryDTO storeAccountFlow) {
        List<StoreAccountFlowVO>  list= storeAccountFlowMapper.selectStoreAccountFlowList(storeAccountFlow);
        list.forEach(l->l.setTypeName(AccountFlowType.getDescValue(l.getType())));
        return list;
    }

    /**
     * 新增账户流水
     * 
     * @param storeAccountFlow 账户流水
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreAccountFlow(StoreAccountFlow storeAccountFlow) {
        storeAccountFlow.setCreateTime(DateUtils.getNowDate());
        return storeAccountFlowMapper.insertStoreAccountFlow(storeAccountFlow);
    }

    /**
     * 修改账户流水
     * 
     * @param storeAccountFlow 账户流水
     * @return 结果
     */
    @Override
    public int updateStoreAccountFlow(StoreAccountFlow storeAccountFlow) {
        return storeAccountFlowMapper.updateStoreAccountFlow(storeAccountFlow);
    }

    /**
     * 删除账户流水对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreAccountFlowByIds(String ids) {
        return storeAccountFlowMapper.deleteStoreAccountFlowByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除账户流水信息
     * 
     * @param flowId 账户流水ID
     * @return 结果
     */
    @Override
    public int deleteStoreAccountFlowById(Long flowId) {
        return storeAccountFlowMapper.deleteStoreAccountFlowById(flowId);
    }
}
