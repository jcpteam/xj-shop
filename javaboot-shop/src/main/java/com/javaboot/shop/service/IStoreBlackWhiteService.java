package com.javaboot.shop.service;

import com.javaboot.shop.domain.StoreBlackWhite;
import com.javaboot.shop.vo.StoreBlackWhiteVO;
import com.javaboot.shop.vo.StorePaymentAccountVO;

import java.util.List;

/**
 * 黑白名单Service接口
 * 
 * @author lqh
 * @date 2021-07-08
 */
public interface IStoreBlackWhiteService {
    /**
     * 查询黑白名单
     * 
     * @param blackWhiteId 黑白名单ID
     * @return 黑白名单
     */
    public StoreBlackWhite selectStoreBlackWhiteById(Long blackWhiteId);

    /**
     * 查询黑白名单列表
     * 
     * @param storeBlackWhite 黑白名单
     * @return 黑白名单集合
     */
    public List<StoreBlackWhiteVO> selectStoreBlackWhiteList(StoreBlackWhite storeBlackWhite);

    /**
     * 新增黑白名单
     * 
     * @param storeBlackWhite 黑白名单
     * @return 结果
     */
    public int insertStoreBlackWhite(StoreBlackWhite storeBlackWhite);

    /**
     * 修改黑白名单
     * 
     * @param storeBlackWhite 黑白名单
     * @return 结果
     */
    public int updateStoreBlackWhite(StoreBlackWhite storeBlackWhite);

    /**
     * 批量删除黑白名单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreBlackWhiteByIds(String ids);

    /**
     * 删除黑白名单信息
     * 
     * @param blackWhiteId 黑白名单ID
     * @return 结果
     */
    public int deleteStoreBlackWhiteById(Long blackWhiteId);

    public int updateStoreBlackWhiteType(StoreBlackWhite storeBlackWhite);

    public void updateBlackOrWhiteStatus(StorePaymentAccountVO vo);
}
