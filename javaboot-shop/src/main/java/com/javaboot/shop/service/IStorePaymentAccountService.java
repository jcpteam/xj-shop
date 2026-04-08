package com.javaboot.shop.service;

import java.util.List;

import com.javaboot.shop.domain.StorePaymentAccount;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.vo.StorePaymentAccountVO;

/**
 * 支付账户Service接口
 * 
 * @author lqh
 * @date 2021-07-09
 */
public interface IStorePaymentAccountService {
    /**
     * 查询支付账户
     * 
     * @param accountId 支付账户ID
     * @return 支付账户
     */
    public StorePaymentAccount selectStorePaymentAccountById(String accountId);

    /**
     * 查询支付账户列表
     * 
     * @param storePaymentAccount 支付账户
     * @return 支付账户集合
     */
    public List<StorePaymentAccountVO> selectStorePaymentAccountList(StorePaymentAccountQueryDTO storePaymentAccount);

    /**
     * 新增支付账户
     * 
     * @param storePaymentAccount 支付账户
     * @return 结果
     */
    public int insertStorePaymentAccount(StorePaymentAccount storePaymentAccount);

    /**
     * 修改支付账户
     * 
     * @param storePaymentAccount 支付账户
     * @return 结果
     */
    public int updateStorePaymentAccount(StorePaymentAccount storePaymentAccount);

    /**
     * 批量删除支付账户
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStorePaymentAccountByIds(String ids);

    /**
     * 删除支付账户信息
     * 
     * @param accountId 支付账户ID
     * @return 结果
     */
    public int deleteStorePaymentAccountById(String accountId);
    /**
     * 修改账户金额
     * @param accountId
     * @param cash
     * @param oldCash
     * @return
     */
    int updateStorePaymentAccountCash( String accountId,  Double cash,  Double oldCash);

    /**
     * 充值提现
     * @param storePaymentAccount
     * @return
     */
    int updateAccountNums(StorePaymentAccount storePaymentAccount);

}
