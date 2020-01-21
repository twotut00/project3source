package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-15 18:02
 * @Description
 **/
public class CinemaResponseVO  implements Serializable {

    private static final long serialVersionUID = 5115262138603453612L;
    private Integer nowPage;
    private Integer totalPage;
    private List<CinemaVO> data;

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<CinemaVO> getData() {
        return data;
    }

    public void setData(List<CinemaVO> data) {
        this.data = data;
    }
}