package com.stylefeng.guns.rest.modular.film.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
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
 * @create: 2019-06-12 17:50
 * @Description
 **/
@Component
@Service(interfaceClass = FilmServiceApi.class)

public class DefaultFilmServiceImpl implements FilmServiceApi {

    @Autowired
    private MtimeBannerTMapper mtimeBannerTMapper;

    @Autowired
    private MtimeFilmTMapper mtimeFilmTMapper;

    @Autowired
    private MtimeCatDictTMapper mtimeCatDictTMapper;

    @Autowired
    private MtimeSourceDictTMapper mtimeSourceDictTMapper;

    @Autowired
    private MtimeYearDictTMapper mtimeYearDictTMapper;

    @Autowired
    private MtimeFilmInfoTMapper mtimeFilmInfoTMapper;

    @Autowired
    private MtimeActorTMapper mtimeActorTMapper;


    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO> bannerVOS = new ArrayList<>();
        List<MtimeBannerT> mtimeBannerTS = mtimeBannerTMapper.selectList(null);
        if (CollectionUtils.isEmpty(mtimeBannerTS)) {
            return bannerVOS;
        }
        for (MtimeBannerT mtimeBannerT: mtimeBannerTS) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(mtimeBannerT.getUuid()+"");
            bannerVO.setBannerAddress(mtimeBannerT.getBannerAddress());
            bannerVO.setBannerUrl(mtimeBannerT.getBannerUrl());
            bannerVOS.add(bannerVO);
        }
        return bannerVOS;
    }

    @Override
    public FilmVO getHotFilms(Boolean isLimit, int nums,int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();

        EntityWrapper<MtimeFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");

        if (isLimit) {
            Page<MtimeFilmT> page = null;
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage,nums,"film_box_office");
                case 2:
                    page = new Page<>(nowPage,nums,"film_time");
                case 3:
                    page = new Page<>(nowPage,nums,"film_source");
                default:
                    page = new Page<>(nowPage,nums,"film_box_office");
                    break;
            }
            List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, entityWrapper);
            List<FilmInfo> list = converToFilmInfo(mtimeFilmTS);
            filmVO.setFilmNum(list.size());
            filmVO.setFilmInfo(list);

        }else {
            Page<MtimeFilmT> page = new Page<>(nowPage,nums);
            if (sourceId != 99) {
                entityWrapper.eq("film_source",sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                entityWrapper.like("film_cats","%#"+catId+"#%");
            }
            List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, entityWrapper);
            List<FilmInfo> list = converToFilmInfo(mtimeFilmTS);
            filmVO.setFilmNum(list.size());

            Integer totalCounts = mtimeFilmTMapper.selectCount(entityWrapper);
            Integer totalPages = totalCounts/nums;
            Integer model = totalCounts%nums;
            totalPages = (model==0)? totalPages : (totalPages +1);

            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalPages);

            filmVO.setFilmInfo(list);

        }

        return filmVO;
    }

    private List<FilmInfo> converToFilmInfo(List<MtimeFilmT> mtimeFilmTS) {
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setScore(mtimeFilmT.getFilmScore());
            filmInfo.setImgAddress(mtimeFilmT.getImgAddress());
            filmInfo.setFilmType(mtimeFilmT.getFilmType());
            filmInfo.setFilmScore(mtimeFilmT.getFilmScore());
            filmInfo.setFilmName(mtimeFilmT.getFilmName());
            filmInfo.setFilmId(mtimeFilmT.getUuid()+"");
            filmInfo.setExpectNum(mtimeFilmT.getFilmPresalenum());
            filmInfo.setBoxNum(mtimeFilmT.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDay(mtimeFilmT.getFilmTime()));
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    @Override
    public FilmVO getSoonFilms(Boolean isLimit, int nums,int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();

        EntityWrapper<MtimeFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","2");

        if (isLimit) {
            Page<MtimeFilmT> page = null;
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                case 2:
                    page = new Page<>(nowPage,nums,"film_time");
                case 3:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                default:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
            }
            List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, entityWrapper);
            List<FilmInfo> list = converToFilmInfo(mtimeFilmTS);
            filmVO.setFilmNum(list.size());
            filmVO.setFilmInfo(list);

        }else {
            Page<MtimeFilmT> page = new Page<>(nowPage,nums);
            if (sourceId != 99) {
                entityWrapper.eq("film_source",sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                entityWrapper.like("film_cats","%#"+catId+"#%");
            }
            List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, entityWrapper);
            List<FilmInfo> list = converToFilmInfo(mtimeFilmTS);
            filmVO.setFilmNum(list.size());

            Integer totalCounts = mtimeFilmTMapper.selectCount(entityWrapper);
            Integer totalPages = totalCounts/nums;
            Integer model = totalCounts%nums;
            totalPages = (model==0)? totalPages : (totalPages +1);

            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalPages);

            filmVO.setFilmInfo(list);


        }

        return filmVO;
    }

    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {

        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();

        EntityWrapper<MtimeFilmT> entityWrapper = new EntityWrapper<>();

        Page<MtimeFilmT> page = null;
        switch (sortId) {
            case 1:
                page = new Page<>(nowPage,nums,"film_box_office");
            case 2:
                page = new Page<>(nowPage,nums,"film_time");
            case 3:
                page = new Page<>(nowPage,nums,"film_source");
            default:
                page = new Page<>(nowPage,nums,"film_box_office");
                break;
        }
        if (sourceId != 99) {
            entityWrapper.eq("film_source",sourceId);
        }
        if (yearId != 99) {
            entityWrapper.eq("film_date", yearId);
        }
        if (catId != 99) {
            entityWrapper.like("film_cats","%#"+catId+"#%");
        }
        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> list = converToFilmInfo(mtimeFilmTS);
        filmVO.setFilmNum(list.size());

        Integer totalCounts = mtimeFilmTMapper.selectCount(entityWrapper);
        Integer totalPages = totalCounts/nums;
        Integer model = totalCounts%nums;
        totalPages = (model==0)? totalPages : (totalPages +1);

        filmVO.setNowPage(nowPage);
        filmVO.setTotalPage(totalPages);

        filmVO.setFilmInfo(list);

        return filmVO;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        //条件  正在上映的前10名

        EntityWrapper<MtimeFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");

        Page<MtimeFilmT> page = new Page<>(1,10,"film_box_office");

        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = converToFilmInfo(mtimeFilmTS);
        return filmInfos;

    }

    @Override
    public List<FilmInfo> getExpectRanking() {

        EntityWrapper<MtimeFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","2");
        //条件  即将上映的前10名

        Page<MtimeFilmT> page = new Page<>(1,10,"film_preSaleNum");

        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = converToFilmInfo(mtimeFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {

        EntityWrapper<MtimeFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");
        //评分前10名 正在上映的
        Page<MtimeFilmT> page = new Page<>(1,10,"film_score");

        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = converToFilmInfo(mtimeFilmTS);
        return filmInfos;
    }

    @Override
    public List<CatVO> getCats() {
        List<MtimeCatDictT> mtimeCatDictTS;
        mtimeCatDictTS = mtimeCatDictTMapper.selectList(null);
        List<CatVO> list = converToCatVO(mtimeCatDictTS);
        return list;
    }

    private List<CatVO> converToCatVO(List<MtimeCatDictT> mtimeCatDictTS) {
        List<CatVO> list = new ArrayList<CatVO>();
        if (CollectionUtils.isEmpty(mtimeCatDictTS)) {
            return list;
        }else {
            for (MtimeCatDictT mtimeCatDictT : mtimeCatDictTS) {
                CatVO catVO = new CatVO();
                catVO.setCatId(mtimeCatDictT.getUuid()+"");
                catVO.setCatName(mtimeCatDictT.getShowName());
                catVO.setActive(false);
                list.add(catVO);
            }
            return list;
        }
    }

    @Override
    public List<SourceVO> getSources() {

        List<MtimeSourceDictT> mtimeSourceDictTS;
        mtimeSourceDictTS = mtimeSourceDictTMapper.selectList(null);
        List<SourceVO> list = converToSourceVO(mtimeSourceDictTS);
        return list;
    }
    private List<SourceVO> converToSourceVO(List<MtimeSourceDictT> mtimeSourceDictTS) {
        List<SourceVO> sourceVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(mtimeSourceDictTS)) {
            return sourceVOS;
        }else {
            for (MtimeSourceDictT mtimeSourceDictT : mtimeSourceDictTS) {
                SourceVO sourceVO = new SourceVO();
                sourceVO.setSourceId(mtimeSourceDictT.getUuid()+"");
                sourceVO.setSourceName(mtimeSourceDictT.getShowName());
                sourceVOS.add(sourceVO);
            }
            return sourceVOS;
        }
    }

    @Override
    public List<YearVO> getYears() {
        List<MtimeYearDictT> mtimeYearDictTS;
        mtimeYearDictTS = mtimeYearDictTMapper.selectList(null);
        List<YearVO> list = converToYearVO(mtimeYearDictTS);
        return list;
    }
    private List<YearVO> converToYearVO(List<MtimeYearDictT> mtimeYearDictTS) {
        List<YearVO> yearVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(mtimeYearDictTS)) {
            return yearVOS;
        }else {
            for (MtimeYearDictT mtimeYearDictT : mtimeYearDictTS) {
                YearVO yearVO = new YearVO();
                yearVO.setYearId(mtimeYearDictT.getUuid()+"");
                yearVO.setYearName(mtimeYearDictT.getShowName());
                yearVOS.add(yearVO);
            }
            return yearVOS;
        }
    }


    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
        //searchType 1 名称 2 Id
        FilmDetailVO filmDetailVO ;
        if (searchType == 1) {
            filmDetailVO = mtimeFilmTMapper.getFilmDetailByName(searchParam);
        }else {
            filmDetailVO = mtimeFilmTMapper.getFilmDetailById(searchParam);
        }
        return filmDetailVO;
    }


    @Override
    public FilmDescVO getFilmDesc(String filmId) {

        MtimeFilmInfoT filmInfo = getFilmInfoById(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(filmInfo.getBiography());
        filmDescVO.setFilmId(filmInfo.getFilmId());
        return filmDescVO;
    }



    @Override
    public ImgVO getImgs(String filmId) {
        MtimeFilmInfoT filmInfo = getFilmInfoById(filmId);
        ImgVO imgVO = new ImgVO();
        String filmImgs = filmInfo.getFilmImgs();
        String[] split = filmImgs.split(",");
        imgVO.setImg01(split[1]);
        imgVO.setImg02(split[2]);
        imgVO.setImg03(split[3]);
        imgVO.setImg04(split[4]);
        imgVO.setMainImg(split[0]);

        return imgVO;
    }

    @Override
    public ActorVO getDectInfo(String filmId) {
        MtimeFilmInfoT filmInfo = getFilmInfoById(filmId);
        Integer directorId = filmInfo.getDirectorId();

        MtimeActorT mtimeActorT = mtimeActorTMapper.selectById(directorId);
        ActorVO actorVO = new ActorVO();
        actorVO.setDirectorName(mtimeActorT.getActorName());
        actorVO.setImgAddress(mtimeActorT.getActorImg());
        return actorVO;
    }

    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actors = mtimeActorTMapper.getActors(filmId);
        return actors;
    }

    private MtimeFilmInfoT getFilmInfoById(String filmId) {
        MtimeFilmInfoT mtimeFilmInfoT  = new MtimeFilmInfoT();
        mtimeFilmInfoT.setFilmId(filmId);
        MtimeFilmInfoT filmInfoT = mtimeFilmInfoTMapper.selectOne(mtimeFilmInfoT);
        return filmInfoT;
    }

    @Override
    public FilmInfoVO getFilmInfoVOById(String filmId) {
        FilmInfoVO filmInfoVO = new FilmInfoVO();
        MtimeFilmT film  = new MtimeFilmT();
        film.setUuid(Integer.valueOf(filmId));
        MtimeFilmT mtimeFilmT = mtimeFilmTMapper.selectOne(film);
        BeanUtils.copyProperties(mtimeFilmT,filmInfoVO);
        return filmInfoVO;
    }
}