package com.stylefeng.guns.api.order.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-17 14:28
 * @Description
 **/
public class SeatJsonVO implements Serializable {


    private static final long serialVersionUID = -4105572037154005745L;

    private Integer limit;
    private String  ids;
    private List<SeatVO> single;
    private List<SeatVO> couple;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<SeatVO> getSingle() {
        return single;
    }

    public void setSingle(List<SeatVO> single) {
        this.single = single;
    }

    public List<SeatVO> getCouple() {
        return couple;
    }

    public void setCouple(List<SeatVO> couple) {
        this.couple = couple;
    }
}