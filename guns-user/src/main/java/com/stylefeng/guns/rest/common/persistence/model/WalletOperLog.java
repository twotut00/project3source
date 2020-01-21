package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ciggar
 * @since 2019-08-05
 */
@TableName("wallet_oper_log")
public class WalletOperLog extends Model<WalletOperLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 流水编号
     */
    @TableField("log_no")
    private String logNo;
    /**
     * 钱包操作类型 1扣费 2加钱
     */
    @TableField("wallet_oper_type")
    private Integer walletOperType;
    /**
     * 外部订单编号
     */
    @TableField("out_order_no")
    private String outOrderNo;
    /**
     * 请求数量
     */
    @TableField("req_amount")
    private BigDecimal reqAmount;
    /**
     * 操作备注
     */
    private String remarks;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogNo() {
        return logNo;
    }

    public void setLogNo(String logNo) {
        this.logNo = logNo;
    }

    public Integer getWalletOperType() {
        return walletOperType;
    }

    public void setWalletOperType(Integer walletOperType) {
        this.walletOperType = walletOperType;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public BigDecimal getReqAmount() {
        return reqAmount;
    }

    public void setReqAmount(BigDecimal reqAmount) {
        this.reqAmount = reqAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "WalletOperLog{" +
        "id=" + id +
        ", userId=" + userId +
        ", logNo=" + logNo +
        ", walletOperType=" + walletOperType +
        ", outOrderNo=" + outOrderNo +
        ", reqAmount=" + reqAmount +
        ", remarks=" + remarks +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
