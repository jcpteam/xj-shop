package com.javaboot.shop.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreGoodsSpuUnitConversion;
import com.javaboot.shop.mapper.StoreGoodsSpuUnitConversionMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StoreGoodsSpuUnitMapper;
import com.javaboot.shop.domain.StoreGoodsSpuUnit;
import com.javaboot.shop.service.IStoreGoodsSpuUnitService;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * spu单位Service业务层处理
 * 
 * @author lqh
 * @date 2021-06-20
 */
@Service
public class StoreGoodsSpuUnitServiceImpl implements IStoreGoodsSpuUnitService {
    @Autowired
    private StoreGoodsSpuUnitMapper storeGoodsSpuUnitMapper;

    @Autowired
    private StoreGoodsSpuUnitConversionMapper storeGoodsSpuUnitConversionMapper;


    /**
     * 查询spu单位
     * 
     * @param spuNo spu单位ID
     * @return spu单位
     */
    @Override
    public StoreGoodsSpuUnit selectStoreGoodsSpuUnitById(String spuNo) {
        return storeGoodsSpuUnitMapper.selectStoreGoodsSpuUnitById(spuNo);
    }

    /**
     * 查询spu单位列表
     * 
     * @param storeGoodsSpuUnit spu单位
     * @return spu单位
     */
    @Override
    public List<StoreGoodsSpuUnit> selectStoreGoodsSpuUnitList(StoreGoodsSpuUnit storeGoodsSpuUnit) {
        return storeGoodsSpuUnitMapper.selectStoreGoodsSpuUnitList(storeGoodsSpuUnit);
        
    }

    /**
     * 新增spu单位
     * 
     * @param storeGoodsSpuUnit spu单位
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodsSpuUnit(StoreGoodsSpuUnit storeGoodsSpuUnit) {
        Map<String, Object> map = new HashMap<>();
        map.put("deptId", "0");
        map.put("spuNos", Convert.toStrArray(storeGoodsSpuUnit.getSpuNos()));
        storeGoodsSpuUnitConversionMapper.deleteSpuUnitConversionBySpus(map);
        deleteStoreGoodsSpuUnitByIds(storeGoodsSpuUnit.getSpuNos());
        int size=0;
        for (String spu:storeGoodsSpuUnit.getSpuNoList()){
            StoreGoodsSpuUnit unit=new StoreGoodsSpuUnit();
            unit.setSpuNo(spu);
            unit.setMainUnitId(storeGoodsSpuUnit.getMainUnitId());
            unit.setSubUnitId(storeGoodsSpuUnit.getSubUnitId());
            unit.setLastModifyTime(DateUtils.getNowDate());
            size+=storeGoodsSpuUnitMapper.insertStoreGoodsSpuUnit(unit);

            StoreGoodsSpuUnitConversion storeGoodsSpuUnitConversion = new StoreGoodsSpuUnitConversion();
            storeGoodsSpuUnitConversion.setSpuNo(spu);
            storeGoodsSpuUnitConversion.setDeptId("0");
            storeGoodsSpuUnitConversion.setMainCaseSub(1.0);
            storeGoodsSpuUnitConversion.setSubCaseMain(1.0);
            storeGoodsSpuUnitConversionMapper.insertStoreGoodsSpuUnitConversion(storeGoodsSpuUnitConversion);
        }
        return size==storeGoodsSpuUnit.getSpuNoList().size()?1:0;
    }

    /**
     * 修改spu单位
     * 
     * @param storeGoodsSpuUnit spu单位
     * @return 结果
     */
    @Override
    public int updateStoreGoodsSpuUnit(StoreGoodsSpuUnit storeGoodsSpuUnit) {
        return storeGoodsSpuUnitMapper.updateStoreGoodsSpuUnit(storeGoodsSpuUnit);
    }

    /**
     * 删除spu单位对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSpuUnitByIds(String ids) {
        return storeGoodsSpuUnitMapper.deleteStoreGoodsSpuUnitByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除spu单位信息
     * 
     * @param spuNo spu单位ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSpuUnitById(String spuNo) {
        return storeGoodsSpuUnitMapper.deleteStoreGoodsSpuUnitById(spuNo);
    }
}
