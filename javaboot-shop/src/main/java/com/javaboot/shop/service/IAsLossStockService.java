package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.AsLossStock;

import java.util.List;

/**
 * lossService接口
 * 
 * @author javaboot
 * @date 2022-01-20
 */
public interface IAsLossStockService extends IService<AsLossStock>
{
    /**
     * 查询loss
     * 
     * @param spuNo lossID
     * @return loss
     */
    public AsLossStock selectAsLossStockById(String spuNo);

    /**
     * 查询loss列表
     * 
     * @param asLossStock loss
     * @return loss集合
     */
    public List<AsLossStock> selectAsLossStockList(AsLossStock asLossStock);

    /**
     * 新增loss
     * 
     * @param asLossStock loss
     * @return 结果
     */
    public int insertAsLossStock(AsLossStock asLossStock);

    /**
     * 修改loss
     * 
     * @param asLossStock loss
     * @return 结果
     */
    public int updateAsLossStock(AsLossStock asLossStock);

    /**
     * 批量删除loss
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAsLossStockByIds(String ids);

    /**
     * 删除loss信息
     * 
     * @param spuNo lossID
     * @return 结果
     */
    public int deleteAsLossStockById(String spuNo);
}
