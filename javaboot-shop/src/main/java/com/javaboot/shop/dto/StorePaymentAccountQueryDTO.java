package com.javaboot.shop.dto;

import com.javaboot.shop.domain.StorePaymentAccount;
import lombok.Data;

import java.util.List;

/**
 * @Classname PaymentAccountQueryDTO
 * @Description 支付账号查询
 * @Date 2021/7/9 0009 20:09
 * @@author lqh
 */
@Data
public class StorePaymentAccountQueryDTO extends StorePaymentAccount {
    /**
     * 关键字
     */
    private String searchKey;
    /**
     * 用户昵称
     */
    private String nickName;
    private List<String> memberIdList;
}
