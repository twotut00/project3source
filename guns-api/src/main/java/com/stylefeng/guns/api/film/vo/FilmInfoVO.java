package com.stylefeng.guns.api.film.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: jia.xue
 * @create: 2019-06-17 16:29
 * @Description
 **/
public class FilmInfoVO implements Serializable {

    private static final long serialVersionUID = -724781619844051011L;
    /**
     * 主键编号
     */
    private Integer uuid;
    /**
     * 影片名称
     */
    private String filmName;
    /**
     * 片源类型: 0-2D,1-3D,2-3DIMAX,4-无
     */
    private Integer filmType;
    /**
     * 影片主图地址
     */
    private String imgAddress;
    /**
     * 影片评分
     */
    private String filmScore;
    /**
     * 影片预售数量
     */
    private Integer filmPresalenum;
    /**
     * 影片票房：每日更新，以万为单位
     */
    private Integer filmBoxOffice;
    /**
     * 影片片源，参照片源字典表
     */
    private Integer filmSource;
    /**
     * 影片分类，参照分类表,多个分类以#分割
     */
    private String filmCats;
    /**
     * 影片区域，参照区域表
     */
    private Integer filmArea;
    /**
     * 影片上映年代，参照年代表
     */
    private Integer filmDate;
    /**
     * 影片上映时间
     */
    private Date filmTime;
    /**
     * 影片状态,1-正在热映，2-即将上映，3-经典影片
     */
    private Integer filmStatus;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public Integer getFilmType() {
        return filmType;
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

    public String getFilmScore() {
        return filmScore;
    }

    public void setFilmScore(String filmScore) {
        this.filmScore = filmScore;
    }

    public Integer getFilmPresalenum() {
        return filmPresalenum;
    }

    public void setFilmPresalenum(Integer filmPresalenum) {
        this.filmPresalenum = filmPresalenum;
    }

    public Integer getFilmBoxOffice() {
        return filmBoxOffice;
    }

    public void setFilmBoxOffice(Integer filmBoxOffice) {
        this.filmBoxOffice = filmBoxOffice;
    }

    public Integer getFilmSource() {
        return filmSource;
    }

    public void setFilmSource(Integer filmSource) {
        this.filmSource = filmSource;
    }

    public String getFilmCats() {
        return filmCats;
    }

    public void setFilmCats(String filmCats) {
        this.filmCats = filmCats;
    }

    public Integer getFilmArea() {
        return filmArea;
    }

    public void setFilmArea(Integer filmArea) {
        this.filmArea = filmArea;
    }

    public Integer getFilmDate() {
        return filmDate;
    }

    public void setFilmDate(Integer filmDate) {
        this.filmDate = filmDate;
    }

    public Date getFilmTime() {
        return filmTime;
    }

    public void setFilmTime(Date filmTime) {
        this.filmTime = filmTime;
    }

    public Integer getFilmStatus() {
        return filmStatus;
    }

    public void setFilmStatus(Integer filmStatus) {
        this.filmStatus = filmStatus;
    }
}