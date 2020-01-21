package com.stylefeng.guns.api.cinema.vo;

import com.stylefeng.guns.api.film.vo.FilmInfo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-15 17:26
 * @Description 场次的详细信息
 **/
public class FieldDetailInfoVO implements Serializable {

    private static final long serialVersionUID = 8584504408656547856L;
    private FilmVO filmInfo;
    private CinemaInfoVO cinemaInfo;
    private HallInfoVO hallInfo;

    public FilmVO getFilmInfo() {
        return filmInfo;
    }

    public void setFilmInfo(FilmVO filmInfo) {
        this.filmInfo = filmInfo;
    }

    public CinemaInfoVO getCinemaInfo() {
        return cinemaInfo;
    }

    public void setCinemaInfo(CinemaInfoVO cinemaInfo) {
        this.cinemaInfo = cinemaInfo;
    }

    public HallInfoVO getHallInfo() {
        return hallInfo;
    }

    public void setHallInfo(HallInfoVO hallInfo) {
        this.hallInfo = hallInfo;
    }
}