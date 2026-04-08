package com.javaboot.shop.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsQueryDTO;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsSaleNumDTO;
import com.javaboot.shop.mapper.*;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品列表Service业务层处理
 *
 * @author lqh
 * @date 2021-05-23
 */
@Service
public class StoreGoodsQuotationGoodsServiceImpl extends ServiceImpl<StoreGoodsQuotationGoodsMapper, StoreGoodsQuotationGoods> implements IStoreGoodsQuotationGoodsService {
    @Autowired
    private StoreGoodsQuotationGoodsMapper storeGoodsQuotationGoodsMapper;
    @Autowired
    private StoreGoodsUnitPriceMapper storeGoodsUnitPriceMapper;
    @Autowired
    private StoreGoodsOrderMapper storeGoodsOrderMapper;
    @Autowired
    private StorePriceSettingDetailMapper storePriceSettingDetailMapper;
    @Autowired
    private IStoreGoodsSalesUnitService iStoreGoodsSalesUnitService;
    @Autowired
    private IStoreGoodsClassifyService classifyService;
    @Autowired
    private IStoreGoodsSpuService spuService;
    @Autowired
    private IStoreGoodsSpuUnitService storeGoodsSpuUnitService;
    @Autowired
    private IStoreGoodsUnitPriceService storeGoodsUnitPriceService;
    @Autowired
    private IStoreGoodsSpuUnitConversionService storeGoodsSpuUnitConversionService;
    @Autowired
    private IStoreSaleSettingService storeSaleSettingService;
    @Autowired
    private StoreMemberCommentMapper storeMemberCommentMapper;

    /**
     * 查询商品列表
     *
     * @param goodsId 商品列表ID
     * @return 商品列表
     */
    @Override
    public StoreGoodsQuotationGoodsVO selectStoreGoodsQuotationGoodsById(Long goodsId) {
        StoreGoodsQuotationGoodsVO goodsQuotationGoods = storeGoodsQuotationGoodsMapper.selectStoreGoodsQuotationGoodsById(goodsId);
        if (goodsQuotationGoods != null) {
            StoreGoodsSpu spu = spuService.selectStoreGoodsSpuById(goodsQuotationGoods.getSpuNo());
            if (spu != null) {
                List<StoreGoodsClassify> classifyList = classifyService.selectStoreGoodClassifyList(null);
                StoreGoodsClassify classify = classifyList.stream().filter(e -> e.getClassifyId().equals(spu.getClassifyId())).findFirst().orElse(null);
                if (classify != null) {
                    StoreGoodsClassify parentClassify = classifyList.stream().filter(e -> e.getClassifyId().equals(classify.getParentId()) && !"05".equals(e.getParentId())).findFirst().orElse(null);
                    if (parentClassify != null) {
                        goodsQuotationGoods.setClassifyId(parentClassify.getClassifyId() + "," + spu.getClassifyId());
                    } else {
                        goodsQuotationGoods.setClassifyId(spu.getClassifyId());
                    }
                }
            }
            QueryWrapper<StoreGoodsUnitPrice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("goods_id", goodsId);
            goodsQuotationGoods.setUnitPriceList(storeGoodsUnitPriceMapper.selectList(queryWrapper));
        }

        return goodsQuotationGoods;
    }

    /**
     * 查询商品列表列表
     *
     * @param storeGoodsQuotationGoods 商品列表
     * @return 商品列表
     */
    @Override
    public List<StoreGoodsQuotationGoodsVO> selectStoreGoodsQuotationGoodsList(StoreGoodsQuotationGoodsQueryDTO storeGoodsQuotationGoods) {
        List<StoreGoodsQuotationGoodsVO> list = storeGoodsQuotationGoodsMapper.selectStoreGoodsQuotationGoodsList(storeGoodsQuotationGoods);
        if (CollectionUtils.isNotEmpty(list)) {

            // 获取报价单商品对应id和单位id
            List<String> unitIdIds = new ArrayList<>(list.size() * 2);
            Set<String> setUnitIds = new HashSet<>(list.size() * 2);
            List<Long> goodsIds = new ArrayList<>(list.size() * 2);
            List<String> spuNos = new ArrayList<>(list.size() * 2);
            for (StoreGoodsQuotationGoodsVO vo : list) {
                setUnitIds.addAll(Arrays.asList(vo.getUnitIds().split(",")));
                goodsIds.add(vo.getGoodsId());
                if(!spuNos.contains(vo.getSpuNo())){
                    spuNos.add(vo.getSpuNo());
                }
            }
            unitIdIds.addAll(setUnitIds);

            //查询单位名称
            StoreGoodsSalesUnit query = new StoreGoodsSalesUnit();
            query.setUnitIds(unitIdIds);
            List<StoreGoodsSalesUnit> storeGoodsSalesUnits = iStoreGoodsSalesUnitService.selectStoreGoodsSalesUnitList(query);
            Map<Long, String> unitMap = storeGoodsSalesUnits.stream().collect(Collectors.toMap(StoreGoodsSalesUnit::getUnitId, StoreGoodsSalesUnit::getName, (o, n) -> o, LinkedHashMap::new));

            //查询单位对应的价格
            QueryWrapper<StoreGoodsUnitPrice> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("goods_id", goodsIds);
            List<StoreGoodsUnitPrice> unitPriceList =  storeGoodsUnitPriceMapper.selectList(queryWrapper);
            Map<Long, List<StoreGoodsUnitPrice>> unitPriceMap = new HashMap<>();
            unitPriceList.forEach(e->{
                e.setUnitName(unitMap.get(e.getUnitId()));
                unitPriceMap.computeIfAbsent(e.getGoodsId(), o->new ArrayList<>());
                unitPriceMap.get(e.getGoodsId()).add(e);
            });

            //查询分类名称
            List<StoreGoodsClassify> classifyList = classifyService.selectStoreGoodClassifyList(null);
            StoreGoodsSpuUnit querySpuUnit  = new StoreGoodsSpuUnit();
            querySpuUnit.setSpuNoList(spuNos);
            List<StoreGoodsSpuUnit> spuUnits = storeGoodsSpuUnitService.selectStoreGoodsSpuUnitList(querySpuUnit);
            List<StoreGoodsSalesUnit> salesUnits = iStoreGoodsSalesUnitService.getNormalSpecificationsList();
            //设置基本单位
            String baseUnit = null;
            if (StringUtils.isNotEmpty(storeGoodsSalesUnits)) {

                StoreGoodsSalesUnit base = storeGoodsSalesUnits.stream().filter(e -> e.getUnitId().equals(1L)).findFirst().orElse(null);
                baseUnit = base == null ? "公斤" : base.getSettlementUnit();
            }
            //查询审核通过的商品控价
            StorePriceSettingDetail querySetting=new StorePriceSettingDetail();
            querySetting.setStatus("2");
            querySetting.setGoodsIdList(goodsIds);
            List<StorePriceSettingDetail> settingDetailList=storePriceSettingDetailMapper.selectSettingDetailList(querySetting);
            //查询商品单位换算关系
            StoreGoodsSpuUnitConversion queryCov=new StoreGoodsSpuUnitConversion();
            queryCov.setSpuNoList(spuNos);
            if(StringUtils.isNotEmpty(storeGoodsQuotationGoods.getDeptId())){
                queryCov.setDeptId(storeGoodsQuotationGoods.getDeptId());
            }else if(StringUtils.isNotEmpty(storeGoodsQuotationGoods.getLoginUserDeptId())){
                queryCov.setDeptId(storeGoodsQuotationGoods.getLoginUserDeptId());
            }
            List<StoreGoodsSpuUnitConversion> listCov= storeGoodsSpuUnitConversionService.selectStoreGoodsSpuUnitConversionList(queryCov);

            //查询spu当天的控价数量
            StoreSaleSetting querySale = new StoreSaleSetting();
            querySale.setSpuNoList(spuNos);
            if(StringUtils.isNotEmpty(storeGoodsQuotationGoods.getDeptId())){
                querySale.setDeptId(storeGoodsQuotationGoods.getDeptId());
            }else if(StringUtils.isNotEmpty(storeGoodsQuotationGoods.getLoginUserDeptId())){
                querySale.setDeptId(storeGoodsQuotationGoods.getLoginUserDeptId());
            }
            String day = DateUtils.getDate();
            querySale.setStartTime(day + " 00:00:00");
            querySale.setEndTime(day + " 23:59:59");
            List<StoreSaleSetting> saleSettingList = storeSaleSettingService.selectStoreSaleSettingListForApp(querySale);

            for (StoreGoodsQuotationGoodsVO vo : list) {
                vo.setSubCaseMain(1.0);
                vo.setStoreGoodsSalesUnitList(new ArrayList<>());
                if (CollectionUtils.isNotEmpty(spuUnits)&&CollectionUtils.isNotEmpty(salesUnits)) {
                    StoreGoodsSpuUnitConversion  goodsCov=null;
                    if(CollectionUtils.isNotEmpty(listCov)){
                        goodsCov =listCov.stream().filter(c->c.getSpuNo().equals(vo.getSpuNo())).findFirst().orElse(null);
                    }
                    for (StoreGoodsSpuUnit s : spuUnits) {
                        if(s.getSpuNo().equals(vo.getSpuNo())) {
                            StoreGoodsSalesUnit main = salesUnits.stream().filter(u -> u.getUnitId().equals(s.getMainUnitId())).findFirst().orElse(null);
                            StoreGoodsSalesUnit sub = salesUnits.stream().filter(u -> u.getUnitId().equals(s.getSubUnitId())).findFirst().orElse(null);
                            vo.setBaseUnit("主:"+main.getName()+",副:"+sub.getName());
                            vo.setMainUnitId(main.getUnitId());
                            vo.setSubUnitId(sub.getUnitId());
                            vo.setMainUnitName(main.getName());
                            vo.setSubUnitName(sub.getName());
                            if(goodsCov!=null){
                                vo.setSubCaseMain(goodsCov.getSubCaseMain());
                            }
                        }
                    }

                }else {
                    vo.setBaseUnit(baseUnit);
                }

                // 默认取一个单位，并计算副单位的转换关系
//                vo.setSaleUnitName(unitMap.get(Long.parseLong(vo.getUnitIds().split(",")[0])));
//                if(vo.getUnitIds().equals(vo.getSubUnitId()+"") && vo.getSaleNum() != null){
//                    vo.setSaleNum(new BigDecimal(vo.getSubCaseMain() * vo.getSaleNum()).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue());
//                }

                //设置规格名称
                if (CollectionUtils.isNotEmpty(storeGoodsSalesUnits)) {
                    String[] arrSpecifications = vo.getUnitIds().split(",");
                    for (int i = 0; i < arrSpecifications.length; i++) {
                        for (StoreGoodsSalesUnit sto : storeGoodsSalesUnits) {
                            if (sto.getUnitId().toString().equals(arrSpecifications[i])) {
                                vo.getStoreGoodsSalesUnitList().add(sto);
                            }
                        }
                    }
                }
                //设置分类名称
                if (CollectionUtils.isNotEmpty(classifyList)) {
                    StoreGoodsClassify classify = classifyList.stream().filter(e -> e.getClassifyId().equals(vo.getClassifyId())).findFirst().orElse(null);
                    if (classify != null) {
                        StoreGoodsClassify parentClassify = classifyList.stream().filter(e -> e.getClassifyId().equals(classify.getParentId())).findFirst().orElse(null);
                        if (parentClassify != null) {
                            vo.setClassifyName((StringUtils.isNotEmpty(parentClassify.getAnotherName()) ? parentClassify.getAnotherName() : parentClassify.getName()).concat("/").concat(StringUtils.isNotEmpty(classify.getAnotherName()) ? classify.getAnotherName() : classify.getName()) );
                        } else {
                            vo.setClassifyName(classify.getName());
                        }
                    }

                }

                // 设置单位价格
                vo.setUnitPriceList(unitPriceMap.get(vo.getGoodsId()));
                StorePriceSettingDetail settingDetail= CollectionUtils.isEmpty(settingDetailList)?null:settingDetailList.stream().filter(s->s.getGoodsId().equals(vo.getGoodsId().toString())).findFirst().orElse(null);
                if(settingDetail!=null){
                    vo.setSettingPrice(settingDetail.getPrice());
                }

                //设置spu总控数
                StoreSaleSetting  saleSetting=null;
                if(CollectionUtils.isNotEmpty(saleSettingList)){
                    saleSetting =saleSettingList.stream().filter(c->c.getSpuNo().equals(vo.getSpuNo())).findFirst().orElse(null);
                }
                if(saleSetting != null){
                    if(saleSetting == null || saleSetting.getSettingQuanintiy() <0){
                        vo.setSettingQuanintiy(0d);
                    }else{
                        vo.setSettingQuanintiy(saleSetting.getSettingQuanintiy());
                    }
                }else{
                    vo.setSettingQuanintiy(0d);
                }

                // 获取商户对应商品备注信息
                QueryWrapper<StoreMemberComment> commentQry = new QueryWrapper<>();
                commentQry.eq("spu_no", vo.getSpuNo());
                commentQry.eq("goods_id", vo.getGoodsId());
                commentQry.eq("member_id", storeGoodsQuotationGoods.getMerchantId());
                List<StoreMemberComment> commentList =  storeMemberCommentMapper.selectList(commentQry);
                if(CollectionUtils.isNotEmpty(commentList)) {
                    vo.setMemberComment(commentList.get(0).getComment());
                }
            }
        }
        return list;
    }

    /**
     * 新增商品列表
     *
     * @param storeGoodsQuotationGoods 商品列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodsQuotationGoods(StoreGoodsQuotationGoods storeGoodsQuotationGoods) {
        //报价单商品名称重复校验
        checkGoodsNameRepeat(storeGoodsQuotationGoods);
        storeGoodsQuotationGoods.setCreateTime(DateUtils.getNowDate());
        int row = storeGoodsQuotationGoodsMapper.insertStoreGoodsQuotationGoods(storeGoodsQuotationGoods);

        // 新增单位对应的价格
        List<StoreGoodsUnitPrice> unitList = JSONArray.parseArray(storeGoodsQuotationGoods.getUnitJson(), StoreGoodsUnitPrice.class);
        if(CollectionUtils.isNotEmpty(unitList))
        {
            unitList.forEach(o->o.setGoodsId(storeGoodsQuotationGoods.getGoodsId()));
            storeGoodsUnitPriceService.saveBatch(unitList);
        }

        return row;
    }

    /**
     * 修改商品列表
     *
     * @param storeGoodsQuotationGoods 商品列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodsQuotationGoods(StoreGoodsQuotationGoods storeGoodsQuotationGoods) {
        //报价单商品名称重复校验
        checkGoodsNameRepeat(storeGoodsQuotationGoods);
        // 新增单位对应的价格
        List<StoreGoodsUnitPrice> unitList = JSONArray.parseArray(storeGoodsQuotationGoods.getUnitJson(), StoreGoodsUnitPrice.class);
        if(CollectionUtils.isNotEmpty(unitList))
        {
            QueryWrapper<StoreGoodsUnitPrice> delWrapper = new QueryWrapper<>();
            delWrapper.eq("goods_id", storeGoodsQuotationGoods.getGoodsId());
            storeGoodsUnitPriceMapper.delete(delWrapper);

            unitList.forEach(o->o.setLastModifyTime(new Date()));
            storeGoodsUnitPriceService.saveBatch(unitList);
        }

        return storeGoodsQuotationGoodsMapper.updateStoreGoodsQuotationGoods(storeGoodsQuotationGoods);
    }

    /**
     * 删除商品列表对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsQuotationGoodsByIds(String ids) {
        int count = storeGoodsOrderMapper.getOrderGoodsCount(Arrays.asList(Convert.toStrArray(ids)));
        if(count > 0) {
            throw new BusinessException("报价单商品关联了订单，不能被删除！");
        }
        return storeGoodsQuotationGoodsMapper.deleteStoreGoodsQuotationGoodsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品列表信息
     *
     * @param goodsId 商品列表ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsQuotationGoodsById(Long goodsId) {
        return storeGoodsQuotationGoodsMapper.deleteStoreGoodsQuotationGoodsById(goodsId);
    }

    /**
     * 更新库存
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSaleNum(StoreGoodsQuotationGoodsSaleNumDTO dto) {
        return storeGoodsQuotationGoodsMapper.updateSaleNum(dto);
    }

    /**
     * 报价单商品名称重复校验
     * @param storeGoodsQuotationGoods
     */
    private void checkGoodsNameRepeat(StoreGoodsQuotationGoods storeGoodsQuotationGoods){
        StoreGoodsQuotationGoodsQueryDTO dto = new StoreGoodsQuotationGoodsQueryDTO();
        dto.setPropertyName(storeGoodsQuotationGoods.getPropertyName());
        dto.setQuotationId(storeGoodsQuotationGoods.getQuotationId());
        dto.setGoodsId(storeGoodsQuotationGoods.getGoodsId());
        int goodsNum = storeGoodsQuotationGoodsMapper.selectGoodsNameNum(dto);
        if(goodsNum > 0) {
            throw new BusinessException("报价单商品名称不能重复！");
        }
    }
}
