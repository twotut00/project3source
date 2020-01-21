package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.api.film.vo.BannerVO;
import com.stylefeng.guns.api.film.vo.FilmInfo;
import com.stylefeng.guns.api.film.vo.FilmVO;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-12 17:16
 * @Description
 **/
public class FilmIndexVO implements Serializable {
    private static final long serialVersionUID = 4803462428288523321L;

    private List<BannerVO> banners;
    private FilmVO hotFilms;
    private FilmVO soonFilms;
    private List<FilmInfo> boxRanking;
    private List<FilmInfo> expectRanking;
    private List<FilmInfo> top100;

    public List<BannerVO> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerVO> banners) {
        this.banners = banners;
    }

    public FilmVO getHotFilms() {
        return hotFilms;
    }

    public void setHotFilms(FilmVO hotFilms) {
        this.hotFilms = hotFilms;
    }

    public FilmVO getSoonFilms() {
        return soonFilms;
    }

    public void setSoonFilms(FilmVO soonFilms) {
        this.soonFilms = soonFilms;
    }

    public List<FilmInfo> getBoxRanking() {
        return boxRanking;
    }

    public void setBoxRanking(List<FilmInfo> boxRanking) {
        this.boxRanking = boxRanking;
    }

    public List<FilmInfo> getExpectRanking() {
        return expectRanking;
    }

    public void setExpectRanking(List<FilmInfo> expectRanking) {
        this.expectRanking = expectRanking;
    }

    public List<FilmInfo> getTop100() {
        return top100;
    }

    public void setTop100(List<FilmInfo> top100) {
        this.top100 = top100;
    }
}