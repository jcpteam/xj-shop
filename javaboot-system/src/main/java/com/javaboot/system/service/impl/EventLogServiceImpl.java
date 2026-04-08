package com.javaboot.system.service.impl;

import java.util.List;

import com.javaboot.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaboot.system.mapper.EventLogMapper;
import com.javaboot.system.domain.EventLog;
import com.javaboot.system.service.ISysEventLogService;
import com.javaboot.common.core.text.Convert;

/**
 * 事件日志Service业务层处理
 *
 * @author javaboot
 * @date 2019-12-30
 */
@Service
public class EventLogServiceImpl implements ISysEventLogService {
    @Autowired
    private EventLogMapper eventLogMapper;

    /**
     * 查询事件日志
     *
     * @param id 事件日志ID
     * @return 事件日志
     */
    @Override
    public EventLog selectEventLogById(Long id) {
        return eventLogMapper.selectEventLogById(id);
    }

    /**
     * 查询事件日志列表
     *
     * @param eventLog 事件日志
     * @return 事件日志
     */
    @Override
    public List<EventLog> selectEventLogList(EventLog eventLog) {
        return eventLogMapper.selectEventLogList(eventLog);
    }

    /**
     * 新增事件日志
     *
     * @param eventLog 事件日志
     * @return 结果
     */
    @Override
    public int insertEventLog(EventLog eventLog) {
        eventLog.setCreateTime(DateUtils.getNowDate());
        return eventLogMapper.insertEventLog(eventLog);
    }

    /**
     * 修改事件日志
     *
     * @param eventLog 事件日志
     * @return 结果
     */
    @Override
    public int updateEventLog(EventLog eventLog) {
        return eventLogMapper.updateEventLog(eventLog);
    }

    /**
     * 删除事件日志对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteEventLogByIds(String ids) {
        return eventLogMapper.deleteEventLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除事件日志信息
     *
     * @param id 事件日志ID
     * @return 结果
     */
    @Override
    public int deleteEventLogById(Long id) {
        return eventLogMapper.deleteEventLogById(id);
    }
}
