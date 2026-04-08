package com.javaboot.shop.service.impl;

import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.*;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.StoreGoodsOrderDTO;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.dto.StoreReceiptQueryDTO;
import com.javaboot.shop.mapper.StoreGoodsOrderMapper;
import com.javaboot.shop.mapper.StorePaymentAccountMapper;
import com.javaboot.shop.mapper.StorePaymentFlowMapper;
import com.javaboot.shop.mapper.StoreReceiptMapper;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import com.javaboot.shop.vo.StoreReceiptVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysDeptService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 收款单Service业务层处理
 *
 * @author lqh
 * @date 2021-07-05
 */
@Service
public class StoreReceiptServiceImpl implements IStoreReceiptService {
    private static final Logger log = LoggerFactory.getLogger(StoreReceiptServiceImpl.class);

    @Autowired
    private StoreReceiptMapper storeReceiptMapper;
    @Autowired
    private IStoreGoodsOrderService storeGoodsOrderService;
    @Autowired
    private IStoreAccountFlowService storeAccountFlowService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IStoreMemberService storeMemberService;

    @Autowired
    private StoreGoodsOrderMapper storeGoodsOrderMapper;

    @Autowired
    private IStorePaymentAccountService storePaymentAccountService;

    @Autowired
    private StorePaymentAccountMapper storePaymentAccountMapper;

    @Autowired
    private IStoreBlackWhiteService storeBlackWhiteService;

    @Autowired
    private StorePaymentFlowMapper storePaymentFlowMapper;

    /**
     * 查询收款单
     *
     * @param receiptId
     *            收款单ID
     * @return 收款单
     */
    @Override
    public StoreReceipt selectStoreReceiptById(Long receiptId) {
        return storeReceiptMapper.selectStoreReceiptById(receiptId);
    }

    /**
     * 查询收款单列表
     *
     * @param storeReceipt
     *            收款单
     * @return 收款单
     */
    @Override
    public List<StoreReceiptVO> selectStoreReceiptList(StoreReceiptQueryDTO storeReceipt, boolean isExport) {
        if(StringUtils.isNotBlank(storeReceipt.getPayBeginDate()) && StringUtils.isNotBlank(storeReceipt.getPayEndDate())) {
            storeReceipt.setPayBeginDate(storeReceipt.getPayBeginDate().concat(" 00:00:00"));
            storeReceipt.setPayEndDate(storeReceipt.getPayEndDate().concat(" 23:59:59"));
        }
        if(StringUtils.isNotBlank(storeReceipt.getReceiptBeginDate()) && StringUtils.isNotBlank(storeReceipt.getReceiptEndDate())) {
            storeReceipt.setReceiptBeginDate(storeReceipt.getReceiptBeginDate().concat(" 00:00:00"));
            storeReceipt.setReceiptEndDate(storeReceipt.getReceiptEndDate().concat(" 23:59:59"));
        }
        List<StoreReceiptVO> list = new ArrayList<>();
        if(isExport) {
            list = storeReceiptMapper.selectExportStoreReceiptList(storeReceipt);
        } else {
            list = storeReceiptMapper.selectStoreReceiptList(storeReceipt);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            SysDept deptQuery = new SysDept();
            deptQuery.setDeptIdList(list.stream().map(StoreReceiptVO::getDeptId).collect(Collectors.toList()));
            List<SysDept> deptList = deptService.selectDeptList(deptQuery);
            StoreMember query = new StoreMember();
            query.setIds(list.stream().map(StoreReceiptVO::getMerchantId).collect(Collectors.toList()));
            List<StoreMember> memberList = storeMemberService.selectStoreMemberList(query);

            StorePaymentAccountQueryDTO queryAccount = new StorePaymentAccountQueryDTO();
            queryAccount.setMemberIdList(query.getIds());
            List<StorePaymentAccountVO> accList = storePaymentAccountMapper.selectStorePaymentAccountList(queryAccount);
            Map<String, StorePaymentAccountVO> acctMap = accList.stream().collect(Collectors.toMap(key->key.getMemberId(), obj->obj));
            list.forEach(o -> {
                o.setPayStatusName(OrderPayStatus.getDescValue(o.getPayStatus()));
                SysDept dept = CollectionUtils.isEmpty(deptList) ? null
                        : deptList.stream().filter(d -> d.getDeptId().equals(o.getDeptId())).findFirst().orElse(null);
                StoreMember member = CollectionUtils.isEmpty(memberList) ? null : memberList.stream()
                        .filter(d -> d.getId().toString().equals(o.getMerchantId())).findFirst().orElse(null);
                if (dept != null) {
                    o.setDeptName(dept.getDeptName());
                }
                if (member != null) {
                    o.setMerchantName(member.getNickname());
                }
                o.setStatusName(ReceiptStatus.getDescValue(o.getStatus()));
                o.setPayTypeName(ReceiptPayType.getDescValue(o.getPayType()));
                if(acctMap.get(o.getMerchantId()) != null) {
                    o.setBalance(acctMap.get(o.getMerchantId()).getCash());
                }
            });
        }
        return list;
    }

    /**
     * 新增收款单
     *
     * @param storeReceipt
     *            收款单
     * @return 结果
     */
    @Override
    public int insertStoreReceipt(StoreReceipt storeReceipt) {
        storeReceipt.setCreateTime(DateUtils.getNowDate());
        return storeReceiptMapper.insertStoreReceipt(storeReceipt);
    }

    /**
     * 批量保存收款单
     *
     * @param orderIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSaveStoreReceipt(String orderIds) {
        StoreGoodsOrderQueryDTO queryOrderDTO = new StoreGoodsOrderQueryDTO();
        List<String> orderIdList=Arrays.asList(orderIds.split(","));
        queryOrderDTO.setOrderIdList(orderIdList);
        List<StoreGoodsOrderVO> orderList = storeGoodsOrderService.selectStoreGoodsOrderList(queryOrderDTO);
        if (CollectionUtils.isNotEmpty(orderList)) {
            //验证订单是否生成过付款单
            StoreReceiptQueryDTO queryDTO=new StoreReceiptQueryDTO();
            queryDTO.setOrderIdList(orderIdList);
            queryDTO.setStatus(ReceiptStatus.NORMAL.getCode());
            List<StoreReceiptVO> list =selectStoreReceiptList(queryDTO, false);
            if(CollectionUtils.isNotEmpty(list)){
                StringBuilder msg=new StringBuilder();
                for (int i = 0; i <list.size() ; i++) {
                    msg.append(list.get(i).getCode()+(i!=list.size()-1?",":""));
                }
                throw new BusinessException("订单【" + msg.toString() + "】已生成过付款单");
            }

            StoreMember query = new StoreMember();
            query.setIds(orderList.stream().map(StoreGoodsOrder::getMerchantId).collect(Collectors.toList()));
            StorePaymentAccountQueryDTO queryAccount = new StorePaymentAccountQueryDTO();
            queryAccount.setMemberIdList(query.getIds());
            List<StorePaymentAccountVO> accList = storePaymentAccountMapper.selectStorePaymentAccountList(queryAccount);
//            List<StorePaymentAccountVO> accList =
//                    storePaymentAccountService.selectStorePaymentAccountList(queryAccount);
            Map<String, List<StoreGoodsOrderVO>> maps =
                    orderList.stream().collect(Collectors.groupingBy(StoreGoodsOrderVO::getMerchantId));
            SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();
            for (Map.Entry<String, List<StoreGoodsOrderVO>> entry : maps.entrySet())
            {
                String k = entry.getKey();
                List<StoreGoodsOrderVO> v = entry.getValue();
                List<String> ids = new ArrayList<>(v.size());
                Double price = 0.0;
                for (StoreGoodsOrderVO o : v)
                {
                    if (OrderStatus.CANCEL.getCode().equals(o.getStatus()))
                    {
                        throw new BusinessException("订单【" + o.getCode() + "】已作废");
                    }
                    if (OrderStatus.WAITING_REVIEW.getCode().equals(o.getStatus()))
                    {
                        throw new BusinessException("订单【" + o.getCode() + "】未审核");
                    }
                    if (OrderStatus.WAITING_SORTING.getCode().equals(o.getStatus()))
                    {
                        throw new BusinessException("订单【" + o.getCode() + "】未分拣");
                    }
                    else if (OrderStatus.DELETE.getCode().equals(o.getStatus()))
                    {
                        throw new BusinessException("订单【" + o.getCode() + "】已删除");
                    }
                    else if (OrderPayStatus.PAYED.getCode().equals(o.getPayStatus()))
                    {
                        throw new BusinessException("订单【" + o.getCode() + "】已付款");
                    }
                    else if (!Constants.PASS.equals(o.getFinancialAuditStatus()))
                    {
                        throw new BusinessException("订单【" + o.getCode() + "】财务未审核");
                    }
                    price += o.getSortingPrice();
                    ids.add(o.getOrderId().toString());

                }
                StorePaymentAccountVO accountVO = CollectionUtils.isEmpty(accList) ?
                        null :
                        accList.stream().filter(a -> a.getMemberId().equals(k)).findFirst().orElse(null);
                StoreReceipt storeReceipt = new StoreReceipt();
                storeReceipt.setAmount(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                storeReceipt.setDeptId(v.get(0).getDeptId());
                // 设置结算客户
                storeReceipt.setMerchantId(k);
                storeReceipt.setCode(CodeConstants.RECEIPT + DateUtils.getRandom());
                storeReceipt.setOrderIds(ids.stream().collect(Collectors.joining(",")));
                storeReceipt.setCreateTime(DateUtils.getNowDate());
                storeReceipt.setLastModifyTime(DateUtils.getNowDate());
                storeReceipt.setPayStatus(OrderPayStatus.NOT_PAY.getCode());
                storeReceipt.setStatus(Constants.NORMAL);
                if (accountVO == null)
                {
                    log.error("客户【" + k + "】未绑定支付账号,无法生成收款单和预付流水");
                }
                else
                {

                    // 生成收款单
                    storeReceipt.setBalance(accountVO.getCash());

                    // 统一结算的商户，要取他的父商户结算
                    StoreMember storeMember = storeMemberService.selectStoreMemberById(Long.parseLong(k));
                    if ("2".equals(storeMember.getSettlementType())
                            && StringUtils.isNotEmpty(storeMember.getSuperCustomer()))
                    {
                        // 获取父商户的结算账户
                        StoreMember storeMemberQry = new StoreMember();
                        storeMemberQry.setCustomerNo(storeMember.getSuperCustomer());
                        StorePaymentAccountQueryDTO qryDto = new StorePaymentAccountQueryDTO();
                        qryDto.setMemberId(
                                storeMemberService.selectStoreMemberList(storeMemberQry).get(0).getId() + "");
                        List<StorePaymentAccountVO> parentAcct = storePaymentAccountService.selectStorePaymentAccountList(
                                qryDto);
                        if (CollectionUtils.isNotEmpty(parentAcct))
                        {
                            storeReceipt.setMerchantId(qryDto.getMemberId());
                            storeReceipt.setBalance(parentAcct.get(0).getCash());
                        }
                        else
                        {
                            log.error("客户【" + storeMember.getSuperCustomer() + "】未绑定支付账号,无法生成收款单和预付流水");
                        }
                    }
                    insertStoreReceipt(storeReceipt);
                    // 生成预付流水
                    //                    StoreAccountFlow storeAccountFlow = new StoreAccountFlow();
                    //                    storeAccountFlow.setAccountId(accountVO.getAccountId());
                    //                    storeAccountFlow.setAccountNumber(accountVO.getAccountNumber());
                    //                    storeAccountFlow.setChangeAmount(price);
                    //                    storeAccountFlow.setAccountNumber(accountVO.getAccountNumber());
                    //                    storeAccountFlow.setOldAmount(accountVO.getCash());
                    //                    storeAccountFlow.setType(AccountFlowType.ADVANCE.getCode());
                    //                    storeAccountFlow.setNowAmount(accountVO.getCash() - price);
                    //                    storeAccountFlowService.insertStoreAccountFlow(storeAccountFlow);
                    //设置原订单状态值
                    storeGoodsOrderMapper.updateOldOfStatus(orderIdList);
                    Map<String, Object> param = new HashMap<>();
                    param.put("receiptStatus", "1");
                    param.put("orderIdList", orderIdList);
                    storeGoodsOrderMapper.updateReceiptStatus(param);
                    for (StoreGoodsOrderVO o : v)
                    {
                        // 异步添加日志
                        CompletableFuture.runAsync(() -> storeGoodsOrderService.addLogNoGoods(o,
                                OrderLogType.PAY_RECEIPT.getCode(),
                                user,
                                "订单生成付款单"));
                    }
                }

            }
        }
        return 1;
    }

    /**
     * 上传凭证
     *
     * @param storeReceipt
     *            收款单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCertificate(StoreReceipt storeReceipt) {

        if (OrderPayStatus.PAYED.getCode().equals(storeReceipt.getPayStatus())) {

            // 已付款则更改订单为已完成
            storeReceipt.setPayType(ReceiptPayType.OFFLINE_PAY.getCode());
            storeReceipt.setLastModifyTime(new Date());
            SysUser loginUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            storeReceipt.setCreatorName(loginUser.getUserName());
            StoreGoodsOrderQueryDTO queryDTO = new StoreGoodsOrderQueryDTO();
            queryDTO.setOrderIdList(Arrays.asList(storeReceipt.getOrderIds().split(",")));
            List<StoreGoodsOrderVO> orderList = storeGoodsOrderMapper.selectStoreGoodsOrderList(queryDTO);
            if (CollectionUtils.isNotEmpty(orderList)) {
                SysUser user = new SysUser();
                user.setUserId("1");
                try{
                    user.setUserId(((SysUser)SecurityUtils.getSubject().getPrincipal()).getUserId());
                }catch (Exception e) {
                }
                orderList.forEach(o -> {
                    o.setStatus(OrderStatus.FINISH.getCode());
                    o.setPayStatus(OrderPayStatus.PAYED.getCode());
                    StoreGoodsOrderDTO storeGoodsOrder = new StoreGoodsOrderDTO();
                    BeanUtils.copyProperties(o, storeGoodsOrder);
                    storeGoodsOrderMapper.updateStoreGoodsOrder(storeGoodsOrder);
                    //storeGoodsOrderService.updateStoreGoodsOrder(storeGoodsOrder);
                    // 异步添加日志
                    CompletableFuture.runAsync(
                            () -> storeGoodsOrderService.addLogNoGoods(o, OrderLogType.COMPLETE.getCode(), user, "订单已完成"));
                });
            }

            // 生成支付流水
            StoreAccountFlow storeAccountFlow = new StoreAccountFlow();
            StoreReceipt nowStoreReceipt = selectStoreReceiptById(storeReceipt.getReceiptId());
            StorePaymentAccountQueryDTO queryAccount = new StorePaymentAccountQueryDTO();
            queryAccount.setMemberId(nowStoreReceipt.getMerchantId());
            List<StorePaymentAccountVO> accList =
                    storePaymentAccountService.selectStorePaymentAccountList(queryAccount);
            if (CollectionUtils.isNotEmpty(accList)) {
                storeAccountFlow.setAccountId(accList.get(0).getAccountId());
                storeAccountFlow.setAccountNumber(accList.get(0).getAccountNumber());
                storeAccountFlow.setChangeAmount(nowStoreReceipt.getAmount());
                storeAccountFlow.setOldAmount(accList.get(0).getCash());
                storeAccountFlow.setType(AccountFlowType.PAY.getCode());
                storeAccountFlow.setNowAmount(accList.get(0).getCash());
                storeAccountFlowService.insertStoreAccountFlow(storeAccountFlow);

                storeBlackWhiteService.updateBlackOrWhiteStatus(accList.get(0));

            } else {
                log.error("客户" + nowStoreReceipt.getMerchantId() + "未绑定账户,无法生成支付流水");
            }

        }

        return storeReceiptMapper.updateStoreReceipt(storeReceipt);
    }

    /**
     * 上传凭证
     *
     * @param storeReceipt
     *            收款单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delCertificate(StoreReceipt storeReceipt) {

        StoreReceipt nowStoreReceipt = selectStoreReceiptById(storeReceipt.getReceiptId());
        // 已付款则更改订单为已完成
        storeReceipt.setPayStatus(OrderPayStatus.NOT_PAY.getCode());
        storeReceipt.setLastModifyTime(new Date());

        StoreGoodsOrderQueryDTO queryDTO = new StoreGoodsOrderQueryDTO();
        queryDTO.setOrderIdList(Arrays.asList(nowStoreReceipt.getOrderIds().split(",")));
        List<StoreGoodsOrderVO> orderList = storeGoodsOrderMapper.selectStoreGoodsOrderList(queryDTO);
        if (CollectionUtils.isNotEmpty(orderList)) {
            SysUser user = new SysUser();
            user.setUserId("1");
            try{
                user.setUserId(((SysUser)SecurityUtils.getSubject().getPrincipal()).getUserId());
            }catch (Exception e) {
            }
            orderList.forEach(o -> {
                o.setStatus(OrderStatus.SENDED.getCode());
                o.setPayStatus(OrderPayStatus.NOT_PAY.getCode());
                StoreGoodsOrderDTO storeGoodsOrder = new StoreGoodsOrderDTO();
                BeanUtils.copyProperties(o, storeGoodsOrder);
                storeGoodsOrderMapper.updateStoreGoodsOrder(storeGoodsOrder);
                // 异步添加日志
                CompletableFuture.runAsync(
                        () -> storeGoodsOrderService.addLogNoGoods(o, OrderLogType.CANCEL_CERTIFICAT.getCode(), user, "删除凭证，订单完成状态变为分拣状态"));
            });
        }

        // 生成支付流水
        StoreAccountFlow storeAccountFlow = new StoreAccountFlow();

        StorePaymentAccountQueryDTO queryAccount = new StorePaymentAccountQueryDTO();
        queryAccount.setMemberId(nowStoreReceipt.getMerchantId());
        List<StorePaymentAccountVO> accList =
                storePaymentAccountService.selectStorePaymentAccountList(queryAccount);
        if (CollectionUtils.isNotEmpty(accList)) {
            storeAccountFlow.setAccountId(accList.get(0).getAccountId());
            storeAccountFlow.setAccountNumber(accList.get(0).getAccountNumber());
            storeAccountFlow.setChangeAmount(nowStoreReceipt.getAmount());
            storeAccountFlow.setOldAmount(accList.get(0).getCash());
            storeAccountFlow.setType(AccountFlowType.CANCEL_CERTIFICAT.getCode());
            storeAccountFlow.setNowAmount(accList.get(0).getCash());
            storeAccountFlowService.insertStoreAccountFlow(storeAccountFlow);

            storeBlackWhiteService.updateBlackOrWhiteStatus(accList.get(0));

        } else {
            log.error("客户" + nowStoreReceipt.getMerchantId() + "未绑定账户,无法生成支付流水");
        }


        return storeReceiptMapper.updateCertificate(storeReceipt);
    }

    /**
     * 修改收款单
     *
     * @param storeReceipt
     *            收款单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStoreReceipt(StoreReceipt storeReceipt) {

        if (OrderPayStatus.PAYED.getCode().equals(storeReceipt.getPayStatus())) {

            // 已付款则更改订单为已完成
            StoreGoodsOrderQueryDTO queryDTO = new StoreGoodsOrderQueryDTO();
            queryDTO.setOrderIdList(Arrays.asList(storeReceipt.getOrderIds().split(",")));
            List<StoreGoodsOrderVO> orderList = storeGoodsOrderMapper.selectStoreGoodsOrderList(queryDTO);
            if (CollectionUtils.isNotEmpty(orderList)) {
                SysUser user = new SysUser();
                user.setUserId("1");
                try{
                    user.setUserId(((SysUser)SecurityUtils.getSubject().getPrincipal()).getUserId());
                }catch (Exception e) {
                }
                orderList.forEach(o -> {
                    o.setStatus(OrderStatus.FINISH.getCode());
                    o.setPayStatus(OrderPayStatus.PAYED.getCode());
                    StoreGoodsOrderDTO storeGoodsOrder = new StoreGoodsOrderDTO();
                    BeanUtils.copyProperties(o, storeGoodsOrder);
                    storeGoodsOrderMapper.updateStoreGoodsOrder(storeGoodsOrder);
                    //storeGoodsOrderService.updateStoreGoodsOrder(storeGoodsOrder);
                    // 异步添加日志
                    CompletableFuture.runAsync(
                            () -> storeGoodsOrderService.addLogNoGoods(o, OrderLogType.COMPLETE.getCode(), user, "订单已完成"));
                });
            }

            // 生成支付流水
            StoreAccountFlow storeAccountFlow = new StoreAccountFlow();
            StoreReceipt nowStoreReceipt = selectStoreReceiptById(storeReceipt.getReceiptId());
            StorePaymentAccountQueryDTO queryAccount = new StorePaymentAccountQueryDTO();
            queryAccount.setMemberId(nowStoreReceipt.getMerchantId());
            List<StorePaymentAccountVO> accList =
                    storePaymentAccountService.selectStorePaymentAccountList(queryAccount);
            if (CollectionUtils.isNotEmpty(accList)) {
                storeAccountFlow.setAccountId(accList.get(0).getAccountId());
                storeAccountFlow.setAccountNumber(accList.get(0).getAccountNumber());
                storeAccountFlow.setChangeAmount(nowStoreReceipt.getAmount());
                storeAccountFlow.setOldAmount(accList.get(0).getCash());
                storeAccountFlow.setType(AccountFlowType.PAY.getCode());
                if(ReceiptPayType.CMB_PAY.getCode().equals(storeReceipt.getPayType())) {
                    storeAccountFlow.setNowAmount(accList.get(0).getCash());
                } else {
                    storeAccountFlow.setNowAmount(accList.get(0).getCash() - nowStoreReceipt.getAmount());
                }
                storeAccountFlowService.insertStoreAccountFlow(storeAccountFlow);
                // 扣除账户金额
                if(!ReceiptPayType.CMB_PAY.getCode().equals(storeReceipt.getPayType())) {
                    if(storeAccountFlow.getNowAmount() < 0) {
                        throw new BusinessException("账户余额不足，请充值！");
                    }

                    storePaymentAccountService.updateStorePaymentAccountCash(accList.get(0).getAccountId(),
                            storeAccountFlow.getNowAmount(),
                            accList.get(0).getCash());
                }
            } else {
                log.error("客户" + nowStoreReceipt.getMerchantId() + "未绑定账户,无法生成支付流水");
            }

        }
        if(ReceiptStatus.CANCEL.getCode().equals(storeReceipt.getStatus())){
            //已作废还原订单状态
            StoreReceipt  oldStoreReceipt=selectStoreReceiptById(storeReceipt.getReceiptId());

            int result=storeGoodsOrderService.updateStatusOfOld(Arrays.asList(oldStoreReceipt.getOrderIds().split(",")));
            if(result==0){
                log.error("还原" + oldStoreReceipt.getOrderIds() + "订单状态失败！");
            }
        }
        return storeReceiptMapper.updateStoreReceipt(storeReceipt);
    }

    /**
     * 删除收款单对象
     *
     * @param ids
     *            需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreReceiptByIds(String ids) {
        for(String id : ids.split(",")) {
            // 判断是否存在正在支付的付款单
            StorePaymentFlow storePaymentFlow = new StorePaymentFlow();
            storePaymentFlow.setReceiptId(Long.parseLong(id));
            storePaymentFlow.setStatus(Constants.NORMAL);
            if(CollectionUtils.isNotEmpty(storePaymentFlowMapper.selectStorePaymentFlowList(storePaymentFlow))) {
                throw new BusinessException("付款单【" + id + "】正在支付中，不能删除！");
            }

            StoreReceipt storeReceipt = storeReceiptMapper.selectStoreReceiptById(Long.parseLong(id));
            Map<String, Object> param = new HashMap<>();
            param.put("receiptStatus","0");
            param.put("financialAuditStatus","0");
            param.put("synStatus","0");
            param.put("orderIdList",Arrays.asList(storeReceipt.getOrderIds().split(",")));
            storeGoodsOrderMapper.updateReceiptStatus(param);
        }
        //设置原订单状态值
        storeGoodsOrderMapper.updateOldOfStatus(Arrays.asList(ids.split(",")));
        return storeReceiptMapper.deleteStoreReceiptByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除收款单信息
     *
     * @param receiptId
     *            收款单ID
     * @return 结果
     */
    @Override
    public int deleteStoreReceiptById(Long receiptId) {
        return storeReceiptMapper.deleteStoreReceiptById(receiptId);
    }
}
