package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-15 17:04
 * @Description
 **/
public class FieldVO implements Serializable {

    private static final long serialVersionUID = 2553119851408890916L;
    private CinemaInfoVO cinemaInfoVO;

    private List<FilmVO> filmList;

    public CinemaInfoVO getCinemaInfoVO() {
        return cinemaInfoVO;
    }

    public void setCinemaInfoVO(CinemaInfoVO cinemaInfoVO) {
        this.cinemaInfoVO = cinemaInfoVO;
    }

    public List<FilmVO> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<FilmVO> filmList) {
        this.filmList = filmList;
    }
}