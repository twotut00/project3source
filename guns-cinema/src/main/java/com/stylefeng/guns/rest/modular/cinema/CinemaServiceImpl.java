package com.stylefeng.guns.rest.modular.cinema;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-15 17:29
 * @Description 影院服务Service
 **/
@Component
@Service(interfaceClass = CinemaServiceApi.class)
public class CinemaServiceImpl implements CinemaServiceApi {

    @Autowired
    private MtimeAreaDictTMapper areaMapper;
    @Autowired
    private MtimeBrandDictTMapper brandMapper;
    @Autowired
    private MtimeHallDictTMapper hallMapper;
    @Autowired
    private MtimeCinemaTMapper cinemaTMapper;
    @Autowired
    private MtimeFieldTMapper fieldTMapper;
    @Autowired
    private MtimeHallFilmInfoTMapper hallFilmInfoTMapper;
    @Override
    public CinemaResponseVO getCinemaList(CinemaRequestVO cinemaRequestVO) {

        EntityWrapper<MtimeCinemaT> entityWrapper = new EntityWrapper<>();
        Boolean flag = false;
        if (cinemaRequestVO.getBrandId() != 99){
            entityWrapper.eq("brand_id",cinemaRequestVO.getBrandId());
            flag = Boolean.TRUE;
        }
        if (cinemaRequestVO.getHallType() != 99) {
            String type = "%#"+cinemaRequestVO.getHallType()+"#%";
            entityWrapper.like("hall_ids",type);
            flag = Boolean.TRUE;
        }
        if (cinemaRequestVO.getDistrictId() != 99) {
            entityWrapper.eq("area_id",cinemaRequestVO.getDistrictId());
            flag = Boolean.TRUE;
        }
        Page<MtimeCinemaT> page = new Page<>(cinemaRequestVO.getNowPage(),cinemaRequestVO.getPageSize());

        List<MtimeCinemaT> cinemaList = cinemaTMapper.selectPage(page, flag ? entityWrapper : null);
        List<CinemaVO> cinemaVOS = convertToCinemaVo(cinemaList);
        Integer pageSize = cinemaRequestVO.getPageSize();
        int totalCounts = cinemaTMapper.selectCount(flag ? entityWrapper : null);
        Integer totalPage = (totalCounts%pageSize == 0) ? (totalCounts/pageSize) : (totalCounts/pageSize + 1);
        CinemaResponseVO cinemaResponseVO = new CinemaResponseVO();

        cinemaResponseVO.setData(cinemaVOS);
        cinemaResponseVO.setNowPage(cinemaRequestVO.getNowPage());
        cinemaResponseVO.setTotalPage(totalPage);

        return cinemaResponseVO;



    }
    private List<CinemaVO> convertToCinemaVo(List<MtimeCinemaT> cinemaList) {
        List<CinemaVO> cinemaVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(cinemaList)) {
            return cinemaVOS;
        }
        for (MtimeCinemaT cinema : cinemaList) {
            CinemaVO vo = new CinemaVO();
            vo.setUuid(cinema.getUuid());
            vo.setCinemaAddress(cinema.getCinemaAddress());
            vo.setCinemaName(cinema.getCinemaName());
            vo.setMinimumPrice(cinema.getMinimumPrice());
            cinemaVOS.add(vo);
        }
        return cinemaVOS;
    }

    @Override
    public List<BrandVO> getBrandList(Integer brandId) {

        List<MtimeBrandDictT> brandList = brandMapper.selectList(null);
        if (CollectionUtils.isEmpty(brandList)) {
            return new ArrayList<BrandVO>();
        }
        List<BrandVO> vos = new ArrayList<>();
        for (MtimeBrandDictT brand : brandList) {
            BrandVO vo = new BrandVO();
            vo.setBrandId(brand.getUuid());
            vo.setBrandName(brand.getShowName());
            if (brandId == brand.getUuid()) {
                vo.setActive(true);
            }else {
                vo.setActive(false);
            }
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<AreaVO> getAreaList(Integer areaId) {
        List<MtimeAreaDictT> areaDictTList = areaMapper.selectList(null);
        if (CollectionUtils.isEmpty(areaDictTList)) {
            return new ArrayList<AreaVO>();
        }
        List<AreaVO> vos = new ArrayList<>();
        for (MtimeAreaDictT area : areaDictTList) {
            AreaVO vo = new AreaVO();
            vo.setAreaId(area.getUuid());
            vo.setAreaName(area.getShowName());
            if (areaId == area.getUuid()) {
                vo.setActive(true);
            }else {
                vo.setActive(false);
            }
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<HallTypeVO> getHalltypeList(Integer hallTypeId) {
        List<MtimeHallDictT> hallList = hallMapper.selectList(null);
        if (CollectionUtils.isEmpty(hallList)) {
            return new ArrayList<HallTypeVO>();
        }
        List<HallTypeVO> vos = new ArrayList<>();
        for (MtimeHallDictT hall : hallList) {
            HallTypeVO vo = new HallTypeVO();
            vo.setHalltypeId(hall.getUuid());
            vo.setHalltypeName(hall.getShowName());
            if (hallTypeId == hall.getUuid()) {
                vo.setActive(true);
            }else {
                vo.setActive(false);
            }
            vos.add(vo);
        }
        return vos;
    }


    @Override
    public CinemaInfoVO getCinemaInfo(Integer cinemaId) {
        MtimeCinemaT cinemaT = new MtimeCinemaT();
        cinemaT.setUuid(cinemaId);
        MtimeCinemaT cinema = cinemaTMapper.selectOne(cinemaT);
        CinemaInfoVO vo = convertToCinemaVo2(cinema);
        return vo;
    }

    private CinemaInfoVO convertToCinemaVo2(MtimeCinemaT cinema) {
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        cinemaInfoVO.setCinemaAdress(cinema.getCinemaAddress());
        cinemaInfoVO.setCinemaId(cinema.getUuid());
        cinemaInfoVO.setCinemaName(cinema.getCinemaName());
        cinemaInfoVO.setCinemaPhone(cinema.getCinemaPhone());
        cinemaInfoVO.setImgUrl(cinema.getImgAddress());
        return cinemaInfoVO;
    }

    @Override
    public List<FilmVO> getFilmsByCinemaId(Integer cinemaId) {
        List<FilmVO> films = fieldTMapper.getFilms(cinemaId);
        return films;
    }

    @Override
    public FieldDetailInfoVO getFieldDetail(Integer cinemaId, Integer fieldId,Boolean isNeedDiscountPrice) {
        CinemaInfoVO cinemaInfo = getCinemaInfo(cinemaId);
        HallInfoVO hallInfoVO = fieldTMapper.getFields(fieldId);
        FilmVO filmInfo = fieldTMapper.getFilmInfoByFieldId(fieldId);
        FieldDetailInfoVO fieldDetailInfoVO = new FieldDetailInfoVO();
        fieldDetailInfoVO.setCinemaInfo(cinemaInfo);
        fieldDetailInfoVO.setFilmInfo(filmInfo);
        fieldDetailInfoVO.setHallInfo(hallInfoVO);
        return fieldDetailInfoVO;
    }

    @Override
    public FieldOrderVO getFieldById(Integer fieldId) {
        FieldOrderVO vo = new FieldOrderVO();
        EntityWrapper<MtimeFieldT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("UUID",fieldId);
        List<MtimeFieldT> mtimeFieldTS = fieldTMapper.selectList(entityWrapper);
        if (CollectionUtils.isEmpty(mtimeFieldTS)) {
            return null;
        }else {
            MtimeFieldT mtimeFieldT = mtimeFieldTS.get(0);
            BeanUtils.copyProperties(mtimeFieldT,vo);
        }
        return vo;
    }
}