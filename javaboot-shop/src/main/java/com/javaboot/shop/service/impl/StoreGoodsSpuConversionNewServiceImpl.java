package com.javaboot.shop.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsSpuConversionNewMapper;
import com.javaboot.shop.domain.StoreGoodsSpuConversionNew;
import com.javaboot.shop.service.IStoreGoodsSpuConversionNewService;
import com.javaboot.common.core.text.Convert;

/**
 * spu转换比例Service业务层处理
 * 
 * @author javaboot
 * @date 2022-03-05
 */
@Service
public class StoreGoodsSpuConversionNewServiceImpl implements IStoreGoodsSpuConversionNewService {
    @Autowired
    private StoreGoodsSpuConversionNewMapper storeGoodsSpuConversionNewMapper;

    /**
     * 查询spu转换比例
     * 
     * @param id spu转换比例ID
     * @return spu转换比例
     */
    @Override
    public StoreGoodsSpuConversionNew selectStoreGoodsSpuConversionNewById(Long id) {
        return storeGoodsSpuConversionNewMapper.selectStoreGoodsSpuConversionNewById(id);
    }

    /**
     * 查询spu转换比例列表
     * 
     * @param storeGoodsSpuConversionNew spu转换比例
     * @return spu转换比例
     */
    @Override
    public List<StoreGoodsSpuConversionNew> selectStoreGoodsSpuConversionNewList(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew) {
        return storeGoodsSpuConversionNewMapper.selectStoreGoodsSpuConversionNewList(storeGoodsSpuConversionNew);
    }

    /**
     * 新增spu转换比例
     * 
     * @param storeGoodsSpuConversionNew spu转换比例
     * @return 结果
     */
    @Override
    public int insertStoreGoodsSpuConversionNew(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew) {
        return storeGoodsSpuConversionNewMapper.insertStoreGoodsSpuConversionNew(storeGoodsSpuConversionNew);
    }

    /**
     * 修改spu转换比例
     * 
     * @param storeGoodsSpuConversionNew spu转换比例
     * @return 结果
     */
    @Override
    public int updateStoreGoodsSpuConversionNew(StoreGoodsSpuConversionNew storeGoodsSpuConversionNew) {
        return storeGoodsSpuConversionNewMapper.updateStoreGoodsSpuConversionNew(storeGoodsSpuConversionNew);
    }

    /**
     * 删除spu转换比例对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSpuConversionNewByIds(String ids) {
        return storeGoodsSpuConversionNewMapper.deleteStoreGoodsSpuConversionNewByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除spu转换比例信息
     * 
     * @param id spu转换比例ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSpuConversionNewById(Long id) {
        return storeGoodsSpuConversionNewMapper.deleteStoreGoodsSpuConversionNewById(id);
    }
}
