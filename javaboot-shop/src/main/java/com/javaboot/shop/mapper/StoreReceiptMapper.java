package com.javaboot.shop.mapper;

import com.javaboot.shop.domain.StoreReceipt;
import com.javaboot.shop.dto.StoreReceiptQueryDTO;
import com.javaboot.shop.vo.StoreReceiptVO;

import java.util.List;

/**
 * 收款单Mapper接口
 *
 * @author lqh
 * @date 2021-07-05
 */
public interface StoreReceiptMapper {
    /**
     * 查询收款单
     *
     * @param receiptId 收款单ID
     * @return 收款单
     */
    public StoreReceipt selectStoreReceiptById(Long receiptId);

    /**
     * 查询收款单列表
     *
     * @param storeReceipt 收款单
     * @return 收款单集合
     */
    public List<StoreReceiptVO> selectStoreReceiptList(StoreReceiptQueryDTO storeReceipt);

    /**
     * 查询收款单列表
     *
     * @param storeReceipt 收款单
     * @return 收款单集合
     */
    public List<StoreReceiptVO> selectExportStoreReceiptList(StoreReceiptQueryDTO storeReceipt);


    /**
     * 新增收款单
     *
     * @param storeReceipt 收款单
     * @return 结果
     */
    public int insertStoreReceipt(StoreReceipt storeReceipt);

    /**
     * 修改收款单
     *
     * @param storeReceipt 收款单
     * @return 结果
     */
    public int updateStoreReceipt(StoreReceipt storeReceipt);

    public int updateCertificate(StoreReceipt storeReceipt);

    /**
     * 删除收款单
     *
     * @param receiptId 收款单ID
     * @return 结果
     */
    public int deleteStoreReceiptById(Long receiptId);

    /**
     * 批量删除收款单
     *
     * @param receiptIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteStoreReceiptByIds(String[] receiptIds);
}
