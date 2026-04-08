package com.javaboot.shop.service.impl;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreGoodsClassify;
import com.javaboot.shop.mapper.StoreGoodsClassifyMapper;
import com.javaboot.shop.service.IStoreGoodsClassifyService;
import com.javaboot.shop.vo.StoreGoodsClassifyTreeVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品分类Service业务层处理
 *
 * @author lqh
 * @date 2021-05-23
 */
@Service
public class StoreGoodsClassifyServiceImpl implements IStoreGoodsClassifyService {
    @Autowired
    private StoreGoodsClassifyMapper storeGoodClassifyMapper;

    /**
     * 查询商品分类
     *
     * @param classifyId 商品分类ID
     * @return 商品分类
     */
    @Override
    public StoreGoodsClassify selectStoreGoodClassifyById(String classifyId) {
        return storeGoodClassifyMapper.selectStoreGoodClassifyById(classifyId);
    }

    /**
     * 查询商品分类列表
     *
     * @param storeGoodClassify 商品分类
     * @return 商品分类
     */
    @Override
    public List<StoreGoodsClassify> selectStoreGoodClassifyList(StoreGoodsClassify storeGoodClassify) {
        return storeGoodClassifyMapper.selectStoreGoodClassifyList(storeGoodClassify);
    }

    /**
     * 新增商品分类
     *
     * @param storeGoodClassify 商品分类
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodClassify(StoreGoodsClassify storeGoodClassify) {
        storeGoodClassify.setCreateTime(DateUtils.getNowDate());
        return storeGoodClassifyMapper.insertStoreGoodClassify(storeGoodClassify);
    }

    /**
     * 修改商品分类
     *
     * @param storeGoodClassify 商品分类
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodClassify(StoreGoodsClassify storeGoodClassify) {
        return storeGoodClassifyMapper.updateStoreGoodClassify(storeGoodClassify);
    }

    /**
     * 删除商品分类对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodClassifyByIds(String ids) {
        return storeGoodClassifyMapper.deleteStoreGoodClassifyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品分类信息
     *
     * @param classifyId 商品分类ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodClassifyById(String classifyId) {
        return storeGoodClassifyMapper.deleteStoreGoodClassifyById(classifyId);
    }

    @Override
    public List<StoreGoodsClassifyTreeVO> queryClassifyTree() {
        StoreGoodsClassify query = new StoreGoodsClassify();
        query.setStatus(Constants.NORMAL);
        List<StoreGoodsClassify> list = storeGoodClassifyMapper.selectStoreGoodClassifyList(query);
        if (CollectionUtils.isNotEmpty(list)) {
            list.sort(Comparator.comparing(StoreGoodsClassify::getSort));
            List<StoreGoodsClassify> parentList =new ArrayList<>(list.size());
            List<StoreGoodsClassify> childList =new ArrayList<>(list.size());
            for (StoreGoodsClassify s:list) {
                if(StringUtils.isNotBlank(s.getAnotherName())) {
                    s.setName(s.getAnotherName());
                }
                boolean isRoot = list.stream().filter(e -> s.getParentId().equals(e.getClassifyId())).count() ==0;
                if (isRoot) {
                    parentList=list.stream().filter(p -> p.getParentId().equals(s.getClassifyId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(parentList)) {
                        Set<String> pIds=parentList.stream().map(StoreGoodsClassify::getClassifyId).collect(Collectors.toSet());
                        childList = list.stream().filter(c -> pIds.contains(c.getParentId())).collect(Collectors.toList());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(parentList)) {
                List<StoreGoodsClassifyTreeVO> tree = new ArrayList<>(parentList.size());
                for (StoreGoodsClassify cla : parentList) {
                    StoreGoodsClassifyTreeVO parent = StoreGoodsClassifyTreeVO.builder().value(cla.getClassifyId()).label(cla.getName()).build();
                    if (CollectionUtils.isNotEmpty(childList)) {
                        findChild(parent, childList);
                    }
                    tree.add(parent);
                }
                return tree;
            }
        }
        return new ArrayList<>();
    }



    private void findChild(StoreGoodsClassifyTreeVO parent, List<StoreGoodsClassify> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<StoreGoodsClassify> childList = list.stream().filter(e -> e.getParentId().equals(parent.getValue())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(childList)) {
                parent.setChildren(new ArrayList<StoreGoodsClassifyTreeVO>(childList.size()));
                for (StoreGoodsClassify cla : childList) {
                    StoreGoodsClassifyTreeVO child = StoreGoodsClassifyTreeVO.builder().value(cla.getClassifyId()).label(cla.getName()).parentId(parent.getParentId()).build();
                    findChild(child, list);
                    parent.getChildren().add(child);
                    list.remove(cla);
                }

            }
        }
    }
}
