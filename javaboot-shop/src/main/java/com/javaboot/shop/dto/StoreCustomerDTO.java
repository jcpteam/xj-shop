package com.javaboot.shop.dto;

import com.javaboot.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class StoreCustomerDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long customerId;

    private String customerName;

    private String adress;

    private String phone;
}
