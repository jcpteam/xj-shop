package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StoreStatDTO;

import java.util.List;

public interface StoreStatDetailMapper
{

    /**
     * 每个区域对应的在岗人数
     * @param dto
     * @return
     */
    List<StoreMember> selectSortMebersList(StoreStatDTO dto);
}
