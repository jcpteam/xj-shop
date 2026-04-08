package com.javaboot.common.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 权限通用常量
 *
 * @author javaboot
 */
public class PermissionConstants {
    /**
     * 新增权限
     */
    public static final String ADD_PERMISSION = "add" ;

    /**
     * 修改权限
     */
    public static final String EDIT_PERMISSION = "edit" ;

    /**
     * 删除权限
     */
    public static final String REMOVE_PERMISSION = "remove" ;

    /**
     * 导出权限
     */
    public static final String EXPORT_PERMISSION = "export" ;

    /**
     * 显示权限
     */
    public static final String VIEW_PERMISSION = "view" ;

    /**
     * 查询权限
     */
    public static final String LIST_PERMISSION = "list" ;

    /**
     * 查询权限
     */
    public static final List<String> ORDER_PASSWORD_ROLE_LIST = Arrays.asList(new String[]{"areamanager","sysadmin","admin" });

    public static final String EXTERNAL_SALER = "externalsaler";
}
