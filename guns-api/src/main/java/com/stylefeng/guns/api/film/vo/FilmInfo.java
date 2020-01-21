package com.stylefeng.guns.api.film.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-12 12:08
 * @Description
 **/
public class FilmInfo implements Serializable {
    private static final long serialVersionUID = -9068611426026315811L;

    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
    private Integer expectNum;
    private String showTime;

    private String filmCats;
    private String filmLength;

    private Integer boxNum;
    private String score;

    public Integer getExpectNum() {
        return expectNum;
    }

    public void setExpectNum(Integer expectNum) {
        this.expectNum = expectNum;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public Integer getFilmType() {
        return filmType;
    }

    public String getFilmCats() {
        return filmCats;
    }

    public void setFilmCats(String filmCats) {
        this.filmCats = filmCats;
    }

    public String getFilmLength() {
        return filmLength;
    }

    public void setFilmLength(String filmLength) {
        this.filmLength = filmLength;
    }

    public void setFilmType(Integer filmType) {
        this.filmType = filmType;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getFilmScore() {
        return filmScore;
    }

    public void setFilmScore(String filmScore) {
        this.filmScore = filmScore;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}