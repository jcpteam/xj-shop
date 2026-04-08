package com.javaboot.shop.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.javaboot.common.annotation.Excel;
import com.javaboot.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 消息对象 store_message
 * 
 * @author javaboot
 * @date 2021-11-25
 */
public class StoreMessage extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    private Long id;

    /** 消息key */
    @Excel(name = "消息key")
    private String messageKey;

    /** 状态：0-删除，1-正常 */
    @Excel(name = "状态：0-删除，1-正常")
    private String status;

    /** 状态：0-未读，1-已读 */
    @Excel(name = "状态：0-未读，1-已读")
    private String readStatus;

    /** 消息标题 */
    @Excel(name = "消息标题")
    private String title;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String content;

    /** 客户id */
    @Excel(name = "客户id")
    private String merchantId;

    /** 接收者用户id */
    @Excel(name = "接收者用户id")
    private String receiveId;

    /** 最后更新时间 */
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
    public void setMessageKey(String messageKey){
        this.messageKey = messageKey;
    }

    public String getMessageKey(){
        return messageKey;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    public void setReadStatus(String readStatus){
        this.readStatus = readStatus;
    }

    public String getReadStatus(){
        return readStatus;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }
    public void setMerchantId(String merchantId){
        this.merchantId = merchantId;
    }

    public String getMerchantId(){
        return merchantId;
    }
    public void setReceiveId(String receiveId){
        this.receiveId = receiveId;
    }

    public String getReceiveId(){
        return receiveId;
    }
    public void setLastModifyTime(Date lastModifyTime){
        this.lastModifyTime = lastModifyTime;
    }

    public Date getLastModifyTime(){
        return lastModifyTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("messageKey", getMessageKey())
            .append("status", getStatus())
            .append("readStatus", getReadStatus())
            .append("title", getTitle())
            .append("content", getContent())
            .append("merchantId", getMerchantId())
            .append("receiveId", getReceiveId())
            .append("createTime", getCreateTime())
            .append("lastModifyTime", getLastModifyTime())
            .toString();
    }
}
