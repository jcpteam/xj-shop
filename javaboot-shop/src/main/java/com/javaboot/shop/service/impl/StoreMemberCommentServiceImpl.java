package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreMemberComment;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;
import com.javaboot.shop.mapper.StoreMemberCommentMapper;
import com.javaboot.shop.service.IStoreMemberCommentService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class StoreMemberCommentServiceImpl extends ServiceImpl<StoreMemberCommentMapper, StoreMemberComment>
    implements IStoreMemberCommentService
{

    public void addOrUpdateCommnet(StoreGoodsOrderDTO storeGoodsOrder)
    {
        if(CollectionUtils.isNotEmpty(storeGoodsOrder.getItemList())) {
            storeGoodsOrder.getItemList().forEach(item -> {

                String comment = item.getComment();
                Long goodsId = item.getGoodsId();
                String spuNo = item.getSpuNo();
                String memberId = storeGoodsOrder.getMerchantId();
                if(StringUtils.isNotEmpty(comment)) {
                    // 先删除老的备注
                    QueryWrapper<StoreMemberComment> delWrapper = new QueryWrapper<>();
                    delWrapper.eq("goods_id", goodsId);
                    delWrapper.eq("member_id", memberId);
                    delWrapper.eq("spu_no", spuNo);
                    this.baseMapper.delete(delWrapper);

                    // 添加新备注
                    StoreMemberComment storeMemberComment = new StoreMemberComment();
                    storeMemberComment.setComment(comment);
                    storeMemberComment.setMemberId(Long.parseLong(memberId));
                    storeMemberComment.setSpuNo(spuNo);
                    storeMemberComment.setGoodsId(goodsId);
                    this.baseMapper.insert(storeMemberComment);

                } else {
                    // 备注清空删除之前的备注
                    QueryWrapper<StoreMemberComment> delWrapper = new QueryWrapper<>();
                    delWrapper.eq("goods_id", goodsId);
                    delWrapper.eq("member_id", memberId);
                    delWrapper.eq("spu_no", spuNo);
                    this.baseMapper.delete(delWrapper);
                }
            });
        }
    }
}
