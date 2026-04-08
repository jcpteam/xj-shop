package com.javaboot.shop.service.impl;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.BlackWhiteType;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreBlackWhite;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.mapper.StoreBlackWhiteMapper;
import com.javaboot.shop.mapper.StoreGoodsOrderMapper;
import com.javaboot.shop.mapper.StoreMemberMapper;
import com.javaboot.shop.service.IStoreBlackWhiteService;
import com.javaboot.shop.service.IStoreMemberService;
import com.javaboot.shop.vo.StoreBlackWhiteVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.service.ISysDeptService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 黑白名单Service业务层处理
 * 
 * @author lqh
 * @date 2021-07-08
 */
@Service
public class StoreBlackWhiteServiceImpl implements IStoreBlackWhiteService {
    @Autowired
    private StoreBlackWhiteMapper storeBlackWhiteMapper;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IStoreMemberService storeMemberServic;
    /**
     * 查询黑白名单
     * 
     * @param blackWhiteId 黑白名单ID
     * @return 黑白名单
     */
    @Override
    public StoreBlackWhite selectStoreBlackWhiteById(Long blackWhiteId) {
        return storeBlackWhiteMapper.selectStoreBlackWhiteById(blackWhiteId);
    }

    /**
     * 查询黑白名单列表
     * 
     * @param storeBlackWhite 黑白名单
     * @return 黑白名单
     */
    @Override
    public List<StoreBlackWhiteVO> selectStoreBlackWhiteList(StoreBlackWhite storeBlackWhite) {
        List<StoreBlackWhiteVO> list= storeBlackWhiteMapper.selectStoreBlackWhiteList(storeBlackWhite);
        SysDept deptQuery=new SysDept();
        deptQuery.setDeptIdList(list.stream().map(StoreBlackWhiteVO::getDeptId).collect(Collectors.toList()));
        List<SysDept> deptList=deptService.selectDeptList(deptQuery);
        StoreMember query = new StoreMember();
        query.setIds(list.stream().map(StoreBlackWhiteVO::getMerchantId).collect(Collectors.toList()));
        List<StoreMember> memberList = storeMemberServic.selectStoreMemberList(query);
        list.forEach(o->{
            SysDept dept= CollectionUtils.isEmpty(deptList)?null: deptList.stream().filter(d->d.getDeptId().equals(o.getDeptId())).findFirst().orElse(null);
            StoreMember member= CollectionUtils.isEmpty(memberList)?null: memberList.stream().filter(d->d.getId().toString().equals(o.getMerchantId())).findFirst().orElse(null);
            if(dept!=null){
                o.setDeptName(dept.getDeptName());
            }
            if(member!=null){
                o.setMerchantName(member.getNickname());
                o.setMerchantType(member.getCustomerType());
            }
            o.setTypeName(BlackWhiteType.getDescValue(o.getType()));
        });
        return list;
    }

    /**
     * 新增黑白名单
     * 
     * @param storeBlackWhite 黑白名单
     * @return 结果
     */
    @Override
    public int insertStoreBlackWhite(StoreBlackWhite storeBlackWhite) {
        storeBlackWhite.setStatus(Constants.NORMAL);
        storeBlackWhite.setCreateTime(DateUtils.getNowDate());
        return storeBlackWhiteMapper.insertStoreBlackWhite(storeBlackWhite);
    }

    /**
     * 修改黑白名单
     * 
     * @param storeBlackWhite 黑白名单
     * @return 结果
     */
    @Override
    public int updateStoreBlackWhite(StoreBlackWhite storeBlackWhite) {
        storeBlackWhite.setLastModifyTime(DateUtils.getNowDate());
        return storeBlackWhiteMapper.updateStoreBlackWhite(storeBlackWhite);
    }

    /**
     * 删除黑白名单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreBlackWhiteByIds(String ids) {
        return storeBlackWhiteMapper.deleteStoreBlackWhiteByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除黑白名单信息
     * 
     * @param blackWhiteId 黑白名单ID
     * @return 结果
     */
    @Override
    public int deleteStoreBlackWhiteById(Long blackWhiteId) {
        return storeBlackWhiteMapper.deleteStoreBlackWhiteById(blackWhiteId);
    }

    /**
     * 删除黑白名单对象
     *
     * @param storeBlackWhite 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int updateStoreBlackWhiteType(StoreBlackWhite storeBlackWhite) {
        Map<String, Object> map = new HashMap<>();
        map.put("idList",Arrays.asList(Convert.toStrArray(storeBlackWhite.getIds())));
        map.put("type",storeBlackWhite.getType());
        return storeBlackWhiteMapper.updateStoreBlackWhiteType(map);
    }

    @Autowired
    private StoreGoodsOrderMapper storeGoodsOrderMapper;

    @Autowired
    private StoreMemberMapper storeMemberMapper;

    public void updateBlackOrWhiteStatus(StorePaymentAccountVO vo) {

        //  永久白名单跳过检查
        StoreBlackWhite blackWhiteQry = new StoreBlackWhite();
        blackWhiteQry.setAccountId(vo.getAccountId());
        blackWhiteQry.setStatus(Constants.NORMAL);
        List<StoreBlackWhiteVO> blackWhiteVOList = storeBlackWhiteMapper.selectStoreBlackWhiteList(blackWhiteQry);
        if(CollectionUtils.isNotEmpty(blackWhiteVOList)
            && BlackWhiteType.WhiteForever.getCode().equals(blackWhiteVOList.get(0).getType())) {
            return;
        }

        // 获取商户对应的账单周期
        int billDays = Integer.parseInt(vo.getBillDays());

        List<String> merchantIds = new ArrayList<>();
        StoreMember storeMember = storeMemberMapper.selectStoreMemberById(Long.parseLong(vo.getMemberId()));
        vo.setDeptId(storeMember.getCustomerArea());
        if (storeMember != null)
        {
            merchantIds.add(storeMember.getId()+"");
            // 获取结算账户对应的子账号
            StoreMember storeMemberQry = new StoreMember();
            storeMemberQry.setSuperCustomer(storeMember.getId()+"");
            List<StoreMember> childMemberList = storeMemberMapper.selectStoreMemberList(storeMemberQry);
            if(CollectionUtils.isNotEmpty(childMemberList)) {
                childMemberList.forEach(o->{
                    merchantIds.add(o.getId()+"");
                });
            }

        }

        // 获取支付账户对应的订单
        StoreGoodsOrderQueryDTO orderQueryDTO = new StoreGoodsOrderQueryDTO();
        orderQueryDTO.setStatusList(Arrays.asList(new String[]{"3"}));
        orderQueryDTO.setMerchantIdList(merchantIds);
        orderQueryDTO.setPayStatus("0");
        List<StoreGoodsOrderVO> orderList = storeGoodsOrderMapper.selectStoreGoodsOrderList(orderQueryDTO);

        boolean isBlack = false;
        if(CollectionUtils.isNotEmpty(orderList)) {
            for (StoreGoodsOrderVO orderVO: orderList) {
                Calendar calendar = Calendar.getInstance() ;
                calendar.setTime(orderVO.getCreateTime());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                Date orderDate = orderVO.getCreateTime();
                if(hour < 9) {
                    orderDate = DateUtils.addDays(orderVO.getCreateTime(), -1);
                }

                orderDate = DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, orderDate));

                Date now = DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date()));


                Long interval = DateUtils.getDatePoorLong(now, orderDate);

                // 付款时间超过了账期，设置成黑名单
                if(interval > billDays) {
                    isBlack = true;
                    break;
                }
            }
        }


        if(StringUtils.isNotEmpty(vo.getDeptId())) {
            StoreBlackWhite insert = new StoreBlackWhite();
            insert.setAccountId(vo.getAccountId());
            insert.setMerchantId(vo.getMemberId());
            insert.setType(isBlack ? BlackWhiteType.Black.getCode() : BlackWhiteType.White.getCode());
            insert.setDeptId(vo.getDeptId());
            insert.setStatus(Constants.NORMAL);
            storeBlackWhiteMapper.deleteStoreBlackWhiteByAccount(vo.getAccountId());
            storeBlackWhiteMapper.insertStoreBlackWhite(insert);
        }
    }
}
