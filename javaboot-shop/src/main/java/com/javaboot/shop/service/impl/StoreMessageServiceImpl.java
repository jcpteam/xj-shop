package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreMessageMapper;
import com.javaboot.shop.domain.StoreMessage;
import com.javaboot.shop.service.IStoreMessageService;
import com.javaboot.common.core.text.Convert;

/**
 * 消息Service业务层处理
 * 
 * @author javaboot
 * @date 2021-11-25
 */
@Service
public class StoreMessageServiceImpl implements IStoreMessageService {
    @Autowired
    private StoreMessageMapper storeMessageMapper;

    /**
     * 查询消息
     * 
     * @param id 消息ID
     * @return 消息
     */
    @Override
    public StoreMessage selectStoreMessageById(Long id) {
        return storeMessageMapper.selectStoreMessageById(id);
    }

    /**
     * 查询消息列表
     * 
     * @param storeMessage 消息
     * @return 消息
     */
    @Override
    public List<StoreMessage> selectStoreMessageList(StoreMessage storeMessage) {
        return storeMessageMapper.selectStoreMessageList(storeMessage);
    }

    /**
     * 新增消息
     * 
     * @param storeMessage 消息
     * @return 结果
     */
    @Override
    public int insertStoreMessage(StoreMessage storeMessage) {
        storeMessage.setCreateTime(DateUtils.getNowDate());
        return storeMessageMapper.insertStoreMessage(storeMessage);
    }

    /**
     * 修改消息
     * 
     * @param storeMessage 消息
     * @return 结果
     */
    @Override
    public int updateStoreMessage(StoreMessage storeMessage) {
        return storeMessageMapper.updateStoreMessage(storeMessage);
    }

    /**
     * 删除消息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreMessageByIds(String ids) {
        return storeMessageMapper.deleteStoreMessageByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除消息信息
     * 
     * @param id 消息ID
     * @return 结果
     */
    @Override
    public int deleteStoreMessageById(Long id) {
        return storeMessageMapper.deleteStoreMessageById(id);
    }
}
