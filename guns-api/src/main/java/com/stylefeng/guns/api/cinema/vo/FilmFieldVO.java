package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-15 16:18
 * @Description
 **/
public class FilmFieldVO implements Serializable {

    private static final long serialVersionUID = -6201125514161434833L;

    private Integer fieldId;
    private String beginTime;
    private String endTime;
    private String language;
    private String hallName;
    private String price;


    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}