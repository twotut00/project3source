package com.stylefeng.guns.api.film.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-14 15:26
 * @Description
 **/
public class FilmDescVO implements Serializable {

    private static final long serialVersionUID = -2033751083881088473L;
    private String filmId;
    private String biography;

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}