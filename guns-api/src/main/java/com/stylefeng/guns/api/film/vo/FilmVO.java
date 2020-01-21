package com.stylefeng.guns.api.film.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-12 12:06
 * @Description
 **/
public class FilmVO implements Serializable {
    private static final long serialVersionUID = 6720869842697536645L;

    private Integer filmNum;

    private Integer totalPage;

    private Integer nowPage;

    private List<FilmInfo> filmInfo;


    public Integer getFilmNum() {
        return filmNum;
    }

    public void setFilmNum(Integer filmNum) {
        this.filmNum = filmNum;
    }

    public List<FilmInfo> getFilmInfo() {
        return filmInfo;
    }

    public void setFilmInfo(List<FilmInfo> filmInfo) {
        this.filmInfo = filmInfo;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }
}