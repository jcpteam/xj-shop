package com.javaboot.shop.task;

import com.javaboot.common.constant.Constants;
import com.javaboot.common.enums.BlackWhiteType;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreBlackWhite;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.domain.StoreOrderPassword;
import com.javaboot.shop.domain.StorePaymentAccount;
import com.javaboot.shop.dto.StoreGoodsOrderQueryDTO;
import com.javaboot.shop.dto.StorePaymentAccountQueryDTO;
import com.javaboot.shop.dto.StoreReceiptQueryDTO;
import com.javaboot.shop.mapper.*;
import com.javaboot.shop.service.IStoreBlackWhiteService;
import com.javaboot.shop.service.IStoreOrderPasswordService;
import com.javaboot.shop.service.IStoreSaleSettingService;
import com.javaboot.shop.vo.StoreBlackWhiteVO;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.shop.vo.StorePaymentAccountVO;
import com.javaboot.shop.vo.StoreReceiptVO;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.vo.SysDeptSelectVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  上数定时任务
 */
@Component("whiteBlackTask")
public class WhiteBlackTask
{

    @Autowired
    private StorePaymentAccountMapper storePaymentAccountMapper;

    @Autowired
    private IStoreBlackWhiteService storeBlackWhiteService;

    /**
     * 定时生成订单密码
     */
    public void initWhiteBlack(){
        // 获取所有的支付账户
        StorePaymentAccountQueryDTO dto = new StorePaymentAccountQueryDTO();
        dto.setStatus(Constants.NORMAL);
        List<StorePaymentAccountVO> list = storePaymentAccountMapper.selectStorePaymentAccountList(dto);
        if(CollectionUtils.isNotEmpty(list)) {
            for (StorePaymentAccountVO vo: list) {

                storeBlackWhiteService.updateBlackOrWhiteStatus(vo);

            }
        }

    }

}
