package com.javaboot.shop.task;

import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import com.javaboot.system.domain.SysUserRoleDept;
import com.javaboot.system.service.ISysUserRoleDeptService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预警定时任务
 */
@Component("earlyWarningTask")
public class EarlyWarningTask {
    private static final Logger logger = LoggerFactory.getLogger(EarlyWarningTask.class);

    @Autowired
    private IStoreGoodsOrderService storeGoodsOrderService;

    @Autowired
    private IStorePaymentAccountService storePaymentAccountService;

    @Autowired
    private IStoreMemberService storeMemberService;

    @Autowired
    private IStoreMessageService storeMessageService;

    @Autowired
    private IStoreEarlyWarningService earlyWarningService;

    @Autowired
    private IStoreSaleSettingService storeSaleSettingService;

    @Autowired
    private ISysUserRoleDeptService sysUserRoleDeptService;

    /**
     * 执行客户账期预警任务
     * 未付款 且 已分拣的订单，如果当前时间-交货日期，大于客户设置的账期天数，就给客户经理发送通知
     */
    public void exePaymentWarning() {
        logger.info("exePaymentWarning start....");
        //查询预警开关是否打开
        StoreEarlyWarning where = new StoreEarlyWarning();
        where.setWarningKey("paymentDayWarn");
        List<StoreEarlyWarning> payWarnList = earlyWarningService.selectStoreEarlyWarningList(where);
        if (payWarnList == null) {
            logger.info("exePaymentWarning end....未配置账期预警脚本开关");
            return;
        }
        if ("0".equals(payWarnList.get(0).getStatus())) {
            logger.info("exePaymentWarning end....账期预警已关闭");
            return;
        }

        //1、查询所有有未付款 且 已分拣订单 的订单列表，按照交货日期升序
        List<StoreGoodsOrderVO> unpayOrderList = storeGoodsOrderService.queryUnpayOrderList();
        if (CollectionUtils.isNotEmpty(unpayOrderList)) {
            Map<String, StoreGoodsOrderVO> merchantId2OrderMap = new HashMap<>();
            List<String> merchantIds = new ArrayList<>();
            for (StoreGoodsOrderVO order : unpayOrderList) {
                if (order.getMerchantId() != null && !merchantIds.contains(order.getMerchantId())) {
                    merchantIds.add(order.getMerchantId());
                    merchantId2OrderMap.put(order.getMerchantId(), order);
                }
            }

            //2、筛选所有客户信息
            if (CollectionUtils.isNotEmpty(merchantIds)) {
                StoreMember query = new StoreMember();
                query.setIds(merchantIds);
                List<StoreMember> memberList = storeMemberService.selectStoreMemberList(query);
                if (CollectionUtils.isNotEmpty(memberList)) {
                    memberList.forEach(m -> {
                        unpayOrderList.forEach(v -> {
                            if (v.getMerchantId().equals(m.getId().toString())) {
                                v.setMerchantName(m.getNickname());
                                v.setContactName(m.getContactName());
                                v.setContactPhone(m.getTelephone());
                                v.setOpmanagerName(m.getOpmanagerName());
                                v.setOpmanagerPhone(m.getOpmanagerPhone());
                            }
                        });
                    });
                }
            }

            //3、查询所有客户的账单周期
            List<StorePaymentAccountVO> storePaymentAccountVOList = null;
            if (CollectionUtils.isNotEmpty(merchantIds)) {
                StorePaymentAccountQueryDTO storePaymentAccountQueryDTO = new StorePaymentAccountQueryDTO();
                storePaymentAccountQueryDTO.setMemberIdList(merchantIds);
                storePaymentAccountVOList = storePaymentAccountService.selectStorePaymentAccountList(storePaymentAccountQueryDTO);
            }

            //4、对比每个客户的账单周期是否超期，超期则发送消息给客户经理
            if (CollectionUtils.isNotEmpty(storePaymentAccountVOList)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (StorePaymentAccountVO storePaymentAccountVO : storePaymentAccountVOList) {
                    if (storePaymentAccountVO.getBillDays() != null && merchantId2OrderMap.get(storePaymentAccountVO.getMemberId()) != null) {
                        long billDays = Integer.valueOf(storePaymentAccountVO.getBillDays());
                        StoreGoodsOrderVO order = merchantId2OrderMap.get(storePaymentAccountVO.getMemberId());
                        long deliveryDate = order.getDeliveryDate().getTime();
                        long curTime = System.currentTimeMillis();
                        long passTime = curTime - deliveryDate;
                        long maxTime = billDays * 24 * 60 * 60 * 1000;

//                        logger.info("exePaymentWarning--->check merchantId:" + storePaymentAccountVO.getMemberId() + " ,orderId:" + order.getOrderId()
//                                + " ,curTime:" + curTime + " ,deliveryDate:" + deliveryDate);
//                        logger.info("exePaymentWarning--->check merchantId:" + storePaymentAccountVO.getMemberId() + " ,orderId:" + order.getOrderId()
//                                + " ,passTime:" + passTime + " ,maxTime:" + maxTime);

                        String deliveryDateText = sdf.format(new Date(deliveryDate));
                        String curTimeText = sdf.format(new Date(curTime));
                        logger.info("exePaymentWarning--->check merchantId:" + storePaymentAccountVO.getMemberId() + " ,merchantName:" + order.getMerchantName()
                                + " ,orderId:" + order.getOrderId()
                                + " ,billDays:" + billDays + " ,deliveryDate:" + deliveryDateText + " ,curTime:" + curTimeText
                        );
                        if (passTime > maxTime) {
                            logger.info("exePaymentWarning--->check merchantId:" + storePaymentAccountVO.getMemberId() + " ,merchantName:" + order.getMerchantName()
                                    + " over max days....send message to managerId: " + order.getOpmanagerId());
                            if (StringUtils.isNotEmpty(storePaymentAccountVO.getMemberId()) && StringUtils.isNotEmpty(order.getOpmanagerId())) {
                                StoreMessage storeMessage = new StoreMessage();
                                storeMessage.setMerchantId(storePaymentAccountVO.getMemberId());
                                storeMessage.setMessageKey("paymentDayWarn");
                                //先查询商户是否已有记录，有记录则先删除
                                List<StoreMessage> existList = storeMessageService.selectStoreMessageList(storeMessage);
                                if (CollectionUtils.isNotEmpty(existList)) {
                                    for (StoreMessage info : existList) {
                                        StoreMessage update = new StoreMessage();
                                        update.setId(info.getId());
                                        update.setStatus("0");
                                        storeMessageService.updateStoreMessage(update);
                                    }
                                }

                                storeMessage.setTitle("账期预警");

                                long d = (passTime - maxTime) / (24 * 60 * 60 * 1000 * 1L);
                                long d1 = (passTime - maxTime) % (24 * 60 * 60 * 1000 * 1L);
                                if (d1 > 0) {
                                    d = d + 1;
                                }

                                String content = "商户【" + order.getMerchantName() + "】账期已超过" + d + "天。"
                                        + "\n商户联系人【" + order.getContactName() + " " + order.getContactPhone() + "】";
                                storeMessage.setContent(content);
                                storeMessage.setReceiveId(order.getOpmanagerId());
                                storeMessageService.insertStoreMessage(storeMessage);
                            }
                        } else {
                            logger.info("exePaymentWarning--->check merchantId:" + storePaymentAccountVO.getMemberId() + " ,merchantName:" + order.getMerchantName()
                                    + " in max days....");
                        }
                    }
                }
            }
        } else {
            logger.info("exePaymentWarning unpayOrderList is empty....");
        }
        logger.info("exePaymentWarning finish....");
    }

    /**
     * 客户未下单预警
     */
    public void excCustomerNoOrderWarn() {
        logger.info("excCustomerNoOrderWarn start....");
        //查询预警开关是否打开
        StoreEarlyWarning where = new StoreEarlyWarning();
        where.setWarningKey("customerNoOrderWarn");
        List<StoreEarlyWarning> payWarnList = earlyWarningService.selectStoreEarlyWarningList(where);
        if (payWarnList == null) {
            logger.info("excCustomerNoOrderWarn end....未配置客户未付款预警脚本开关");
            return;
        }
        if ("0".equals(payWarnList.get(0).getStatus())) {
            logger.info("excCustomerNoOrderWarn end....客户未付款预警已关闭");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1); //前一天
        String yestoday = sdf.format(ca.getTime());
        ca.add(Calendar.DATE, -1); //前两天
        String beforeYestoday = sdf.format(ca.getTime());
        logger.info("excCustomerNoOrderWarn yestoday...." + yestoday);
        logger.info("excCustomerNoOrderWarn beforeYestoday...." + beforeYestoday);
        List<StoreMember> storeMemberList = earlyWarningService.getNoOrderCustomerListForWarn(beforeYestoday, yestoday);
        if (CollectionUtils.isNotEmpty(storeMemberList)) {
            for (StoreMember storeMember : storeMemberList) {
                if (StringUtils.isNotEmpty(storeMember.getOpmanagerId())) {
                    logger.info("excCustomerNoOrderWarn--->check merchantId:" + storeMember.getId() + " ,merchantName:" + storeMember.getNickname()
                            + " yestoday no order....send message to managerId: " + storeMember.getOpmanagerId());
                    StoreMessage storeMessage = new StoreMessage();
                    storeMessage.setMerchantId(storeMember.getId() + "");
                    storeMessage.setMessageKey("customerNoOrderWarn");
                    //先查询商户是否已有记录，有记录则先删除
                    List<StoreMessage> existList = storeMessageService.selectStoreMessageList(storeMessage);
                    if (CollectionUtils.isNotEmpty(existList)) {
                        for (StoreMessage info : existList) {
                            StoreMessage update = new StoreMessage();
                            update.setId(info.getId());
                            update.setStatus("0");
                            storeMessageService.updateStoreMessage(update);
                        }
                    }

                    storeMessage.setTitle("客户未下单预警");

                    String content = "商户【" + storeMember.getNickname() + "】昨日无订货。"
                            + "\n商户联系人【" + storeMember.getContactName() + " " + storeMember.getTelephone() + "】";
                    storeMessage.setContent(content);
                    storeMessage.setReceiveId(storeMember.getOpmanagerId());
                    storeMessageService.insertStoreMessage(storeMessage);
                } else {
                    logger.info("excCustomerNoOrderWarn--->check merchantId:" + storeMember.getId() + " ,merchantName:" + storeMember.getNickname()
                            + " yestoday no order....but managerId is null, no message....");
                }
            }
        }

        logger.info("excCustomerNoOrderWarn end....");
    }

    /**
     * 库存预警
     */
    public void excStockMaxWarn() {
        logger.info("excStockMaxWarn start....");
        //查询预警开关是否打开
        StoreEarlyWarning where = new StoreEarlyWarning();
        where.setWarningKey("stockWarn");
        List<StoreEarlyWarning> payWarnList = earlyWarningService.selectStoreEarlyWarningList(where);
        if (CollectionUtils.isEmpty(payWarnList)) {
            logger.info("excStockMaxWarn end....未配置库存预警脚本开关");
            return;
        }
        if ("0".equals(payWarnList.get(0).getStatus())) {
            logger.info("excStockMaxWarn end....库存预警已关闭");
            return;
        }
        if (StringUtils.isEmpty(payWarnList.get(0).getComment())) {
            logger.info("excStockMaxWarn end....库存预警阈值未设置");
            return;
        }
        if (StringUtils.isEmpty(payWarnList.get(0).getComment())) {
            logger.info("excStockMaxWarn end....库存预警阈值未设置");
            return;
        }
        float maxPercent = Float.valueOf(payWarnList.get(0).getComment());
        if (maxPercent >= 100) {
            if (StringUtils.isEmpty(payWarnList.get(0).getComment())) {
                logger.info("excStockMaxWarn end....库存预警阈值设置等于或超过100%,设置不正确 ----->" + maxPercent);
                return;
            }
        }

        //查询当日商品上数，当某个商品库存数量低于阈值百分比则给对应区域业务经理发送消息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        String today = sdf.format(ca.getTime());
        logger.info("excStockMaxWarn day...." + today);
        StoreSaleSetting saleSetting = new StoreSaleSetting();
        saleSetting.setCreateTimeStart(today + " 00:00:00");
        saleSetting.setCreateTimeEnd(today + " 23:59:59");
        List<StoreSaleSetting> saleSettingList = storeSaleSettingService.selectStoreSaleSettingList(saleSetting);
        if (CollectionUtils.isNotEmpty(saleSettingList)) {
            Map<String, List<SysUserRoleDept>> deptId2InfoMap = new HashMap<>();
            for (StoreSaleSetting info : saleSettingList) {
                if (info.getSelfQuanintiy() != null && info.getSettingQuanintiy() != null) {
                    double belongPercent = info.getSettingQuanintiy() * 100.0f / info.getSelfQuanintiy();
                    if (belongPercent < maxPercent) {
                        //查询当前区域的业务经理，发送消息
                        List<SysUserRoleDept> sysUserRoleDeptList = null;
                        if (deptId2InfoMap.get(info.getDeptId()) != null) {
                            sysUserRoleDeptList = deptId2InfoMap.get(info.getDeptId());
                        } else {
                            SysUserRoleDept sysUserRoleDept = new SysUserRoleDept();
                            sysUserRoleDept.setRoleId(10001L);
                            sysUserRoleDept.setDeptId(info.getDeptId());
                            sysUserRoleDeptList = sysUserRoleDeptService.selectSysUserRoleDeptList(sysUserRoleDept);
                        }
                        if (CollectionUtils.isNotEmpty(sysUserRoleDeptList)) {
                            deptId2InfoMap.put(info.getDeptId(), sysUserRoleDeptList);
                            for (SysUserRoleDept userRole : sysUserRoleDeptList) {
                                logger.info("excStockMaxWarn--->check spu:" + info.getSpuNo() + " ,settingId:" + info.getSettingId()
                                        + " below stock warn....send message to managerId: " + userRole.getUserId());
                                StoreMessage storeMessage = new StoreMessage();
                                storeMessage.setMessageKey("stockWarn");
                                //先查询商户是否已有记录，有记录则先删除
//                                List<StoreMessage> existList = storeMessageService.selectStoreMessageList(storeMessage);
//                                if (CollectionUtils.isNotEmpty(existList)) {
//                                    for (StoreMessage message : existList) {
//                                        StoreMessage update = new StoreMessage();
//                                        update.setId(message.getId());
//                                        update.setStatus("0");
//                                        storeMessageService.updateStoreMessage(update);
//                                    }
//                                }

                                storeMessage.setTitle("库存预警");

                                String content = "商品spu【" + info.getSpuNo() + "】库存低于" + maxPercent + "% ，请检查库存余量。";
                                storeMessage.setContent(content);
                                storeMessage.setReceiveId(userRole.getUserId());
                                storeMessageService.insertStoreMessage(storeMessage);
                            }
                        } else {
                            logger.info("excStockMaxWarn--->check spu:" + info.getSpuNo() + " ,settingId:" + info.getSettingId()
                                    + " below stock warn....but no mamagerId,no send message");
                        }
                    }
                }
            }
        } else {
            logger.info("excStockMaxWarn 当天还未设置商品库存数量....");
        }

        logger.info("excStockMaxWarn end....");
    }

    /**
     * 超期库存预警:每天早上八点钟跑一次，如果商品昨日的结转库存大于最近三天的入库数量则提示有商品超期
     */
    public void excOverStockWarn() {
        logger.info("excOverStockWarn start....");

        logger.info("excOverStockWarn end....");
    }
}
