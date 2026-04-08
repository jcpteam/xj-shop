package com.javaboot.shop.service.impl;

import com.javaboot.common.core.text.Convert;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StoreGoodsQuotationQueryDTO;
import com.javaboot.shop.mapper.StoreGoodsQuotationMapper;
import com.javaboot.shop.mapper.StoreMemberMapper;
import com.javaboot.shop.service.IStoreMemberService;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.mapper.SysDeptMapper;
import com.javaboot.system.mapper.SysUserMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 商城会员信息Service业务层处理
 *
 * @author javaboot
 * @date 2019-08-30
 */
@Service
public class StoreMemberServiceImpl implements IStoreMemberService {
    @Autowired
    private StoreMemberMapper storeMemberMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private StoreGoodsQuotationMapper storeGoodsQuotationMapper;


    /**
     * 查询商城会员信息
     *
     * @param id 商城会员信息ID
     * @return 商城会员信息
     */
    @Override
    public StoreMember selectStoreMemberById(Long id) {
        StoreMember storeMember = storeMemberMapper.selectStoreMemberById(id);
        if(StringUtils.isNotBlank(storeMember.getSuperCustomer())) {
            StoreMember storeMemberQry = new StoreMember();
            storeMemberQry.setCustomerNo(storeMember.getSuperCustomer());
            List<StoreMember> parent = storeMemberMapper.selectStoreMemberList(storeMemberQry);
            if(CollectionUtils.isNotEmpty(parent)) {
                storeMember.setSuperCustomerName(parent.get(0).getNickname());
            }
        }
        return storeMember;
    }

    /**
     * 查询商城会员信息列表
     *
     * @param storeMember 商城会员信息
     * @return 商城会员信息
     */
    @Override
    public List<StoreMember> selectStoreMemberList(StoreMember storeMember) {
        List<StoreMember> list = storeMemberMapper.selectStoreMemberList(storeMember);

        // 总公司列表
        List<StoreMember> superList = new ArrayList<>();
        // 填充所属区域
        // 填充所属报价单
        Set<String> deptIds = new HashSet<>();
        //Set<String> superNos = new HashSet<>();
        Set<Long> quotationIds = new HashSet<>();
        list.forEach(o->{
            if(StringUtils.isNotEmpty(o.getCustomerArea())) {
                deptIds.add(o.getCustomerArea());
            }
            if(o.getQuotationId() != null && o.getQuotationId() != 0) {
                quotationIds.add(o.getQuotationId());
            }
//            if(StringUtils.isNotEmpty(o.getSuperCustomer())) {
//                superNos.add(o.getSuperCustomer());
//            }
        });

//        if(CollectionUtils.isNotEmpty(superNos)) {
//            StoreMember storeMemberQry = new StoreMember();
//            storeMemberQry.setCustomerNOs(new ArrayList<>(superNos));
//            superList = storeMemberMapper.selectStoreMemberList(storeMemberQry);
//        }

//        if(CollectionUtils.isNotEmpty(superList)) {
//            for (StoreMember o : list) {
//                for (StoreMember superMember : superList) {
//                    if (superMember.getCustomerNo().equals(o.getSuperCustomer())) {
//                        o.setSuperCustomerName(superMember.getNickname());
//                        break;
//                    }
//                }
//            }
//        }

        SysDept sysDept = new SysDept();
        sysDept.setDeptIdList(new ArrayList<>(deptIds));
        List<SysDept> deptList = deptMapper.selectDeptList(sysDept);
        StoreGoodsQuotationQueryDTO storeGoodsQuotationQueryDTO = new StoreGoodsQuotationQueryDTO();
        storeGoodsQuotationQueryDTO.setQuotationIdList(new ArrayList<>(quotationIds));
        List<StoreGoodsQuotation> storeGoodsQuotationList =  storeGoodsQuotationMapper.selectStoreGoodsQuotationList(storeGoodsQuotationQueryDTO);
        list.forEach(o->{
            if(CollectionUtils.isNotEmpty(deptList)) {
                deptList.forEach(d->{
                    if(d.getDeptId().equals(o.getCustomerArea())){
                        o.setDeptName(d.getDeptName());
                    }
                });
            }

            if(CollectionUtils.isNotEmpty(storeGoodsQuotationList)) {
                storeGoodsQuotationList.forEach(s->{
                    if(s.getQuotationId().equals(o.getQuotationId())){
                        o.setQuotationName(s.getQuotationName());
                    }
                });
            }
        });

        return list;
    }

    /**
     * 查询商城会员信息列表
     *
     * @param storeMember 商城会员信息
     * @return 商城会员信息
     */
    @Override
    public StoreGoodsQuotation quotationWithMemberId(StoreMember storeMember) {
        StoreMember storeMember1 = storeMemberMapper.selectStoreMemberById(storeMember.getId());
        if(storeMember1 == null || storeMember1.getQuotationId() == null){
            return null;
        }
        return storeGoodsQuotationMapper.selectStoreGoodsQuotationById(storeMember1.getQuotationId());
    }

    @Override
    public List<StoreMember> selectStoreMemberListOfLoginUser() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        StoreMember query = new StoreMember();
        if(!user.isAdmin()){
            query.setCustomerArea(user.getDeptId());
        }
        return  selectStoreMemberList(query);
    }

    /**
     * 新增商城会员信息
     *
     * @param storeMember 商城会员信息
     * @return 结果
     */
    @Override
    public int insertStoreMember(StoreMember storeMember) {
        storeMember.setCreateTime(DateUtils.getNowDate());
        return storeMemberMapper.insertStoreMember(storeMember);
    }

    /**
     * 修改商城会员信息
     *
     * @param storeMember 商城会员信息
     * @return 结果
     */
    @Override
    public int updateStoreMember(StoreMember storeMember) {
        storeMember.setUpdateTime(DateUtils.getNowDate());
        return storeMemberMapper.updateStoreMember(storeMember);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSaveOpmanagerId(StoreMember storeMember) {
        int c = 0;
        if(StringUtils.isNotEmpty(storeMember.getMemberIds()) && StringUtils.isNotEmpty(storeMember.getOpmanagerId())){
            String[] arr = storeMember.getMemberIds().split(",");
            for (int i = 0;i< arr.length ;i++){
                if(StringUtils.isNotEmpty(arr[i])){
                    StoreMember updateM = new StoreMember();
                    updateM.setUpdateTime(DateUtils.getNowDate());
                    updateM.setId(Long.valueOf(arr[i]));
                    updateM.setOpmanagerId(storeMember.getOpmanagerId());
                    c += storeMemberMapper.updateStoreMember(updateM);
                }
            }
        }
        return c;
    }

    /**
     * 修改商户报价单信息
     *
     * @param storeMember 商城会员信息
     * @return 结果
     */
    @Override
    public int updateQuotation(StoreMember storeMember) {
        storeMember.setUpdateTime(DateUtils.getNowDate());
        return storeMemberMapper.updateStoreMemberQuotationId(storeMember);
    }

    /**
     * 删除商城会员信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStoreMemberByIds(String ids) {
        return storeMemberMapper.deleteStoreMemberByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商城会员信息信息
     *
     * @param id 商城会员信息ID
     * @return 结果
     */
    @Override
    public int deleteStoreMemberById(Long id) {
        return storeMemberMapper.deleteStoreMemberById(id);
    }

    @Override
    public List<StoreMember> queryOpmanagerList() {
        return storeMemberMapper.queryOpmanagerList();
    }
}
