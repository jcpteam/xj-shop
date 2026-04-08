package com.javaboot.shop.service;

import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreGoodsQuotation;
import com.javaboot.shop.domain.StoreMember;

import java.util.List;

/**
 * 商城会员信息Service接口
 *
 * @author javaboot
 * @date 2019-08-30
 */
public interface IStoreMemberService {
    /**
     * 查询商城会员信息
     *
     * @param id
     *            商城会员信息ID
     * @return 商城会员信息
     */
    public StoreMember selectStoreMemberById(Long id);

    /**
     * 查询商城会员信息列表
     *
     * @param storeMember
     *            商城会员信息
     * @return 商城会员信息集合
     */
    public List<StoreMember> selectStoreMemberList(StoreMember storeMember);

    /**
     * 查询登陆用户所属区域的会员
     * 
     * @param
     * @return
     */
    List<StoreMember> selectStoreMemberListOfLoginUser();

    StoreGoodsQuotation quotationWithMemberId(StoreMember storeMember);

    /**
     * 新增商城会员信息
     *
     * @param storeMember
     *            商城会员信息
     * @return 结果
     */
    public int insertStoreMember(StoreMember storeMember);

    /**
     * 修改商城会员信息
     *
     * @param storeMember
     *            商城会员信息
     * @return 结果
     */
    public int updateStoreMember(StoreMember storeMember);
    public int batchSaveOpmanagerId(StoreMember storeMember);


    /**
     * 修改商户报价单信息
     *
     * @param storeMember 商城会员信息
     * @return 结果
     */
    int updateQuotation(StoreMember storeMember);

    /**
     * 批量删除商城会员信息
     *
     * @param ids
     *            需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreMemberByIds(String ids);

    /**
     * 删除商城会员信息信息
     *
     * @param id
     *            商城会员信息ID
     * @return 结果
     */
    public int deleteStoreMemberById(Long id);


    /**
     * 查询业务员列表
     * @return
     */
    public List<StoreMember> queryOpmanagerList();
}
