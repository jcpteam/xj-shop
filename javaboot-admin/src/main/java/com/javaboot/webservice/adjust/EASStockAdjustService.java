package com.javaboot.webservice.adjust;

import com.javaboot.common.json.JSON;
import com.javaboot.shop.domain.StoreGoodsWarehouseAdjust;
import com.javaboot.shop.domain.StoreGoodsWarehouseAdjustItem;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.dto.StoreGoodsWarehouseAdjustDTO;
import com.javaboot.shop.mapper.StoreGoodsWarehouseAdjustItemMapper;
import com.javaboot.shop.mapper.StoreGoodsWarehouseAdjustMapper;
import com.javaboot.shop.mapper.StoreHouseMapper;
import com.javaboot.shop.vo.StoreGoodsWarehouseAdjustVO;
import com.javaboot.webservice.EASLoginConstant;
import com.javaboot.webservice.login.EASLoginProxy;
import com.javaboot.webservice.login.EASLoginProxyServiceLocator;
import com.javaboot.webservice.login.WSContext;
import org.apache.axis.client.Stub;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component(value = "easStockAdjustService")
public class EASStockAdjustService
{
    private final Logger logger = LoggerFactory.getLogger(EASStockAdjustService.class);

    @Autowired
    private StoreGoodsWarehouseAdjustMapper storeGoodsWarehouseAdjustMapper;

    @Autowired
    private StoreGoodsWarehouseAdjustItemMapper storeGoodsWarehouseAdjustItemMapper;

    @Autowired
    private StoreHouseMapper storeHouseMapper;

    public void syncAdjust()
    {
        StoreGoodsWarehouseAdjust storeGoodsWarehouseAdjust = new StoreGoodsWarehouseAdjust();
        storeGoodsWarehouseAdjust.setStatus("1");
        storeGoodsWarehouseAdjust.setSynStatus("0");
        List<StoreGoodsWarehouseAdjustVO> adjustVOList = storeGoodsWarehouseAdjustMapper.selectStoreGoodsWarehouseAdjustList(storeGoodsWarehouseAdjust);
        if(CollectionUtils.isEmpty(adjustVOList)) {
            return;
        }

        Set<Long> ids = new HashSet<>();
        Set<Long> storeIds = new HashSet<>();
        adjustVOList.forEach(o->{
            ids.add(o.getAdjustId());
            storeIds.add(o.getSourceWarehouseId());
            storeIds.add(o.getTargetWarehouseId());
        });
        StoreGoodsWarehouseAdjustItem itemDto = new StoreGoodsWarehouseAdjustItem();
        itemDto.setAdjustIds(new ArrayList<>(ids));
        List<StoreGoodsWarehouseAdjustItem> itemList = storeGoodsWarehouseAdjustItemMapper.selectStoreGoodsWarehouseAdjustItemList(itemDto);

        List<StoreHouse> storeHouseList = storeHouseMapper.selectStoreHouseListByIds(new ArrayList<>(storeIds));


        Map<Long, List<StoreGoodsWarehouseAdjustItem>> itemMap = itemList.stream().collect(Collectors.groupingBy(StoreGoodsWarehouseAdjustItem::getAdjustId));
        Map<Long, StoreHouse> storeMap = storeHouseList.stream().collect(Collectors.toMap(StoreHouse::getStoreId, o->o));


        List<EASAdjustReq> billList = new ArrayList<>();

        adjustVOList.forEach(o->{
            EASAdjustReq adjustReq = new EASAdjustReq();

            EASAdjustInfo adjustInfo = new EASAdjustInfo ();
            adjustInfo.setBizType("320");
            adjustInfo.setCreator("025281");
            adjustInfo.setInStorageOrg(storeMap.get(o.getTargetWarehouseId()).getDeptId());
            adjustInfo.setOutStorageOrg(storeMap.get(o.getSourceWarehouseId()).getDeptId());
            adjustInfo.setRemark(o.getRemark());
            adjustReq.setBill(adjustInfo);

            List<EASAdjustItem> adjustItems = new ArrayList<>();
            EASAdjustItem adjustItem = new EASAdjustItem();
            itemMap.get(o.getAdjustId()).forEach(detail->{
                adjustItem.setMaterial(detail.getSpuNo());
                adjustItem.setQty(new BigDecimal(detail.getUnitNumber()));
                adjustItem.setPrice(new BigDecimal(detail.getAmount()));
                adjustItem.setAmount(adjustItem.getQty().multiply(adjustItem.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                adjustItem.setInPlanDate(detail.getCreateTime());
                adjustItem.setOutPlanDate(detail.getCreateTime());
                // todo 获取仓库编号
                adjustItem.setInWarehouse(storeMap.get(o.getTargetWarehouseId()).getStoreNo());
                adjustItem.setOutWarehouse(storeMap.get(o.getSourceWarehouseId()).getStoreNo());
                adjustItems.add(adjustItem);
            });
            adjustReq.setEntry(adjustItems);

            billList.add(adjustReq);
        });

        try {
            //登录
            EASLoginProxy proxy = new EASLoginProxyServiceLocator().getEASLogin();
            WSContext context = proxy.login(EASLoginConstant.USER_NAME, EASLoginConstant.PASSWORD, EASLoginConstant.SLN_NAME, EASLoginConstant.DC_NAME, EASLoginConstant.LANGUAGE, EASLoginConstant.DB_TYPE);
            logger.error("====请求参数：{}====", JSON.marshal(billList));
            // 调用调库接口
            WSDSStockMoveWSFacadeSrvProxy wsDSStockMoveWSFacadeSrvProxy = new WSDSStockMoveWSFacadeSrvProxyServiceLocator().getWSDSStockMoveWSFacade();
            ((Stub) wsDSStockMoveWSFacadeSrvProxy).setHeader("http://login.webservice.bos.kingdee.com", "SessionId", context.getSessionId());
            String rsp = wsDSStockMoveWSFacadeSrvProxy.crtStockTransferBill(JSON.marshal(billList));
            //返回值
            logger.info("***调拨返回值:{}", rsp);
            adjustVOList.forEach(o->{
                StoreGoodsWarehouseAdjustDTO dto = new StoreGoodsWarehouseAdjustDTO();
                dto.setAdjustId(o.getAdjustId());
                dto.setSynStatus("1");
                dto.setLastModifyTime(new Date());
                storeGoodsWarehouseAdjustMapper.updateStoreGoodsWarehouseAdjust(dto);
            });


        } catch (Exception e) {
            logger.error("===crtStockTransferBill接口请求报错！！",e);
        }
    }
}
