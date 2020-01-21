package com.stylefeng.guns.api.film.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-13 11:03
 * @Description
 **/
public class CatVO implements Serializable {

    private static final long serialVersionUID = -6922987844675263296L;

    private String catId;
    private String catName;
    private Boolean isActive;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}