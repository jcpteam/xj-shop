package com.javaboot.oss.service;

import com.javaboot.oss.domain.SysOss;

import java.util.List;

/**
 * 文件上传
 */
public interface ISysOssService {
    /**
     * 列表查询方法
     *
     * @param sysOss
     * @return
     */
    List<SysOss> getList(SysOss sysOss);

    /**
     * @param ossEntity
     */
    int save(SysOss ossEntity);

    /**
     * @param ossId
     * @return
     */
    SysOss findById(Long ossId);

    /**
     * @param sysOss
     * @return
     */
    int update(SysOss sysOss);

    /**
     * @param ids
     * @return
     */
    int deleteByIds(String ids);
}
