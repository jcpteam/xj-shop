package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreBlackWhite;
import com.javaboot.shop.vo.StoreBlackWhiteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 黑白名单Mapper接口
 * 
 * @author lqh
 * @date 2021-07-08
 */
public interface StoreBlackWhiteMapper {
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
     * 删除黑白名单
     * 
     * @param blackWhiteId 黑白名单ID
     * @return 结果
     */
    public int deleteStoreBlackWhiteById(Long blackWhiteId);

    /**
     * 批量删除黑白名单
     * 
     * @param blackWhiteIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreBlackWhiteByIds(String[] blackWhiteIds);

    /**
     * 删除黑白名单
     *
     * @param accountId 支付账号id
     * @return 结果
     */
    public int deleteStoreBlackWhiteByAccount(String  accountId);

    public int updateStoreBlackWhiteType(Map<String, Object> map);
}
