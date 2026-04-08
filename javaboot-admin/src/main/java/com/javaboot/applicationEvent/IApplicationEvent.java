package com.javaboot.applicationEvent;

/**
 * 系统事件接口
 *
 * @author javaboot
 */
public interface IApplicationEvent {
    /**
     * 当事件被触发时
     *
     * @param source
     * @param params
     */
    void onTrigger(Object source, Object params);
}
