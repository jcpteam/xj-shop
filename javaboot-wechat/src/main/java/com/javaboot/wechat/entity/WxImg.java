package com.javaboot.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaboot.framework.config.typehandler.ArrayLongTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;

/**
 * 微信图片
 *
 * @author javaboot.cn
 * @version 1.0.0
 * @date 2020/10/16 10:12
 */
@Data
@TableName("wx_material_img")
public class WxImg extends Model<WxUser> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 图片真实名称
     */
    private String realName;
    /**
     * 微信服务器返回media_id
     */
    private String mediaId;
    /**
     * 图片类型
     */
    private String type;
    /**
     * 图层存储路径
     */
    private String path;
    /**
     * 微信服务器返回URL
     */
    private String url;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 公众号AppId
     */
    private String appid;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
