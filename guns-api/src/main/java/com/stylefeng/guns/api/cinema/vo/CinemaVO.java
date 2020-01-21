package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-15 16:03
 * @Description
 **/
public class CinemaVO implements Serializable {

    private static final long serialVersionUID = -3422083022394776801L;
    private Integer uuid;
    private String cinemaName;
    private String cinemaAddress;
    private Integer minimumPrice;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinemaAddress() {
        return cinemaAddress;
    }

    public void setCinemaAddress(String cinemaAddress) {
        this.cinemaAddress = cinemaAddress;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Integer minimumPrice) {
        this.minimumPrice = minimumPrice;
    }
}