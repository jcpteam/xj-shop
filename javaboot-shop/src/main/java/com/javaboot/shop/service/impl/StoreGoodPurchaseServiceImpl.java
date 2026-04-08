package com.javaboot.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.StockCategory;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.StoreGoodPurchaseDTO;
import com.javaboot.shop.dto.StoreGoodPurchaseQueryDTO;
import com.javaboot.shop.dto.StoreWarehouseStockDTO;
import com.javaboot.shop.mapper.StoreGoodPurchaseMapper;
import com.javaboot.shop.mapper.StoreHouseMapper;
import com.javaboot.shop.mapper.StoreWarehouseStockMapper;
import com.javaboot.shop.service.IStoreGoodPurchaseItemService;
import com.javaboot.shop.service.IStoreGoodPurchaseService;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.service.IStoreWarehouseStockService;
import com.javaboot.shop.vo.StoreGoodPurchaseVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.service.ISysDeptService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品采购Service业务层处理
 *
 * @author lqh
 * @date 2021-05-23
 */
@Service
public class StoreGoodPurchaseServiceImpl implements IStoreGoodPurchaseService {
    @Autowired
    private StoreGoodPurchaseMapper storeGoodPurchaseMapper;

    @Autowired
    private IStoreWarehouseStockService storeWarehouseStockService;

    @Autowired
    private StoreWarehouseStockMapper storeWarehouseStockMapper;

    @Autowired
    private StoreHouseMapper storeHouseMapper;

    @Autowired
    private IStoreGoodPurchaseItemService itemService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IStoreHouseService storeHouseService;

    /**
     * 查询商品采购
     *
     * @param purchaseId 商品采购ID
     * @return 商品采购
     */
    @Override
    public StoreGoodPurchaseVO selectStoreGoodPurchaseById(Long purchaseId) {
        StoreGoodPurchaseVO vo = storeGoodPurchaseMapper.selectStoreGoodPurchaseById(purchaseId);
        Map<String, Object> map = new HashMap<>(2);
        map.put("purchase_id", purchaseId);
        map.put("status",Constants.NORMAL);
        vo.setItemList(itemService.listByMap(map));
        return vo;
    }

    /**
     * 查询商品采购列表
     *
     * @param storeGoodPurchase 商品采购
     * @return 商品采购
     */
    @Override
    public List<StoreGoodPurchaseVO> selectStoreGoodPurchaseList(StoreGoodPurchaseQueryDTO storeGoodPurchase) {
        List<StoreGoodPurchaseVO> list= storeGoodPurchaseMapper.selectStoreGoodPurchaseList(storeGoodPurchase);
        if(CollectionUtils.isNotEmpty(list)){
            List<StoreHouse> houseList = storeHouseService.selectStoreHouseListByIds(list.stream().map(StoreGoodPurchaseVO::getWarehouseId).collect(Collectors.toList()));
            Map<Long, StoreHouse> houseMap = houseList.stream().collect(Collectors.toMap(key->key.getStoreId(), obj->obj));


            SysDept query=new SysDept();
            query.setDeptIdList(list.stream().map(StoreGoodPurchase::getDeptId).collect(Collectors.toList()));
            List<SysDept> deptList=deptService.selectDeptList(query);
            if (CollectionUtils.isNotEmpty(deptList)) {
                list.forEach(v->{
                    SysDept dept= deptList.stream().filter(d->d.getDeptId().equals(v.getDeptId())).findFirst().orElse(null);
                    v.setDeptName(dept==null?"":dept.getDeptName());

                    if(houseMap.get(v.getWarehouseId()) != null) {

                        v.setWarehouseName(houseMap.get(v.getWarehouseId()).getStoreName());
                    }

                });
            }
        }
        return list;
    }

    /**
     * 新增商品采购
     *
     * @param dto 商品采购
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodPurchase(StoreGoodPurchaseDTO dto) {
        dto.setCreateTime(DateUtils.getNowDate());
        dto.setUpdateTime(DateUtils.getNowDate());
        dto.setStatus(Constants.NORMAL);
        dto.setPurchaseNo(CodeConstants.PURCHASE+DateUtils.getRandom());
        int result = storeGoodPurchaseMapper.insertStoreGoodPurchase(dto);
        dto.getItemList().forEach(i -> {
            i.setPurchaseId(dto.getPurchaseId());
            i.setStatus(Constants.NORMAL);
            i.setCreateTime(DateUtils.getNowDate());
            i.setUpdateTime(DateUtils.getNowDate());
        });
        itemService.saveBatch(dto.getItemList(), dto.getItemList().size());
        return result;
    }

    /**
     * 修改商品采购
     *
     * @param dto 商品采购
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodPurchase(StoreGoodPurchaseDTO dto) {
        int result = storeGoodPurchaseMapper.updateStoreGoodPurchase(dto);
        Map<String, Object> params = new HashMap<>(2);
        params.put("purchase_id", dto.getPurchaseId());
        params.put("status",Constants.NORMAL);
        List<StoreGoodPurchaseItem> oldItem=itemService.listByMap(params);
        oldItem.forEach(o->{
            o.setStatus(Constants.DELETE);
            o.setLastModifyTime(DateUtils.getNowDate());
        });
        itemService.updateBatchById(oldItem);
        dto.getItemList().forEach(i -> {
            i.setPurchaseId(dto.getPurchaseId());
            i.setCreateTime(DateUtils.getNowDate());
            i.setUpdateTime(DateUtils.getNowDate());
            i.setStatus(Constants.NORMAL);
        });
        itemService.saveBatch(dto.getItemList(), dto.getItemList().size());
        return result;
    }

    /**
     * 删除商品采购对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodPurchaseByIds(String ids) {

        String[] idArr =  Convert.toStrArray(ids);
        for (String id : idArr) {
            // 存在对应的入库单不能删除
            QueryWrapper<StoreWarehouseStock> tempQuery = new QueryWrapper<>();
            tempQuery.eq("object_id", id);
            tempQuery.eq("status", Constants.NORMAL);
            tempQuery.eq("category", StockCategory.PURCHASE_ORDER.getCode());
            List<StoreWarehouseStock> stockList = storeWarehouseStockMapper.selectList(tempQuery);
            if(CollectionUtils.isNotEmpty(stockList)) {
                throw new BusinessException("采购单下存在订单，请先删除对应的入库单！");
            }
         }

        QueryWrapper<StoreGoodPurchaseItem> wq = new QueryWrapper<>();
        wq.in("purchase_id", Convert.toStrArray(ids));
        wq.eq("status",Constants.NORMAL);
        List<StoreGoodPurchaseItem> oldItem=itemService.list(wq);
        oldItem.forEach(o->{
            o.setStatus(Constants.DELETE);
            o.setLastModifyTime(DateUtils.getNowDate());
        });
        itemService.updateBatchById(oldItem);
        return storeGoodPurchaseMapper.deleteStoreGoodPurchaseByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品采购信息
     *
     * @param purchaseId 商品采购ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodPurchaseById(Long purchaseId) {

        Map<String, Object> map = new HashMap<>(2);
        map.put("purchase_id", purchaseId);
        map.put("status",Constants.NORMAL);
        List<StoreGoodPurchaseItem> oldItem=itemService.listByMap(map);
        oldItem.forEach(o->{
            o.setStatus(Constants.DELETE);
            o.setLastModifyTime(DateUtils.getNowDate());
        });
        itemService.updateBatchById(oldItem);
        return storeGoodPurchaseMapper.deleteStoreGoodPurchaseById(purchaseId);
    }

    /**
     * 采购单生成入库单
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addStockByPurchase(StoreGoodPurchaseDTO req) {
        QueryWrapper<StoreGoodPurchase> wq = new QueryWrapper<>();
        wq.in("purchase_id", Convert.toStrArray(req.getIds()));
        List<StoreGoodPurchase> purchasesList = storeGoodPurchaseMapper.selectList(wq);
        purchasesList.forEach(storeGoodPurchase -> {
            StoreGoodPurchaseItem itemQry = new StoreGoodPurchaseItem();
            itemQry.setPurchaseId(storeGoodPurchase.getPurchaseId());
            itemQry.setStatus(Constants.NORMAL);
            List<StoreGoodPurchaseItem> itemList = itemService.selectStoreGoodPurchaseItemList(itemQry);

            StoreWarehouseStockDTO dto = new StoreWarehouseStockDTO();
            dto.setObjectId(storeGoodPurchase.getPurchaseId()+"");
            dto.setDeptId(storeGoodPurchase.getDeptId());
            dto.setWarehouseId(storeGoodPurchase.getWarehouseId());
            dto.setSupplierId(storeGoodPurchase.getSupplierId());
            dto.setPurchaseDate(storeGoodPurchase.getPurchaseDate());
            // 根据区域查询仓库
            StoreHouse storeHouse = new StoreHouse();
            storeHouse.setDeptId(dto.getDeptId());
            List<StoreHouse> list = storeHouseMapper.selectStoreHouseList(storeHouse);
            if(CollectionUtils.isEmpty(list)) {
                throw new BusinessException("所属区域下无仓库，不能生成入库单！");
            }
            dto.setWarehouseId(list.get(0).getStoreId());
            dto.setStockType(list.get(0).getStoreType());
            // 采购单入库
            dto.setCategory(StockCategory.PURCHASE_ORDER.getCode());
            dto.setCreateTime(DateUtils.parseDate(req.getStockDate()));
            dto.setComment(storeGoodPurchase.getComment());
            dto.setAdjustAmount(storeGoodPurchase.getAdjustAmount());
            // 判断入库单是否已经存在
            QueryWrapper query = new QueryWrapper<StoreWarehouseStock>();
            query.eq("object_id",dto.getObjectId()+"");
            query.eq("category",StockCategory.PURCHASE_ORDER.getCode());
            query.eq("status", Constants.NORMAL);
            List<StoreWarehouseStock> stockList = storeWarehouseStockMapper.selectList(query);

            List<StoreWarehouseStockItem> stockItemList = new ArrayList<>();
            itemList.forEach(item->{
                StoreWarehouseStockItem stockItem = new StoreWarehouseStockItem();
                stockItem.setQuantity(item.getQuantity());
                stockItem.setQuantityUnit(item.getQuantityUnit());
                stockItem.setStocksNumber(item.getStocksNumber());
                stockItem.setWeightUnit(item.getWeightUnit());
                stockItem.setRealQuantity(item.getQuantity());
                stockItem.setRealStocksNumber(item.getStocksNumber());
                stockItem.setSpuNo(item.getSpuNo());
                stockItem.setWarehouseId(dto.getWarehouseId());
                stockItem.setAmount(item.getPrice());
                stockItem.setTax(item.getTax());
                stockItem.setTaxRate(item.getTaxRate());
                stockItem.setNoTaxPrice(item.getNoTaxPrice());
                stockItem.setTotalPrice(item.getTotalPrice());
                stockItem.setAdjustAmount(item.getAdjustAmount());
                stockItem.setObjectJson(JSON.toJSONString(stockItem));
                stockItemList.add(stockItem);
            });
            dto.setItemList(stockItemList);

            if(CollectionUtils.isNotEmpty(stockList)) {
                StoreWarehouseStock storeWarehouseStock = stockList.get(0);
                StoreWarehouseStockDTO updateDTO = new StoreWarehouseStockDTO();
                BeanUtils.copyProperties(storeWarehouseStock, updateDTO);
                updateDTO.setCreateTime(DateUtils.parseDate(req.getStockDate()));
                updateDTO.setItemList(stockItemList);
                updateDTO.setComment(storeGoodPurchase.getComment());
                updateDTO.setAdjustAmount(storeGoodPurchase.getAdjustAmount());
                updateDTO.setSupplierId(storeGoodPurchase.getSupplierId());
                updateDTO.setPurchaseDate(storeGoodPurchase.getPurchaseDate());
                storeWarehouseStockService.updateStoreWarehouseStock(updateDTO, false);
            } else {
                storeWarehouseStockService.insertStoreWarehouseStock(dto);
            }

            storeGoodPurchase.setLastModifyTime(new Date());
            storeGoodPurchase.setStockStatus(Constants.NORMAL);
            storeGoodPurchaseMapper.updateById(storeGoodPurchase);

        });

        return 1;
    }
}
