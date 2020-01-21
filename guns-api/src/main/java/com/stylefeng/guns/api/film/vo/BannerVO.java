package com.stylefeng.guns.api.film.vo;


import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-12 11:04
 * @Description
 **/

public class BannerVO implements Serializable {

        private static final long serialVersionUID = 3183118274636846850L;

        private String bannerId;
        private String bannerAddress;
        private String bannerUrl;


    public String getBannerId() {
            return bannerId;
        }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerAddress() {
        return bannerAddress;
    }

    public void setBannerAddress(String bannerAddress) {
        this.bannerAddress = bannerAddress;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }
}