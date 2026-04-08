package com.javaboot.shop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.enums.AccountFlowType;
import com.javaboot.common.enums.AccountType;
import com.javaboot.common.enums.PaymentAccountStatus;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreAccountFlow;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.service.IStoreAccountFlowService;
import com.javaboot.shop.service.IStoreMemberService;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.shop.mapper.StorePaymentAccountMapper;
import com.javaboot.shop.domain.StorePaymentAccount;
import com.javaboot.shop.service.IStorePaymentAccountService;
import com.javaboot.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付账户Service业务层处理
 * 
 * @author lqh
 * @date 2021-07-09
 */
@Service
public class StorePaymentAccountServiceImpl implements IStorePaymentAccountService {
    @Autowired
    private StorePaymentAccountMapper storePaymentAccountMapper;
    @Autowired
    private IStoreMemberService storeMemberService;
    @Autowired
    private IStoreAccountFlowService storeAccountFlowService;
    /**
     * 查询支付账户
     * 
     * @param accountId
     *            支付账户ID
     * @return 支付账户
     */
    @Override
    public StorePaymentAccount selectStorePaymentAccountById(String accountId) {
        return storePaymentAccountMapper.selectStorePaymentAccountById(accountId);
    }

    /**
     * 查询支付账户列表
     * 
     * @param storePaymentAccount
     *            支付账户
     * @return 支付账户
     */
    @Override
    public List<StorePaymentAccountVO> selectStorePaymentAccountList(StorePaymentAccountQueryDTO storePaymentAccount) {
        List<StorePaymentAccountVO> list = storePaymentAccountMapper.selectStorePaymentAccountList(storePaymentAccount);
        if (CollectionUtils.isNotEmpty(list)) {
            StoreMember query = new StoreMember();
            query.setIds(list.stream().map(StorePaymentAccount::getMemberId).collect(Collectors.toList()));
            List<StoreMember> memberList = storeMemberService.selectStoreMemberList(query);
            if (CollectionUtils.isNotEmpty(memberList)) {
                list.forEach(l -> {
                    l.setStatusName(PaymentAccountStatus.getDescValue(l.getStatus()));
                    StoreMember me = memberList.stream().filter(e -> e.getId().toString().equals(l.getMemberId()))
                        .findFirst().orElse(null);
                    if (me != null) {
                        // 设置门店名称
                        l.setStoreName(me.getNickname());
                    }
                });
            }
        }
        return list;
    }

    /**
     * 新增支付账户
     * 
     * @param storePaymentAccount
     *            支付账户
     * @return 结果
     */
    @Override
    public int insertStorePaymentAccount(StorePaymentAccount storePaymentAccount) {
        StoreMember me= storeMemberService.selectStoreMemberById(Long.parseLong(storePaymentAccount.getMemberId()));
        if(me==null){
            throw new BusinessException("未查到商户【" + storePaymentAccount.getMemberId() + "】");
        }
        StorePaymentAccountQueryDTO query=new StorePaymentAccountQueryDTO();
        query.setMemberId(storePaymentAccount.getMemberId());
        List<StorePaymentAccountVO> list= selectStorePaymentAccountList(query);
        if(CollectionUtils.isNotEmpty(list)){
            throw new BusinessException("商户【" + me.getNickname() + "】已绑定支付账号"+list.get(0).getAccountNumber());
        }
        storePaymentAccount.setCreateTime(DateUtils.getNowDate());
        storePaymentAccount.setAccountNumber(CodeConstants.PAY+me.getCustomerNo());
        return storePaymentAccountMapper.insertStorePaymentAccount(storePaymentAccount);
    }

    /**
     * 修改支付账户
     * 
     * @param storePaymentAccount
     *            支付账户
     * @return 结果
     */
    @Override
    public int updateStorePaymentAccount(StorePaymentAccount storePaymentAccount) {
        return storePaymentAccountMapper.updateStorePaymentAccount(storePaymentAccount);
    }

    /**
     * 充值提现
     *
     * @param storePaymentAccount
     *            支付账户
     * @return 结果
     */
    @Override
    @Transactional
    public int updateAccountNums(StorePaymentAccount storePaymentAccount) {


        StorePaymentAccount dbObj = storePaymentAccountMapper.selectStorePaymentAccountById(storePaymentAccount.getAccountId());

        // 生成支付流水
        StoreAccountFlow storeAccountFlow = new StoreAccountFlow();
        storeAccountFlow.setAccountId(dbObj.getAccountId());
        storeAccountFlow.setAccountNumber(dbObj.getAccountNumber());
        storeAccountFlow.setChangeAmount(storePaymentAccount.getAmount());
        storeAccountFlow.setOldAmount(dbObj.getCash());
        storeAccountFlow.setType(storePaymentAccount.getOperateType());
        storeAccountFlow.setRemark(storePaymentAccount.getRemark());
        if(AccountFlowType.WITHDRAW.getCode().equals(storePaymentAccount.getOperateType())) {
            switch (storePaymentAccount.getAccountType()) {
                case "1":
                    if(dbObj.getCash() < storePaymentAccount.getAmount()) {
                        throw new BusinessException("现金账户余额不足！");
                    }
                    dbObj.setCash(dbObj.getCash() - storePaymentAccount.getAmount());
                    storeAccountFlow.setNowAmount(dbObj.getCash());
                    break;
                case "2":
                    if(dbObj.getBondAccount() < storePaymentAccount.getAmount()) {
                        throw new BusinessException("保证金账户余额不足！");
                    }
                    dbObj.setBondAccount(dbObj.getBondAccount() - storePaymentAccount.getAmount());
                    storeAccountFlow.setNowAmount(dbObj.getBondAccount());
                    break;
                case "3":
                    if(dbObj.getRebateAccount() < storePaymentAccount.getAmount()) {
                        throw new BusinessException("返点账户余额不足！");
                    }
                    dbObj.setRebateAccount(dbObj.getRebateAccount() - storePaymentAccount.getAmount());
                    storeAccountFlow.setNowAmount(dbObj.getRebateAccount());
                    break;
            }
        }
        // 充值
        if(AccountFlowType.RECHARGE.getCode().equals(storePaymentAccount.getOperateType())) {
            switch (storePaymentAccount.getAccountType()) {
                case "1":
                    dbObj.setCash(dbObj.getCash() + storePaymentAccount.getAmount());
                    storeAccountFlow.setNowAmount(dbObj.getCash());
                    break;
                case "2":
                    dbObj.setBondAccount(dbObj.getBondAccount() + storePaymentAccount.getAmount());
                    storeAccountFlow.setNowAmount(dbObj.getBondAccount());
                    break;
                case "3":
                    dbObj.setRebateAccount(dbObj.getRebateAccount() + storePaymentAccount.getAmount());
                    storeAccountFlow.setNowAmount(dbObj.getRebateAccount());
                    break;
            }
        }
        storeAccountFlowService.insertStoreAccountFlow(storeAccountFlow);
        return storePaymentAccountMapper.updateStorePaymentAccount(dbObj);

    }
    /**
     * 删除支付账户对象
     * 
     * @param ids
     *            需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteStorePaymentAccountByIds(String ids) {
        return storePaymentAccountMapper.deleteStorePaymentAccountByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付账户信息
     * 
     * @param accountId
     *            支付账户ID
     * @return 结果
     */
    @Override
    public int deleteStorePaymentAccountById(String accountId) {
        return storePaymentAccountMapper.deleteStorePaymentAccountById(accountId);
    }

    /**
     * 修改账户金额
     * 
     * @param accountId
     * @param cash
     * @param oldCash
     * @return
     */
    @Override
    public int updateStorePaymentAccountCash(String accountId, Double cash, Double oldCash) {
        synchronized ((accountId + "").intern()) {
            return storePaymentAccountMapper.updateStorePaymentAccountCash(accountId, cash, oldCash);
        }
    }

}
