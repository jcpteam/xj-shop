/*
 * Copyright (c) 2016-2019 http://www.javaboot.cn. All rights reserved.
 * This file is part of the javaboot development framework.
 * Do not make the source code available without the author's permission.
 * This copyright must be retained when used in commercial products.
 * If you have any questions, please email admin@javaboot.cn
 */

package com.javaboot.common.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * layui数据表格封装类
 * @date: 2019/11/27 21:11
 * @author: javaboot
 * @version: 1.0
 */
public class DataTable implements Serializable {
    /**状态码*/
    private int code;
    /**回执消息*/
    private String msg;
    /**数据总数*/
    private long count;
    /**表格数据*/
    private List data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
