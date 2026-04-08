package com.javaboot.shop.vo;

import com.javaboot.shop.domain.StorePaymentAccount;
import lombok.Data;

/**
 * @Classname StorePaymentAccountVO
 * @Description 支付账号
 * @Date 2021/7/9 0009 19:59
 * @@author lqh
 */
@Data
public class StorePaymentAccountVO extends StorePaymentAccount {

    private String statusName;
    private String storeName;
    private String billDays;
}
