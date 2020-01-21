package com.stylefeng.guns.api.film.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-14 16:07
 * @Description
 **/
public class ActorRequestVO implements Serializable {

    private static final long serialVersionUID = 6903917170903498321L;
    private ActorVO director;
    private List<ActorVO> actors;

    public ActorVO getDirector() {
        return director;
    }

    public void setDirector(ActorVO director) {
        this.director = director;
    }

    public List<ActorVO> getActors() {
        return actors;
    }

    public void setActors(List<ActorVO> actors) {
        this.actors = actors;
    }
}