package com.javaboot.shop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.javaboot.common.enums.OrderLogType;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.dto.StoreGoodsOrderLogQueryDTO;
import com.javaboot.shop.vo.StoreGoodsOrderLogVO;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsOrderLogMapper;
import com.javaboot.shop.domain.StoreGoodsOrderLog;
import com.javaboot.shop.service.IStoreGoodsOrderLogService;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 记录调整时候的退货或者补货Service业务层处理
 *
 * @author lqh
 * @date 2021-06-03
 */
@Service
public class StoreGoodsOrderLogServiceImpl implements IStoreGoodsOrderLogService {
    @Autowired
    private StoreGoodsOrderLogMapper storeGoodsOrderLogMapper;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询记录调整时候的退货或者补货
     *
     * @param logId 记录调整时候的退货或者补货ID
     * @return 记录调整时候的退货或者补货
     */
    @Override
    public StoreGoodsOrderLog selectStoreGoodsOrderLogById(Long logId) {
        return storeGoodsOrderLogMapper.selectStoreGoodsOrderLogById(logId);
    }

    /**
     * 查询记录调整时候的退货或者补货列表
     *
     * @param storeGoodsOrderLog 记录调整时候的退货或者补货
     * @return 记录调整时候的退货或者补货
     */
    @Override
    public List<StoreGoodsOrderLogVO> selectStoreGoodsOrderLogList(StoreGoodsOrderLogQueryDTO storeGoodsOrderLog) {
        List<StoreGoodsOrderLogVO> voList = storeGoodsOrderLogMapper.selectStoreGoodsOrderLogList(storeGoodsOrderLog);
        if (CollectionUtils.isNotEmpty(voList)) {
            List<SysUser> userList = sysUserService.selectUserByIds(voList.stream().map(StoreGoodsOrderLog::getOperateUserId).collect(Collectors.toList()));
            voList.forEach(v -> {
                v.setTypeName(OrderLogType.getDescValue(v.getType()));
                if (CollectionUtils.isNotEmpty(userList)) {
                    SysUser user = userList.stream().filter(u -> u.getUserId().toString().equals(v.getOperateUserId())).findFirst().orElse(null);
                    if (user != null) {
                        v.setOperateUserName(user.getUserName());
                    }
                }
            });
        }
        return voList;
    }

    /**
     * 新增记录调整时候的退货或者补货
     *
     * @param storeGoodsOrderLog 记录调整时候的退货或者补货
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodsOrderLog(StoreGoodsOrderLog storeGoodsOrderLog) {
        storeGoodsOrderLog.setCreateTime(DateUtils.getNowDate());
        return storeGoodsOrderLogMapper.insertStoreGoodsOrderLog(storeGoodsOrderLog);
    }

    /**
     * 修改记录调整时候的退货或者补货
     *
     * @param storeGoodsOrderLog 记录调整时候的退货或者补货
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodsOrderLog(StoreGoodsOrderLog storeGoodsOrderLog) {
        return storeGoodsOrderLogMapper.updateStoreGoodsOrderLog(storeGoodsOrderLog);
    }

    /**
     * 删除记录调整时候的退货或者补货对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsOrderLogByIds(String ids) {
        return storeGoodsOrderLogMapper.deleteStoreGoodsOrderLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除记录调整时候的退货或者补货信息
     *
     * @param logId 记录调整时候的退货或者补货ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsOrderLogById(Long logId) {
        return storeGoodsOrderLogMapper.deleteStoreGoodsOrderLogById(logId);
    }
}
