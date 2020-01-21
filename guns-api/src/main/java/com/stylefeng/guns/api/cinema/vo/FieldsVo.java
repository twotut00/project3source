package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-07-12 23:08
 * @Description
 **/
public class FieldsVo implements Serializable {
    private static final long serialVersionUID = -6840271051129762914L;

    private CinemaInfoVO cinemaInfo;
    private List<FilmVO> filmList;

    public CinemaInfoVO getCinemaInfo() {
        return cinemaInfo;
    }

    public void setCinemaInfo(CinemaInfoVO cinemaInfo) {
        this.cinemaInfo = cinemaInfo;
    }

    public List<FilmVO> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<FilmVO> filmList) {
        this.filmList = filmList;
    }
}