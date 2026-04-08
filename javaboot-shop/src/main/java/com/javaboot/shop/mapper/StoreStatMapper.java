package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StoreStatDTO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;

import java.util.List;

public interface StoreStatMapper
{
    Integer selectOrderTotalNum(StoreStatDTO dto);

    Integer selectMerchantTotalNum(StoreStatDTO dto);

    Integer selectGoodsTotalNum(StoreStatDTO dto);

    Integer selectSpuTotalNum(StoreStatDTO dto);

    List<StoreGoodsOrderItem> selectTodayOrderList(StoreStatDTO dto);

    List<StoreGoodsOrderVO> selectNoPayList(StoreStatDTO dto);

    List<StoreGoodsOrderVO> selectOrderTotalList(StoreStatDTO dto);

    List<StoreMember> selectNoOrderMerberList(StoreStatDTO dto);

    List<StoreGoodsOrderItem> selectSpuOrderNumsList(StoreStatDTO dto);

}
