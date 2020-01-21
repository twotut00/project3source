package com.stylefeng.guns.api.cinema.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-15 16:11
 * @Description
 **/
public class CinemaConditionVO implements Serializable {

    private static final long serialVersionUID = 1660214916393694198L;

    private List<BrandVO> brandList;
    private List<AreaVO> areaList;
    private List<HallTypeVO> halltypeList;

    public List<BrandVO> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<BrandVO> brandList) {
        this.brandList = brandList;
    }

    public List<AreaVO> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaVO> areaList) {
        this.areaList = areaList;
    }

    public List<HallTypeVO> getHalltypeList() {
        return halltypeList;
    }

    public void setHalltypeList(List<HallTypeVO> halltypeList) {
        this.halltypeList = halltypeList;
    }
}