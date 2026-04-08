package com.javaboot.shop.service.impl;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreGoodsClassify;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.domain.StoreGoodsSpu;
import com.javaboot.shop.domain.StoreGoodsSpuUnit;
import com.javaboot.shop.dto.StoreGoodsSpuDTO;
import com.javaboot.shop.mapper.StoreGoodsSpuMapper;
import com.javaboot.shop.service.IStoreGoodsClassifyService;
import com.javaboot.shop.service.IStoreGoodsSalesUnitService;
import com.javaboot.shop.service.IStoreGoodsSpuService;
import com.javaboot.shop.service.IStoreGoodsSpuUnitService;
import com.javaboot.shop.vo.StoreGoodsSpuVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品SPU信息Service业务层处理
 *
 * @author lqh
 * @date 2021-05-23
 */
@Service
public class StoreGoodsSpuServiceImpl implements IStoreGoodsSpuService {
    @Autowired
    private StoreGoodsSpuMapper storeGoodsSpuMapper;
    @Autowired
    private IStoreGoodsClassifyService classifyService;
    @Autowired
    private IStoreGoodsSpuUnitService storeGoodsSpuUnitService;
    @Autowired
    private IStoreGoodsSalesUnitService storeGoodsSalesUnitService;

    /**
     * 查询商品SPU信息
     *
     * @param spuNo 商品SPU信息ID
     * @return 商品SPU信息
     */
    @Override
    public StoreGoodsSpuVO selectStoreGoodsSpuById(String spuNo) {
        return storeGoodsSpuMapper.selectStoreGoodsSpuById(spuNo);
    }

    /**
     * 查询商品SPU信息列表
     *
     * @param storeGoodsSpu 商品SPU信息
     * @return 商品SPU信息
     */
    @Override
    public List<StoreGoodsSpuVO> selectStoreGoodsSpuList(StoreGoodsSpuDTO storeGoodsSpu) {
        List<StoreGoodsSpuVO> list = storeGoodsSpuMapper.selectStoreGoodsSpuList(storeGoodsSpu);
        if (CollectionUtils.isNotEmpty(list)) {
            //查询分类名称
            StoreGoodsClassify storeGoodClassify = new StoreGoodsClassify();
            storeGoodClassify.setStatus(Constants.NORMAL);
            List<StoreGoodsClassify> classifyList = classifyService.selectStoreGoodClassifyList(null);
            StoreGoodsSpuUnit query = new StoreGoodsSpuUnit();
            query.setSpuNoList(list.stream().map(StoreGoodsSpu::getSpuNo).collect(Collectors.toList()));
            List<StoreGoodsSpuUnit> spuUnits = storeGoodsSpuUnitService.selectStoreGoodsSpuUnitList(query);
            if (CollectionUtils.isNotEmpty(spuUnits)) {
                List<StoreGoodsSalesUnit> salesUnits = storeGoodsSalesUnitService.getNormalSpecificationsList();
                if (CollectionUtils.isNotEmpty(salesUnits)) {
                    spuUnits.forEach(s -> {
                        StoreGoodsSalesUnit main = salesUnits.stream().filter(u -> u.getUnitId().equals(s.getMainUnitId())).findFirst().orElse(null);
                        StoreGoodsSalesUnit sub = salesUnits.stream().filter(u -> u.getUnitId().equals(s.getSubUnitId())).findFirst().orElse(null);
                        s.setMainUnit(main);
                        s.setSubUnit(sub);
                        s.setUnitList(Arrays.asList(main,sub));
                    });
                }
            }
            for (StoreGoodsSpuVO vo : list) {
                //设置分类名称
                if (CollectionUtils.isNotEmpty(classifyList)) {
                    StoreGoodsClassify classify = classifyList.stream().filter(e -> e.getClassifyId().equals(vo.getClassifyId())).findFirst().orElse(null);
                    if (classify != null) {
                        StoreGoodsClassify parentClassify = classifyList.stream().filter(e -> e.getClassifyId().equals(classify.getParentId())).findFirst().orElse(null);
                        if (parentClassify != null) {
                            vo.setClassifyName(parentClassify.getName() + classify.getName());
                        } else {
                            vo.setClassifyName(classify.getName());
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(spuUnits)) {
                    StoreGoodsSpuUnit storeGoodsSpuUnit = spuUnits.stream().filter(s -> s.getSpuNo().equals(vo.getSpuNo())).findFirst().orElse(null);
                    if (storeGoodsSpuUnit != null && storeGoodsSpuUnit.getMainUnit() != null && storeGoodsSpuUnit.getSubUnit() != null) {
                        vo.setMainUnitName(storeGoodsSpuUnit.getMainUnit().getName());
                        vo.setSubUnitName(storeGoodsSpuUnit.getSubUnit().getName());
                        vo.setSpuUnitList(storeGoodsSpuUnit.getUnitList());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增商品SPU信息
     *
     * @param storeGoodsSpu 商品SPU信息
     * @return 结果
     */
    @Override
    public int insertStoreGoodsSpu(StoreGoodsSpu storeGoodsSpu) {
        storeGoodsSpu.setCreateTime(DateUtils.getNowDate());
        return storeGoodsSpuMapper.insertStoreGoodsSpu(storeGoodsSpu);
    }

    /**
     * 修改商品SPU信息
     *
     * @param storeGoodsSpu 商品SPU信息
     * @return 结果
     */
    @Override
    public int updateStoreGoodsSpu(StoreGoodsSpu storeGoodsSpu) {
        return storeGoodsSpuMapper.updateStoreGoodsSpu(storeGoodsSpu);
    }

    /**
     * 删除商品SPU信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSpuByIds(String ids) {
        return storeGoodsSpuMapper.deleteStoreGoodsSpuByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品SPU信息信息
     *
     * @param spuNo 商品SPU信息ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsSpuById(Long spuNo) {
        return storeGoodsSpuMapper.deleteStoreGoodsSpuById(spuNo);
    }


}
