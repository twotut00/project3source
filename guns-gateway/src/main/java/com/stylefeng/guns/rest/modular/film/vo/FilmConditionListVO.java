package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.api.film.vo.CatVO;
import com.stylefeng.guns.api.film.vo.SourceVO;
import com.stylefeng.guns.api.film.vo.YearVO;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-13 11:24
 * @Description
 **/
public class FilmConditionListVO implements Serializable {
    private static final long serialVersionUID = -4850435102161980204L;

    private List<CatVO>  catInfo;
    private List<SourceVO> sourceInfo;
    private List<YearVO> yearInfo;

    public List<CatVO> getCatInfo() {
        return catInfo;
    }

    public void setCatInfo(List<CatVO> catInfo) {
        this.catInfo = catInfo;
    }

    public List<SourceVO> getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(List<SourceVO> sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public List<YearVO> getYearInfo() {
        return yearInfo;
    }

    public void setYearInfo(List<YearVO> yearInfo) {
        this.yearInfo = yearInfo;
    }
}