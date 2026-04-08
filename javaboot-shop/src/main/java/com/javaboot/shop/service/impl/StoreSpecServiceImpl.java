package com.javaboot.shop.service.impl;

import com.javaboot.common.core.text.Convert;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreSpec;
import com.javaboot.shop.domain.StoreSpecItem;
import com.javaboot.shop.mapper.StoreSpecItemMapper;
import com.javaboot.shop.mapper.StoreSpecMapper;
import com.javaboot.shop.service.IStoreSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品规格(独立)Service业务层处理
 *
 * @author javaboot
 * @date 2019-08-25
 */
@Service
public class StoreSpecServiceImpl implements IStoreSpecService {
    @Autowired
    private StoreSpecMapper storeSpecMapper;

    @Autowired
    private StoreSpecItemMapper storeSpecItemMapper;

    /**
     * 查询商品规格(独立)
     *
     * @param id 商品规格(独立)ID
     * @return 商品规格(独立)
     */
    @Override
    public StoreSpec selectStoreSpecById(Integer id) {
        StoreSpec storeSpec = storeSpecMapper.selectStoreSpecById(id);
        StoreSpecItem storeSpecItem = new StoreSpecItem();
        storeSpecItem.setSpecId(id);
        List<StoreSpecItem> itemList = storeSpecItemMapper.selectStoreSpecItemList(storeSpecItem);
        List<String> strList = itemList.stream().map(StoreSpecItem::getItem).collect(Collectors.toList());
        String str = org.apache.commons.lang3.StringUtils.join(strList, "\n");
        storeSpec.setSpecItem(str);
        //System.out.println(storeSpec);
        return storeSpec;
    }

    /**
     * 查询商品规格(独立)列表
     *
     * @param storeSpec 商品规格(独立)
     * @return 商品规格(独立)
     */
    @Override
    public List<StoreSpec> selectStoreSpecList(StoreSpec storeSpec) {
        List<StoreSpec> list = storeSpecMapper.selectStoreSpecList(storeSpec);
        StoreSpecItem storeSpecItem = new StoreSpecItem();
        for (StoreSpec storeSpec1 : list) {
            storeSpecItem.setSpecId(storeSpec1.getId());
            List<StoreSpecItem> itemList = storeSpecItemMapper.selectStoreSpecItemList(storeSpecItem);
            List<String> strList = itemList.stream().map(StoreSpecItem::getItem).collect(Collectors.toList());
            String str = org.apache.commons.lang3.StringUtils.join(strList, ",");
            storeSpec1.setSpecItem(str);
        }
        return list;
    }

    /**
     * 新增商品规格(独立)
     *
     * @param storeSpec 商品规格(独立)
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreSpec(StoreSpec storeSpec) {
        int rows = 1;
        storeSpecMapper.insertStoreSpec(storeSpec);
        List<String> itemList = StringUtils.stringToList(storeSpec.getSpecItem());
        List<StoreSpecItem> specItems = new ArrayList<>();
        for (String item : itemList) {
            StoreSpecItem specItem = new StoreSpecItem();
            specItem.setItem(item);
            specItem.setSpecId(storeSpec.getId());

            specItems.add(specItem);
        }
        if (specItems.size() > 0) {
            rows = storeSpecItemMapper.batchSpecItem(specItems);
        }
        return rows;
    }

    /**
     * 修改商品规格(独立)
     *
     * @param storeSpec 商品规格(独立)
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreSpec(StoreSpec storeSpec) {
        int rows = 1;
        storeSpecMapper.updateStoreSpec(storeSpec);
        storeSpecItemMapper.deleteStoreSpecItemBySpecId(storeSpec.getId());
        List<String> itemList = StringUtils.stringToList(storeSpec.getSpecItem());
        List<StoreSpecItem> specItems = new ArrayList<>();

        for (String item : itemList) {
            StoreSpecItem specItem = new StoreSpecItem();
            specItem.setItem(item);
            specItem.setSpecId(storeSpec.getId());
            if (StringUtils.isEmpty(storeSpecItemMapper.selectStoreSpecItemList(specItem))) {
                specItems.add(specItem);
            }
        }
        if (specItems.size() > 0) {
            rows = storeSpecItemMapper.batchSpecItem(specItems);
        }
        //return rows;
        return rows;
    }

    /**
     * 删除商品规格(独立)对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreSpecByIds(String ids) {
        return storeSpecMapper.deleteStoreSpecByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品规格(独立)信息
     *
     * @param id 商品规格(独立)ID
     * @return 结果
     */
    @Override
    public int deleteStoreSpecById(Integer id) {
        return storeSpecMapper.deleteStoreSpecById(id);
    }
}
