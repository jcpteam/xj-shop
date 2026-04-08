package com.javaboot.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 公众号账户
 *
 * @author javaboot.cn
 * @version 1.0.0
 * @date 2020/9/25 12:28
 */
@Data
@TableName("wx_account")
@EqualsAndHashCode(callSuper = true)
public class WxAccount extends Model<WxAccount> {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 公众号名称
     */
    private String name;

    /**
     * 公众号类型(1-服务号, 2-订阅号)
     */
    private Integer type;

    /**
     * appid
     */
    private String appid;

    /**
     * secret
     */
    private String secret;

    /**
     * token
     */
    private String token;

    /**
     * aeskey
     */
    private String aeskey;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
