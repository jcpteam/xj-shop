package com.javaboot.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 微信素材
 *
 * @author javaboot.cn
 * @version 1.0.0
 * @date 2020/10/16 10:12
 */
@Data
@TableName("wx_material")
public class WxMaterial extends Model<WxUser> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 文件真实名称
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long fileLength;
    /**
     * 文件存储路径
     */
    private String filePath;
    /**
     * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     */
    private String type;
    /**
     * 新增的永久素材的media_id
     */
    private String mediaId;
    /**
     * 新增的图片素材的图片URL（仅新增图片素材时会返回该字段）
     */
    private String url;

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
