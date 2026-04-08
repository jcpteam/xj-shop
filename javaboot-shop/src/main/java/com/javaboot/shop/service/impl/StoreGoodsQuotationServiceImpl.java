package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.GoodsQuotationStatus;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import com.javaboot.shop.domain.StoreGoodsUnitPrice;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsQueryDTO;
import com.javaboot.shop.dto.StoreGoodsQuotationQueryDTO;
import com.javaboot.shop.mapper.StoreGoodsQuotationGoodsMapper;
import com.javaboot.shop.mapper.StoreGoodsQuotationMapper;
import com.javaboot.shop.mapper.StoreGoodsUnitPriceMapper;
import com.javaboot.shop.mapper.StoreMemberMapper;
import com.javaboot.shop.service.IStoreGoodsQuotationService;
import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报价信息Service业务层处理
 *
 * @author lqh
 * @date 2021-05-18
 */
@Service
public class StoreGoodsQuotationServiceImpl implements IStoreGoodsQuotationService {
    @Autowired
    private StoreGoodsQuotationMapper storeGoodsQuotationMapper;
    @Autowired
    private StoreGoodsQuotationGoodsMapper goodsQuotationGoodsMapper;

    @Autowired
    private StoreMemberMapper storeMemberMapper;
    @Autowired
    private StoreGoodsUnitPriceMapper storeGoodsUnitPriceMapper;

    /**
     * 查询报价信息
     *
     * @param quotationid 报价信息ID
     * @return 报价信息
     */
    @Override
    public StoreGoodsQuotation selectStoreGoodsQuotationById(Long quotationid) {
        return storeGoodsQuotationMapper.selectStoreGoodsQuotationById(quotationid);
    }

    /**
     * 查询报价信息列表
     *
     * @param storeGoodsQuotation 报价信息
     * @return 报价信息
     */
    @Override
    public List<StoreGoodsQuotation> selectStoreGoodsQuotationList(StoreGoodsQuotationQueryDTO storeGoodsQuotation) {
        List<StoreGoodsQuotation> list = storeGoodsQuotationMapper.selectStoreGoodsQuotationList(storeGoodsQuotation);
        if (CollectionUtils.isNotEmpty(list)) {
            //查询商品在售数量：
            QueryWrapper<StoreGoodsQuotationGoods> queryGoodsWrapper = new QueryWrapper<>();
            queryGoodsWrapper.eq("status","1");
            queryGoodsWrapper.in("quotation_id", list.stream().map(StoreGoodsQuotation::getQuotationId).collect(Collectors.toSet()));
            List<StoreGoodsQuotationGoods> goods = goodsQuotationGoodsMapper.selectList(queryGoodsWrapper);
            if (CollectionUtils.isNotEmpty(goods)) {
                list.forEach(e ->
                    e.setGoodsNum((int) goods.stream().filter(g -> e.getQuotationId().equals(g.getQuotationId())).count())
                );
            }

            //关联的商户数量
            StoreMember storeMember = new StoreMember();
            storeMember.setQuotationIds(new ArrayList<>(list.stream().map(StoreGoodsQuotation::getQuotationId).collect(Collectors.toSet())));
            List<StoreMember> storeMembers = storeMemberMapper.selectStoreMemberList(storeMember);
            if (CollectionUtils.isNotEmpty(storeMembers)) {
                list.forEach(e ->
                    e.setMeberNum((int) storeMembers.stream().filter(g -> e.getQuotationId().equals(g.getQuotationId())).count())
                );
            }
        }
        return list;
    }

    @Override
    public List<StoreGoodsQuotation> selectStoreGoodsQuotationListOfUserOrQuotation(StoreGoodsQuotationQueryDTO storeGoodsQuotation) {
        StoreGoodsQuotationQueryDTO query = new StoreGoodsQuotationQueryDTO();
        //有报价单id按区域id查
        if (storeGoodsQuotation.getQuotationId() != null) {
            query.setQuotationId(storeGoodsQuotation.getQuotationId());
        } else {
            //没有则按登陆用户部门 超级管理员看全部
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            if (!user.isAdmin()) {
                query.setDeptId(user.getDeptId());
            }
            if(StringUtils.isNotEmpty(storeGoodsQuotation.getDeptId())) {
                query.setDeptId(storeGoodsQuotation.getDeptId());
            }
        }
        return storeGoodsQuotationMapper.selectStoreGoodsQuotationList(query);
    }

    /**
     * 新增报价信息
     *
     * @param storeGoodsQuotation 报价信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStoreGoodsQuotation(StoreGoodsQuotation storeGoodsQuotation) {
        return storeGoodsQuotationMapper.insertStoreGoodsQuotation(storeGoodsQuotation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int copyStoreGoodsQuotation(StoreGoodsQuotation storeGoodsQuotation) {
        // 获取报价单信息
        StoreGoodsQuotation storeGoodsQuotationDb = storeGoodsQuotationMapper.selectStoreGoodsQuotationById(storeGoodsQuotation.getQuotationId());
        storeGoodsQuotationDb.setQuotationId(null);
        storeGoodsQuotationDb.setCreateTime(new Date());
        storeGoodsQuotationDb.setLastModifyTime(new Date());
        storeGoodsQuotationDb.setQuotationName(storeGoodsQuotationDb.getQuotationName().concat(DateUtils.parseDateToStr("HHmmss", new Date())));
        storeGoodsQuotationDb.setQuotationAbbreviation(storeGoodsQuotationDb.getQuotationName());
        // 获取报价单下商品
        StoreGoodsQuotationGoodsQueryDTO dto = new StoreGoodsQuotationGoodsQueryDTO();
        dto.setQuotationId(storeGoodsQuotation.getQuotationId());
        List<StoreGoodsQuotationGoodsVO> quotationGoodsVOList = goodsQuotationGoodsMapper.selectStoreGoodsQuotationGoodsList(dto);

        // 新增报价单
        int i = storeGoodsQuotationMapper.insertStoreGoodsQuotation(storeGoodsQuotationDb);

        // 新增报价单商品
        quotationGoodsVOList.forEach(quotationGoodsVO->{

            QueryWrapper<StoreGoodsUnitPrice> goodsUnitPriceQueryWrapper = new QueryWrapper<>();
            goodsUnitPriceQueryWrapper.eq("goods_id",quotationGoodsVO.getGoodsId());
            List<StoreGoodsUnitPrice> goodsUnitPriceList = storeGoodsUnitPriceMapper.selectList(goodsUnitPriceQueryWrapper);

            StoreGoodsQuotationGoods quotationGoods = new StoreGoodsQuotationGoods();
            BeanUtils.copyProperties(quotationGoodsVO, quotationGoods);
            quotationGoods.setQuotationId(storeGoodsQuotationDb.getQuotationId());
            quotationGoods.setGoodsId(null);
            quotationGoods.setSaleNum(0.0);
            quotationGoods.setSalePercent(0.0);
            goodsQuotationGoodsMapper.insertStoreGoodsQuotationGoods(quotationGoods);

            goodsUnitPriceList.forEach(obj -> {
                StoreGoodsUnitPrice storeGoodsUnitPrice = new StoreGoodsUnitPrice();
                BeanUtils.copyProperties(obj, storeGoodsUnitPrice);
                storeGoodsUnitPrice.setGoodsId(quotationGoods.getGoodsId());
                storeGoodsUnitPriceMapper.insert(storeGoodsUnitPrice);
            });
        });
        return i;
    }

    /**
     * 修改报价信息
     *
     * @param storeGoodsQuotation 报价信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreGoodsQuotation(StoreGoodsQuotation storeGoodsQuotation) {
        if( storeGoodsQuotation.getStatus().equals(GoodsQuotationStatus.OFF.getValue())) {
            // 激活状态变成非激活状态，关联了商户不能修改
            StoreMember member = new StoreMember();
            member.setQuotationId(storeGoodsQuotation.getQuotationId());
            List<StoreMember> memberList = storeMemberMapper.selectStoreMemberList(member);
            if(CollectionUtils.isNotEmpty(memberList)) {
                throw new BusinessException("报价单关联了商户，不能修改激活状态！");
            }

            // 同步下架对应的商品
            StoreGoodsQuotationGoods goodsQuotationGoods = new StoreGoodsQuotationGoods();
            goodsQuotationGoods.setStatus(GoodsQuotationStatus.OFF.getValue());
            QueryWrapper<StoreGoodsQuotationGoods> queryWrapper = new QueryWrapper();
            queryWrapper.eq("quotation_id", storeGoodsQuotation.getQuotationId());
            goodsQuotationGoodsMapper.update(goodsQuotationGoods, queryWrapper);

        }
        return storeGoodsQuotationMapper.updateStoreGoodsQuotation(storeGoodsQuotation);
    }

    /**
     * 删除报价信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsQuotationByIds(String ids) {
        return storeGoodsQuotationMapper.deleteStoreGoodsQuotationByIds(Convert.toStrArray(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeStoreGoodsQuotationByIds(String ids) {
        Long quotationId = Long.parseLong(ids);
        // 删除报价单，关联了商户不允许删除
        StoreMember member = new StoreMember();
        member.setQuotationId(quotationId);
        List<StoreMember> memberList = storeMemberMapper.selectStoreMemberList(member);
        if(CollectionUtils.isNotEmpty(memberList)) {
            throw new BusinessException("报价单关联了商户，不能被删除！");
        }

        QueryWrapper<StoreGoodsQuotationGoods> queryWrapper = new QueryWrapper();
        queryWrapper.in("quotation_id", Arrays.asList(Convert.toStrArray(ids)));
        queryWrapper.eq("status", Constants.NORMAL);
        Page<StoreGoodsQuotationGoods> page = new Page<>(0, 1);
        Page<StoreGoodsQuotationGoods> pageDb = goodsQuotationGoodsMapper.selectPage(page, queryWrapper);
        if(pageDb.getTotal() > 0) {
            throw new BusinessException("报价单下存在销售商品，不能被删除！");
        }

        StoreGoodsQuotation storeGoodsQuotation = new StoreGoodsQuotation();
        storeGoodsQuotation.setQuotationId(quotationId);
        storeGoodsQuotation.setStatus("0");
        return storeGoodsQuotationMapper.updateStoreGoodsQuotation(storeGoodsQuotation);
    }

    /**
     * 删除报价信息信息
     *
     * @param quotationid 报价信息ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStoreGoodsQuotationById(Long quotationid) {
        return storeGoodsQuotationMapper.deleteStoreGoodsQuotationById(quotationid);
    }
}
