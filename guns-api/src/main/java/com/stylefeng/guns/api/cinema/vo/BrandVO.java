package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-15 16:05
 * @Description
 **/
public class BrandVO implements Serializable {

    private static final long serialVersionUID = 7862446041904834290L;
    private Integer brandId;
    private String brandName;
    private Boolean isActive;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}