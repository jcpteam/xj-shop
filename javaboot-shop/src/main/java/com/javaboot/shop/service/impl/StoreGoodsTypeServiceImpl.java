package com.javaboot.shop.service.impl;

import com.javaboot.common.core.text.Convert;
import com.javaboot.shop.domain.StoreGoodsType;
import com.javaboot.shop.mapper.StoreGoodsTypeMapper;
import com.javaboot.shop.service.IStoreGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品类型(商品模型)Service业务层处理
 *
 * @author javaboot
 * @date 2019-08-24
 */
@Service
public class StoreGoodsTypeServiceImpl implements IStoreGoodsTypeService {
    @Autowired
    private StoreGoodsTypeMapper storeGoodsTypeMapper;

    /**
     * 查询商品类型(商品模型)
     *
     * @param id 商品类型(商品模型)ID
     * @return 商品类型(商品模型)
     */
    @Override
    public StoreGoodsType selectStoreGoodsTypeById(Integer id) {
        return storeGoodsTypeMapper.selectStoreGoodsTypeById(id);
    }

    /**
     * 查询商品类型(商品模型)列表
     *
     * @param storeGoodsType 商品类型(商品模型)
     * @return 商品类型(商品模型)
     */
    @Override
    public List<StoreGoodsType> selectStoreGoodsTypeList(StoreGoodsType storeGoodsType) {
        return storeGoodsTypeMapper.selectStoreGoodsTypeList(storeGoodsType);
    }

    /**
     * 新增商品类型(商品模型)
     *
     * @param storeGoodsType 商品类型(商品模型)
     * @return 结果
     */
    @Override
    public int insertStoreGoodsType(StoreGoodsType storeGoodsType) {
        return storeGoodsTypeMapper.insertStoreGoodsType(storeGoodsType);
    }

    /**
     * 修改商品类型(商品模型)
     *
     * @param storeGoodsType 商品类型(商品模型)
     * @return 结果
     */
    @Override
    public int updateStoreGoodsType(StoreGoodsType storeGoodsType) {
        return storeGoodsTypeMapper.updateStoreGoodsType(storeGoodsType);
    }

    /**
     * 删除商品类型(商品模型)对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsTypeByIds(String ids) {
        return storeGoodsTypeMapper.deleteStoreGoodsTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品类型(商品模型)信息
     *
     * @param id 商品类型(商品模型)ID
     * @return 结果
     */
    public int deleteStoreGoodsTypeById(Integer id) {
        return storeGoodsTypeMapper.deleteStoreGoodsTypeById(id);
    }
}
