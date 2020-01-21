package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-15 16:14
 * @Description
 **/
public class CinemaInfoVO implements Serializable {
    private static final long serialVersionUID = 7158058319077621068L;

    private Integer cinemaId;
    private String imgUrl;
    private String cinemaName;
    private String cinemaAdress;
    private String cinemaPhone;

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinemaAdress() {
        return cinemaAdress;
    }

    public void setCinemaAdress(String cinemaAdress) {
        this.cinemaAdress = cinemaAdress;
    }

    public String getCinemaPhone() {
        return cinemaPhone;
    }

    public void setCinemaPhone(String cinemaPhone) {
        this.cinemaPhone = cinemaPhone;
    }
}