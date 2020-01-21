package com.stylefeng.guns.api.film.vo;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-06-14 16:10
 * @Description
 **/
public class InfoRequestVO implements Serializable {

    private static final long serialVersionUID = 6462759948137523673L;
    private  String biopgraphy;
    private ActorRequestVO actors;
    private ImgVO imgVO;
    private String filmId;

    public String getBiopgraphy() {
        return biopgraphy;
    }

    public void setBiopgraphy(String biopgraphy) {
        this.biopgraphy = biopgraphy;
    }

    public ActorRequestVO getActors() {
        return actors;
    }

    public void setActors(ActorRequestVO actors) {
        this.actors = actors;
    }

    public ImgVO getImgVO() {
        return imgVO;
    }

    public void setImgVO(ImgVO imgVO) {
        this.imgVO = imgVO;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }
}