package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreFeedback;

import java.util.List;

/**
 * 留言反馈Service接口
 *
 * @author javaboot
 * @date 2020-03-21
 */
public interface IStoreFeedbackService {
    /**
     * 查询留言反馈
     *
     * @param id 留言反馈ID
     * @return 留言反馈
     */
    public StoreFeedback selectStoreFeedbackById(Integer id);

    /**
     * 查询留言反馈列表
     *
     * @param storeFeedback 留言反馈
     * @return 留言反馈集合
     */
    public List<StoreFeedback> selectStoreFeedbackList(StoreFeedback storeFeedback);

    /**
     * 新增留言反馈
     *
     * @param storeFeedback 留言反馈
     * @return 结果
     */
    public int insertStoreFeedback(StoreFeedback storeFeedback);

    /**
     * 修改留言反馈
     *
     * @param storeFeedback 留言反馈
     * @return 结果
     */
    public int updateStoreFeedback(StoreFeedback storeFeedback);

    /**
     * 批量删除留言反馈
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreFeedbackByIds(String ids);

    /**
     * 删除留言反馈信息
     *
     * @param id 留言反馈ID
     * @return 结果
     */
    public int deleteStoreFeedbackById(Integer id);
}
