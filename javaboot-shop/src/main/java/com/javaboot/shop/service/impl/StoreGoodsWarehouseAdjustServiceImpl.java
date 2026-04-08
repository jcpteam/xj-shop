package com.javaboot.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.StockCategory;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.StoreGoodsWarehouseAdjustDTO;
import com.javaboot.shop.dto.StoreWarehouseStockDTO;
import com.javaboot.shop.mapper.StoreGoodsWarehouseAdjustMapper;
import com.javaboot.shop.mapper.StoreWarehouseStockMapper;
import com.javaboot.shop.service.IStoreGoodsWarehouseAdjustItemService;
import com.javaboot.shop.service.IStoreGoodsWarehouseAdjustService;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.service.IStoreWarehouseStockService;
import com.javaboot.shop.vo.StoreGoodsWarehouseAdjustVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品调拨信息Service业务层处理
 * 
 * @author lqh
 * @date 2021-06-10
 */
@Service
public class StoreGoodsWarehouseAdjustServiceImpl implements IStoreGoodsWarehouseAdjustService {
    @Autowired
    private StoreGoodsWarehouseAdjustMapper storeGoodsWarehouseAdjustMapper;
    @Autowired
    private IStoreGoodsWarehouseAdjustItemService itemService;
    @Autowired
    private IStoreHouseService storeHouseService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IStoreWarehouseStockService storeWarehouseStockService;
    @Autowired
    private StoreWarehouseStockMapper storeWarehouseStockMapper;

    /**
     * 查询商品调拨信息
     * 
     * @param adjustId 商品调拨信息ID
     * @return 商品调拨信息
     */
    @Override
    public StoreGoodsWarehouseAdjustVO selectStoreGoodsWarehouseAdjustById(Long adjustId) {
        StoreGoodsWarehouseAdjustVO vo= storeGoodsWarehouseAdjustMapper.selectStoreGoodsWarehouseAdjustById(adjustId);
        StoreGoodsWarehouseAdjustItem query=new StoreGoodsWarehouseAdjustItem();
        query.setAdjustId(adjustId);
        vo.setAdjustTimeText(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,vo.getAdjustTime()));
        vo.setItemList(itemService.selectStoreGoodsWarehouseAdjustItemList(query));
        return vo;
    }

    /**
     * 查询商品调拨信息列表
     * 
     * @param storeGoodsWarehouseAdjust 商品调拨信息
     * @return 商品调拨信息
     */
    @Override
    public List<StoreGoodsWarehouseAdjustVO> selectStoreGoodsWarehouseAdjustList(StoreGoodsWarehouseAdjust storeGoodsWarehouseAdjust) {

        if(StringUtils.isEmpty(storeGoodsWarehouseAdjust.getDeptId())){
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            storeGoodsWarehouseAdjust.setDeptId(user.isAdmin()?null:user.getDeptId());
        }
        List<StoreGoodsWarehouseAdjustVO> list=storeGoodsWarehouseAdjustMapper.selectStoreGoodsWarehouseAdjustList(storeGoodsWarehouseAdjust);
        if(CollectionUtils.isNotEmpty(list)){
            List<StoreHouse> houseList= storeHouseService.selectStoreHouseListByIds(list.stream().map(StoreGoodsWarehouseAdjustVO::getSourceWarehouseId).collect(Collectors.toList()));
            houseList.addAll(storeHouseService.selectStoreHouseListByIds(list.stream().map(StoreGoodsWarehouseAdjustVO::getTargetWarehouseId).collect(Collectors.toList())));
            SysDept deptQuery=new SysDept();
            deptQuery.setDeptIdList(list.stream().map(StoreGoodsWarehouseAdjustVO::getDeptId).collect(Collectors.toList()));
            List<SysDept> deptList=deptService.selectDeptList(deptQuery);
            List<String> userIds=new ArrayList<>(list.size());
            list.forEach(l->userIds.add(l.getCreatorId().toString()));
            List<SysUser> userList = sysUserService.selectUserByIds(userIds);
            if(CollectionUtils.isNotEmpty(houseList)){
                list.forEach(s->{
                    StoreHouse sourceWarehouse= CollectionUtils.isEmpty(houseList)?null: houseList.stream().filter(h->h.getStoreId().equals(s.getSourceWarehouseId())).findFirst().orElse(null);
                    StoreHouse targetWarehouse= CollectionUtils.isEmpty(houseList)?null: houseList.stream().filter(h->h.getStoreId().equals(s.getTargetWarehouseId())).findFirst().orElse(null);
                    SysUser sysUser= CollectionUtils.isEmpty(userList)?null: userList.stream().filter(h->h.getUserId().equals(s.getCreatorId())).findFirst().orElse(null);
                    SysDept dept= CollectionUtils.isEmpty(deptList)?null: deptList.stream().filter(d->d.getDeptId().equals(s.getDeptId())).findFirst().orElse(null);
                    if(sysUser!=null){
                        s.setCreatorName(sysUser.getUserName());
                    }
                    if(sourceWarehouse!=null){
                        s.setSourceWarehouseName(sourceWarehouse.getStoreName());
                    }
                    if(targetWarehouse!=null){
                        s.setTargetWarehouseName(targetWarehouse.getStoreName());
                    }
                    if(dept!=null){
                        s.setDeptName(dept.getDeptName());
                    }
                    s.setAdjustTimeText(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,s.getAdjustTime()));
                });
            }
        }
        return list;
    }

    /**
     * 新增商品调拨信息
     * 
     * @param storeGoodsWarehouseAdjust 商品调拨信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodsWarehouseAdjust(StoreGoodsWarehouseAdjustDTO storeGoodsWarehouseAdjust) {
        storeGoodsWarehouseAdjust.setCreateTime(DateUtils.getNowDate());
        storeGoodsWarehouseAdjust.setStatus(Constants.NORMAL);
        storeGoodsWarehouseAdjust.setAdjustNo(CodeConstants.ADJUST+DateUtils.getRandom());
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        storeGoodsWarehouseAdjust.setCreatorId(user.getUserId());
        int result= storeGoodsWarehouseAdjustMapper.insertStoreGoodsWarehouseAdjust(storeGoodsWarehouseAdjust);
        storeGoodsWarehouseAdjust.getItemList().forEach(i->{
            i.setAdjustId(storeGoodsWarehouseAdjust.getAdjustId());
            i.setStatus(Constants.NORMAL);
            itemService.insertStoreGoodsWarehouseAdjustItem(i);
        });
        return result;
    }

    /**
     * 修改商品调拨信息
     * 
     * @param storeGoodsWarehouseAdjust 商品调拨信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodsWarehouseAdjust(StoreGoodsWarehouseAdjustDTO storeGoodsWarehouseAdjust) {
        int result= storeGoodsWarehouseAdjustMapper.updateStoreGoodsWarehouseAdjust(storeGoodsWarehouseAdjust);
        itemService.deleteStoreGoodsWarehouseAdjustItemByAdjustId(storeGoodsWarehouseAdjust.getAdjustId());
        storeGoodsWarehouseAdjust.getItemList().forEach(i->{
            i.setAdjustId(storeGoodsWarehouseAdjust.getAdjustId());
            i.setUpdateTime(DateUtils.getNowDate());
            i.setStatus(Constants.NORMAL);
            itemService.insertStoreGoodsWarehouseAdjustItem(i);
        });
        return result;
    }

    /**
     * 删除商品调拨信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsWarehouseAdjustByIds(String ids) {

        String[] idArr =  Convert.toStrArray(ids);
        for (String id : idArr) {
            // 存在对应的入库单不能删除
            QueryWrapper<StoreWarehouseStock> tempQuery = new QueryWrapper<>();
            tempQuery.eq("object_id", id);
            tempQuery.eq("status", Constants.NORMAL);
            tempQuery.eq("category", StockCategory.ADJUST_IN_ORDER.getCode());
            List<StoreWarehouseStock> stockList = storeWarehouseStockMapper.selectList(tempQuery);
            if(CollectionUtils.isNotEmpty(stockList)) {
                throw new BusinessException("调拨单下存在订单，请先删除对应的调拨单！");
            }
        }

        return storeGoodsWarehouseAdjustMapper.deleteStoreGoodsWarehouseAdjustByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品调拨信息信息
     * 
     * @param adjustId 商品调拨信息ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsWarehouseAdjustById(Long adjustId) {
        itemService.deleteStoreGoodsWarehouseAdjustItemByAdjustId(adjustId);
        return storeGoodsWarehouseAdjustMapper.deleteStoreGoodsWarehouseAdjustById(adjustId);
    }

    /**
     * 调拨单生成入库单
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addStockByAdjust(StoreGoodsWarehouseAdjustDTO req) {
        QueryWrapper<StoreGoodsWarehouseAdjust> wq = new QueryWrapper<>();
        wq.in("adjust_id", Convert.toStrArray(req.getIds()));
        List<StoreGoodsWarehouseAdjust> adjustList = storeGoodsWarehouseAdjustMapper.selectList(wq);

        List<Long> warehouseIdList = new ArrayList<>();
        warehouseIdList.addAll(adjustList.stream().map(StoreGoodsWarehouseAdjust::getSourceWarehouseId).collect(Collectors.toList()));
        warehouseIdList.addAll(adjustList.stream().map(StoreGoodsWarehouseAdjust::getTargetWarehouseId).collect(Collectors.toList()));
        List<StoreHouse> houseList = storeHouseService.selectStoreHouseListByIds(warehouseIdList);
        Map<Long, StoreHouse> houseMap = houseList.stream().collect(Collectors.toMap(key->key.getStoreId(), obj->obj));

        adjustList.forEach(adjust -> {
            StoreGoodsWarehouseAdjustItem itemQry = new StoreGoodsWarehouseAdjustItem();
            itemQry.setAdjustId(adjust.getAdjustId());
            List<StoreGoodsWarehouseAdjustItem> itemList = itemService.selectStoreGoodsWarehouseAdjustItemList(itemQry);


            StoreWarehouseStockDTO dtoOut = new StoreWarehouseStockDTO();
            dtoOut.setObjectId(adjust.getAdjustId()+"");
            StoreHouse storeHouseOut = houseMap.get(adjust.getSourceWarehouseId());
            if(storeHouseOut != null) {
                dtoOut.setDeptId(storeHouseOut.getDeptId());
            } else {
                dtoOut.setDeptId(adjust.getDeptId());
            }
            dtoOut.setSupplierId(dtoOut.getDeptId());
            dtoOut.setWarehouseId(adjust.getSourceWarehouseId());
            dtoOut.setPurchaseDate(adjust.getAdjustTime());
            dtoOut.setStockType("1");
            // 调拨单出库
            dtoOut.setCategory(StockCategory.ADJUST_OUT_ORDER.getCode());
            dtoOut.setCreateTime(DateUtils.parseDate(req.getStockDate()));
            dtoOut.setComment(adjust.getComment());

            StoreWarehouseStockDTO dto = new StoreWarehouseStockDTO();
            dto.setObjectId(adjust.getAdjustId()+"");
            StoreHouse storeHouse = houseMap.get(adjust.getTargetWarehouseId());
            if(storeHouse != null) {
                dto.setDeptId(storeHouse.getDeptId());
            } else {
                dto.setDeptId(adjust.getDeptId());
            }
            dto.setSupplierId(dtoOut.getDeptId());
            dto.setWarehouseId(adjust.getTargetWarehouseId());
            dto.setStockType("1");
            // 调拨单入库
            dto.setCategory(StockCategory.ADJUST_IN_ORDER.getCode());
            dto.setCreateTime(DateUtils.parseDate(req.getStockDate()));
            dto.setPurchaseDate(adjust.getAdjustTime());
            dto.setComment(adjust.getComment());

            // 判断入库单是否已经存在
            QueryWrapper query = new QueryWrapper<StoreWarehouseStock>();
            query.eq("object_id",dto.getObjectId()+"");
            query.eq("category",StockCategory.ADJUST_IN_ORDER.getCode());
            query.eq("status", Constants.NORMAL);
            List<StoreWarehouseStock> stockList = storeWarehouseStockMapper.selectList(query);

            List<StoreWarehouseStockItem> stockItemList = new ArrayList<>();
            itemList.forEach(item->{
                StoreWarehouseStockItem stockItem = new StoreWarehouseStockItem();
                stockItem.setQuantity(item.getUnitNumber());
                stockItem.setQuantityUnit(item.getQuantityUnit());
                stockItem.setStocksNumber(item.getAdjustNumber());
                stockItem.setWeightUnit(item.getWeightUnit());
                stockItem.setRealQuantity(item.getUnitNumber());
                stockItem.setRealStocksNumber(item.getAdjustNumber());
                stockItem.setSpuNo(item.getSpuNo());
                stockItem.setWarehouseId(dto.getWarehouseId());
                stockItem.setAmount(item.getAmount());
                stockItem.setTotalPrice(new BigDecimal(item.getAmount() * item.getUnitNumber()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                stockItem.setObjectJson(JSON.toJSONString(stockItem));
                stockItemList.add(stockItem);
            });
            dto.setItemList(stockItemList);

            if(CollectionUtils.isNotEmpty(stockList)) {
                StoreWarehouseStock storeWarehouseStock = stockList.get(0);
                StoreWarehouseStockDTO updateDTO = new StoreWarehouseStockDTO();
                BeanUtils.copyProperties(storeWarehouseStock, updateDTO);
                updateDTO.setComment(adjust.getComment());
                updateDTO.setCreateTime(DateUtils.parseDate(req.getStockDate()));
                updateDTO.setItemList(stockItemList);
                updateDTO.setDeptId(dto.getDeptId());
                updateDTO.setWarehouseId(dto.getWarehouseId());
                updateDTO.setPurchaseDate(adjust.getAdjustTime());
                storeWarehouseStockService.updateStoreWarehouseStock(updateDTO, false);
            } else {
                storeWarehouseStockService.insertStoreWarehouseStock(dto);
            }



            // 判断入库单是否已经存在
            QueryWrapper queryOut = new QueryWrapper<StoreWarehouseStock>();
            queryOut.eq("object_id",dto.getObjectId()+"");
            queryOut.eq("category",StockCategory.ADJUST_OUT_ORDER.getCode());
            queryOut.eq("status",Constants.NORMAL);
            List<StoreWarehouseStock> stockOutList = storeWarehouseStockMapper.selectList(queryOut);

            List<StoreWarehouseStockItem> stockItemOutList = new ArrayList<>();
            itemList.forEach(item->{
                StoreWarehouseStockItem stockItem = new StoreWarehouseStockItem();
                stockItem.setQuantity(-item.getUnitNumber());
                stockItem.setQuantityUnit(item.getQuantityUnit());
                stockItem.setStocksNumber(-item.getAdjustNumber());
                stockItem.setWeightUnit(item.getWeightUnit());
                stockItem.setRealQuantity(-item.getUnitNumber());
                stockItem.setRealStocksNumber(-item.getAdjustNumber());
                stockItem.setSpuNo(item.getSpuNo());
                stockItem.setWarehouseId(dtoOut.getWarehouseId());
                stockItem.setAmount(item.getAmount());
                stockItem.setObjectJson(JSON.toJSONString(stockItem));
                stockItem.setTotalPrice(new BigDecimal(item.getAmount() * item.getUnitNumber()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                stockItemOutList.add(stockItem);
            });
            dtoOut.setItemList(stockItemOutList);

            if(CollectionUtils.isNotEmpty(stockOutList)) {
                StoreWarehouseStock storeWarehouseStock = stockOutList.get(0);
                StoreWarehouseStockDTO updateDTO = new StoreWarehouseStockDTO();
                BeanUtils.copyProperties(storeWarehouseStock, updateDTO);
                updateDTO.setCreateTime(DateUtils.parseDate(req.getStockDate()));
                updateDTO.setItemList(stockItemOutList);
                updateDTO.setComment(adjust.getComment());
                updateDTO.setDeptId(dtoOut.getDeptId());
                updateDTO.setWarehouseId(dtoOut.getWarehouseId());
                updateDTO.setPurchaseDate(adjust.getAdjustTime());
                storeWarehouseStockService.updateStoreWarehouseStock(updateDTO, false);
            } else {
                storeWarehouseStockService.insertStoreWarehouseStock(dtoOut);
            }

            adjust.setLastModifyTime(new Date());
            adjust.setStockStatus(Constants.NORMAL);
            storeGoodsWarehouseAdjustMapper.updateById(adjust);
        });

        return 1;
    }
}
