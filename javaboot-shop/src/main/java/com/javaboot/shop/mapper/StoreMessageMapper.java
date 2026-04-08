package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreMessage;
import java.util.List;

/**
 * 消息Mapper接口
 * 
 * @author javaboot
 * @date 2021-11-25
 */
public interface StoreMessageMapper {
    /**
     * 查询消息
     * 
     * @param id 消息ID
     * @return 消息
     */
    public StoreMessage selectStoreMessageById(Long id);

    /**
     * 查询消息列表
     * 
     * @param storeMessage 消息
     * @return 消息集合
     */
    public List<StoreMessage> selectStoreMessageList(StoreMessage storeMessage);

    /**
     * 新增消息
     * 
     * @param storeMessage 消息
     * @return 结果
     */
    public int insertStoreMessage(StoreMessage storeMessage);

    /**
     * 修改消息
     * 
     * @param storeMessage 消息
     * @return 结果
     */
    public int updateStoreMessage(StoreMessage storeMessage);

    /**
     * 删除消息
     * 
     * @param id 消息ID
     * @return 结果
     */
    public int deleteStoreMessageById(Long id);

    /**
     * 批量删除消息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreMessageByIds(String[] ids);
}
