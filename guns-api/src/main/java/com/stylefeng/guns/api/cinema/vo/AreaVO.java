package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 * @author: jia.xue
 * @create: 2019-06-15 16:06
 * @Description
 **/
public class AreaVO implements Serializable {

    private static final long serialVersionUID = 5835409968512935409L;
    private Integer areaId;
    private String areaName;
    private Boolean isActive;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}