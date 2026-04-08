package com.javaboot.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaboot.shop.domain.StoreMemberComment;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;

public interface IStoreMemberCommentService extends IService<StoreMemberComment>
{
    void addOrUpdateCommnet(StoreGoodsOrderDTO storeGoodsOrder);
}
