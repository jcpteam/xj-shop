package com.javaboot.shop.service.impl;

import java.util.List;
import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreSortingBoxMapper;
import com.javaboot.shop.domain.StoreSortingBox;
import com.javaboot.shop.service.IStoreSortingBoxService;
import com.javaboot.common.core.text.Convert;

/**
 * 分拣框信息Service业务层处理
 * 
 * @author javaboot
 * @date 2021-06-11
 */
@Service
public class StoreSortingBoxServiceImpl implements IStoreSortingBoxService {
    @Autowired
    private StoreSortingBoxMapper storeSortingBoxMapper;

    /**
     * 查询分拣框信息
     * 
     * @param id 分拣框信息ID
     * @return 分拣框信息
     */
    @Override
    public StoreSortingBox selectStoreSortingBoxById(Long id) {
        return storeSortingBoxMapper.selectStoreSortingBoxById(id);
    }

    /**
     * 查询分拣框信息列表
     * 
     * @param storeSortingBox 分拣框信息
     * @return 分拣框信息
     */
    @Override
    public List<StoreSortingBox> selectStoreSortingBoxList(StoreSortingBox storeSortingBox) {
        return storeSortingBoxMapper.selectStoreSortingBoxList(storeSortingBox);
    }

    /**
     * 新增分拣框信息
     * 
     * @param storeSortingBox 分拣框信息
     * @return 结果
     */
    @Override
    public int insertStoreSortingBox(StoreSortingBox storeSortingBox) {
        storeSortingBox.setCreateTime(DateUtils.getNowDate());
        return storeSortingBoxMapper.insertStoreSortingBox(storeSortingBox);
    }

    /**
     * 修改分拣框信息
     * 
     * @param storeSortingBox 分拣框信息
     * @return 结果
     */
    @Override
    public int updateStoreSortingBox(StoreSortingBox storeSortingBox) {
        return storeSortingBoxMapper.updateStoreSortingBox(storeSortingBox);
    }

    /**
     * 删除分拣框信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreSortingBoxByIds(String ids) {
        return storeSortingBoxMapper.deleteStoreSortingBoxByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除分拣框信息信息
     * 
     * @param id 分拣框信息ID
     * @return 结果
     */
    @Override
    public int deleteStoreSortingBoxById(Long id) {
        return storeSortingBoxMapper.deleteStoreSortingBoxById(id);
    }
}
