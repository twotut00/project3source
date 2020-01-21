package com.stylefeng.guns.rest.common.persistence.model;

import java.math.BigDecimal;
import java.util.Date;
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
 * @since 2019-08-08
 */
@TableName("mtime_promo_order")
public class MtimePromoOrder extends Model<MtimePromoOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id 不是自增的
     */
    private String uuid;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 影院id
     */
    @TableField("cinema_id")
    private Integer cinemaId;
    /**
     * 兑换码
     */
    @TableField("exchange_code")
    private String exchangeCode;
    /**
     * 兑换开始时间
     */
    @TableField("start_time")
    private Date startTime;
    /**
     * 兑换结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 购买数量
     */
    private Integer amount;
    /**
     * 订单总金额
     */
    private BigDecimal price;
    /**
     * 订单创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public String  getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "MtimePromoOrder{" +
        "uuid=" + uuid +
        ", userId=" + userId +
        ", cinemaId=" + cinemaId +
        ", exchangeCode=" + exchangeCode +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", amount=" + amount +
        ", price=" + price +
        ", createTime=" + createTime +
        "}";
    }
}
