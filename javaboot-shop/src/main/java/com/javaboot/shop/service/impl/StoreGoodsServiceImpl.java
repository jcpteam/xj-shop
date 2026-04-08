package com.javaboot.shop.service.impl;

import com.javaboot.common.core.text.Convert;
import com.javaboot.shop.domain.StoreGoods;
import com.javaboot.shop.domain.StoreSpecGoodsPrice;
import com.javaboot.shop.mapper.StoreGoodsMapper;
import com.javaboot.shop.mapper.StoreSpecGoodsPriceMapper;
import com.javaboot.shop.service.IStoreGoodsService;
import com.javaboot.shop.service.IStoreSpecGoodsPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 商品主Service业务层处理
 *
 * @author javaboot
 * @date 2019-08-25
 */
@Service
public class StoreGoodsServiceImpl implements IStoreGoodsService {
    @Autowired
    private StoreGoodsMapper storeGoodsMapper;

    @Autowired
    private StoreSpecGoodsPriceMapper storeSpecGoodsPriceMapper;

    @Autowired
    private IStoreSpecGoodsPriceService storeSpecGoodsPriceService;

    @Override
    @Transactional
    public int saveSpecAttr(Map<String, Map<String, String>> specDataMap,
                            Map<String, String> attrDataMap, String goodsId, String goodsType) {
        //新增或者更新规格
        StoreSpecGoodsPrice storeSpecGoodsPrice = new StoreSpecGoodsPrice();
        StoreSpecGoodsPrice goodsPriceDomain = new StoreSpecGoodsPrice();
        storeSpecGoodsPrice.setGoodsId(Integer.valueOf(goodsId));
        goodsPriceDomain.setGoodsId(Integer.valueOf(goodsId));
        storeSpecGoodsPriceMapper.delStoreSpecGoodsPriceByGoodsId(Integer.valueOf(goodsId));

        for (Map.Entry<String, Map<String, String>> entry : specDataMap.entrySet()) {
            String mapKey = entry.getKey();
            Map map = entry.getValue();
            storeSpecGoodsPrice.setKey(mapKey);

            goodsPriceDomain.setKey(mapKey);
            goodsPriceDomain.setPrice(Double.valueOf(map.get("price").toString()));
            goodsPriceDomain.setMarketPrice(Double.valueOf(map.get("market_price").toString()));
            goodsPriceDomain.setStoreCount(Long.valueOf(map.get("store_count").toString()));
            goodsPriceDomain.setKeyName(map.get("key_name").toString());
            storeSpecGoodsPriceService.insertStoreSpecGoodsPrice(goodsPriceDomain);

        }

        //商品更新模型
        StoreGoods storeGoods = new StoreGoods();
        storeGoods.setGoodsId(Integer.valueOf(goodsId));
        storeGoods.setGoodsType(Integer.valueOf(goodsType));
        this.updateStoreGoods(storeGoods);

        return 1;
    }

    @Override
    public String getGoodsItems(Integer goodsId) {
        return storeSpecGoodsPriceMapper.selectStoreSpecGoodsPriceItemStr(goodsId);
    }

    /**
     * 查询商品主
     *
     * @param goodsId 商品主ID
     * @return 商品主
     */
    @Override
    public StoreGoods selectStoreGoodsById(Integer goodsId) {
        return storeGoodsMapper.selectStoreGoodsById(goodsId);
    }

    /**
     * 查询商品主列表
     *
     * @param storeGoods 商品
     * @return 商品主
     */
    @Override
    public List<StoreGoods> selectStoreGoodsList(StoreGoods storeGoods) {
        List<StoreGoods> storeGoodsList = storeGoodsMapper.selectStoreGoodsList(storeGoods);
        return storeGoodsList;
    }

    /**
     * 新增商品主
     *
     * @param storeGoods 商品主
     * @return 结果
     */
    @Override
    public int insertStoreGoods(StoreGoods storeGoods) {
        return storeGoodsMapper.insertStoreGoods(storeGoods);
    }

    /**
     * 修改商品主
     *
     * @param storeGoods 商品主
     * @return 结果
     */
    @Override
    public int updateStoreGoods(StoreGoods storeGoods) {
        return storeGoodsMapper.updateStoreGoods(storeGoods);
    }

    /**
     * 删除商品主对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsByIds(String ids) {
        return storeGoodsMapper.deleteStoreGoodsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品主信息
     *
     * @param goodsId 商品主ID
     * @return 结果
     */
    @Override
    public int deleteStoreGoodsById(Integer goodsId) {
        return storeGoodsMapper.deleteStoreGoodsById(goodsId);
    }
}
