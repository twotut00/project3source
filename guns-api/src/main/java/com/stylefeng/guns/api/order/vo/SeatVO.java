package com.stylefeng.guns.api.order.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-17 14:30
 * @Description
 **/
public class SeatVO implements Serializable {

    private static final long serialVersionUID = 7324321100768648148L;
    private Integer seatId;
    private Integer row;
    private Integer column;

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }
}