package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 商城首页广告信息对象 store_homepage_ads
 * 
 * @author javaboot
 * @date 2021-06-05
 */
public class StoreHomepageAds extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 广告编号 */
    @Excel(name = "广告编号")
    private String adNo;

    /** 广告图片地址 */
    @Excel(name = "广告图片地址")
    private String imgUrl;

    /** 播放顺序 */
    @Excel(name = "播放顺序")
    private Integer sequenceNo;

    /** 状态：0-关闭，1-开启 */
    @Excel(name = "状态：0-关闭，1-开启")
    private String status;

    /** 创建人id */
    @Excel(name = "创建人id")
    private String creatorId;

    private String creatorName;

    /** 最后修改时间 */
    private Date lastModifyTime;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setAdNo(String adNo){
        this.adNo = adNo;
    }

    public String getAdNo(){
        return adNo;
    }
    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getImgUrl(){
        return imgUrl;
    }
    public void setSequenceNo(Integer sequenceNo){
        this.sequenceNo = sequenceNo;
    }

    public Integer getSequenceNo(){
        return sequenceNo;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    public void setCreatorId(String creatorId){
        this.creatorId = creatorId;
    }

    public String getCreatorId(){
        return creatorId;
    }
    public void setLastModifyTime(Date lastModifyTime){
        this.lastModifyTime = lastModifyTime;
    }

    public Date getLastModifyTime(){
        return lastModifyTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("adNo", getAdNo())
            .append("imgUrl", getImgUrl())
            .append("sequenceNo", getSequenceNo())
            .append("status", getStatus())
            .append("creatorId", getCreatorId())
            .append("creatorName", getCreatorName())
            .append("createTime", getCreateTime())
            .append("lastModifyTime", getLastModifyTime())
            .toString();
    }
}
