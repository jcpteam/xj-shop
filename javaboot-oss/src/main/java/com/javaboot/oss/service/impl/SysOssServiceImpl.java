package com.javaboot.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.oss.domain.SysOss;
import com.javaboot.oss.mapper.SysOssMapper;
import com.javaboot.oss.service.ISysOssService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sysOssService")
public class SysOssServiceImpl implements ISysOssService {
    @Autowired
    private SysOssMapper sysOssMapper;

    @Override
    public List<SysOss> getList(SysOss sysOss) {
        QueryWrapper<SysOss> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(sysOss.getFileName())) {
            queryWrapper.lambda().like(SysOss::getFileName, "%" + sysOss.getFileName() + "%");
        }
        if (StringUtils.isNotBlank(sysOss.getFileSuffix())) {
            queryWrapper.lambda().eq(SysOss::getFileSuffix, sysOss.getFileSuffix());
        }
        if (StringUtils.isNotBlank(sysOss.getCreateBy())) {
            queryWrapper.lambda().like(SysOss::getCreateBy, sysOss.getCreateBy());
        }
        return sysOssMapper.selectList(queryWrapper);
    }

    @Override
    public int save(SysOss ossEntity) {
        return sysOssMapper.insert(ossEntity);
    }

    @Override
    public SysOss findById(Long ossId) {
        return sysOssMapper.selectById(ossId);
    }

    @Override
    public int update(SysOss sysOss) {
        return sysOssMapper.updateById(sysOss);
    }

    @Override
    public int deleteByIds(String ids) {
        return sysOssMapper.deleteById(ids);
    }
}
