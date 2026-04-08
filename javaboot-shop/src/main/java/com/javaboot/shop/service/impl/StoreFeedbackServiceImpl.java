package com.javaboot.shop.service.impl;

import java.util.List;

import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreFeedbackMapper;
import com.javaboot.shop.domain.StoreFeedback;
import com.javaboot.shop.service.IStoreFeedbackService;
import com.javaboot.common.core.text.Convert;

/**
 * 留言反馈Service业务层处理
 *
 * @author javaboot
 * @date 2020-03-21
 */
@Service
public class StoreFeedbackServiceImpl implements IStoreFeedbackService {
    @Autowired
    private StoreFeedbackMapper storeFeedbackMapper;

    /**
     * 查询留言反馈
     *
     * @param id 留言反馈ID
     * @return 留言反馈
     */
    @Override
    public StoreFeedback selectStoreFeedbackById(Integer id) {
        return storeFeedbackMapper.selectStoreFeedbackById(id);
    }

    /**
     * 查询留言反馈列表
     *
     * @param storeFeedback 留言反馈
     * @return 留言反馈
     */
    @Override
    public List<StoreFeedback> selectStoreFeedbackList(StoreFeedback storeFeedback) {
        return storeFeedbackMapper.selectStoreFeedbackList(storeFeedback);
    }

    /**
     * 新增留言反馈
     *
     * @param storeFeedback 留言反馈
     * @return 结果
     */
    @Override
    public int insertStoreFeedback(StoreFeedback storeFeedback) {
        storeFeedback.setCreateTime(DateUtils.getNowDate());
        return storeFeedbackMapper.insertStoreFeedback(storeFeedback);
    }

    /**
     * 修改留言反馈
     *
     * @param storeFeedback 留言反馈
     * @return 结果
     */
    @Override
    public int updateStoreFeedback(StoreFeedback storeFeedback) {
        return storeFeedbackMapper.updateStoreFeedback(storeFeedback);
    }

    /**
     * 删除留言反馈对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreFeedbackByIds(String ids) {
        return storeFeedbackMapper.deleteStoreFeedbackByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除留言反馈信息
     *
     * @param id 留言反馈ID
     * @return 结果
     */
    @Override
    public int deleteStoreFeedbackById(Integer id) {
        return storeFeedbackMapper.deleteStoreFeedbackById(id);
    }
}
