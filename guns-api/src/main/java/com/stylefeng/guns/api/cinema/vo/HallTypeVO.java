package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-15 16:10
 * @Description
 **/
public class HallTypeVO implements Serializable {
    private static final long serialVersionUID = -8854534157311119306L;

    private Integer halltypeId;
    private String halltypeName;
    private Boolean isActive;

    public Integer getHalltypeId() {
        return halltypeId;
    }

    public void setHalltypeId(Integer halltypeId) {
        this.halltypeId = halltypeId;
    }

    public String getHalltypeName() {
        return halltypeName;
    }

    public void setHalltypeName(String halltypeName) {
        this.halltypeName = halltypeName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}