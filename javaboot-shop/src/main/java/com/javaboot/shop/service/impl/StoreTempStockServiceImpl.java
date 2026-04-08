package com.javaboot.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.StockCategory;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.dto.StoreWarehouseStockDTO;
import com.javaboot.shop.dto.StoreWarehouseStockQueryDTO;
import com.javaboot.shop.mapper.*;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.StoreWarehouseStockVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.domain.SysDictData;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.service.ISysDictDataService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品入库Service业务层处理
 * 
 * @author lqh
 * @date 2021-05-20
 */
@Service
public class StoreTempStockServiceImpl  extends ServiceImpl<StoreTempStockMapper, StoreTempStock>
    implements IStoreTempStockService
{

}
