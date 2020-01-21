package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionListVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
     * @author: jia.xue
     * @create: 2019-06-12 10:54
     * @Description
     **/
    @RequestMapping("/film")
    @RestController
    public class FilmController {

        private static final String IMG_PRE = "http://img.meetingshop.cn/";

        @Reference(interfaceClass = FilmServiceApi.class,check = false)
        private FilmServiceApi filmServiceApi;
        /**
         * 获取首页信息
         * 1 获取banner信息
         * 2 获取热映影片
         * 3 即将上映电影
         * 4 票行排行
         * 5 人气榜
     * 6 获取前100
     * @return
     */
    @RequestMapping(value = "/getIndex",method = RequestMethod.GET)
    public ResponseVO getIndex() {

        FilmIndexVO filmIndexVO = new FilmIndexVO();

        filmIndexVO.setBanners(filmServiceApi.getBanners());

        filmIndexVO.setHotFilms(filmServiceApi.getHotFilms(Boolean.TRUE,8,1,1,99,99,99));

        filmIndexVO.setSoonFilms(filmServiceApi.getSoonFilms(Boolean.TRUE,8,1,1,99,99,99));

        filmIndexVO.setBoxRanking(filmServiceApi.getBoxRanking());

        filmIndexVO.setExpectRanking(filmServiceApi.getExpectRanking());

        filmIndexVO.setTop100(filmServiceApi.getTop());

        return ResponseVO.success(IMG_PRE,filmIndexVO);


    }

    /**
     * 影片查询条件接口
     * @param catId
     * @param sourceId
     * @param yearId
     * @return
     */
    @RequestMapping(value = "/getConditionList",method = RequestMethod.GET)
    public ResponseVO getConditionList(@RequestParam(name = "catId", defaultValue = "99", required = false) String catId,
                                            @RequestParam(name = "sourceId", defaultValue = "99", required = false)String sourceId,
                                            @RequestParam(name = "yearId", defaultValue = "99", required = false)String yearId){

        FilmConditionListVO conditionListVO = new FilmConditionListVO();

        List<CatVO> cats = filmServiceApi.getCats();
        List<SourceVO> sources = filmServiceApi.getSources();
        List<YearVO> years = filmServiceApi.getYears();

        List<String> catIds = cats.stream().map(CatVO::getCatId).distinct().collect(Collectors.toList());
        List<String> sourceIds = sources.stream().map(SourceVO::getSourceId).distinct().collect(Collectors.toList());
        List<String> yearIds = years.stream().map(YearVO::getYearId).distinct().collect(Collectors.toList());

        for (CatVO catVO : cats) {
            String catVOCatId = catVO.getCatId();
            if (catVOCatId.equals(catId)){
                catVO.setActive(true);
            } else {
                catVO.setActive(false);
            }
        }
        for (SourceVO sourceVO : sources) {
            if (sourceId.equals(sourceVO.getSourceId())){
                sourceVO.setActive(true);
            }else {
                sourceVO.setActive(false);
            }
        }

        for (YearVO yearVO : years) {
            if (yearId.equals(yearVO.getYearId())) {
                yearVO.setActive(true);
            }else {
                yearVO.setActive(false);
            }
        }

        conditionListVO.setCatInfo(cats);
        conditionListVO.setSourceInfo(sources);
        conditionListVO.setYearInfo(years);
        return ResponseVO.success(conditionListVO);
    }


    @RequestMapping(value = "/getFilms", method = RequestMethod.GET)
    public ResponseVO getFilms(FilmRequestVO filmRequestVO){

        FilmVO filmVO = null;
        switch (filmRequestVO.getShowType()) {
            case 1:
                filmVO = filmServiceApi.getHotFilms(false,filmRequestVO.getPageSize(),
                        filmRequestVO.getNowPage(),filmRequestVO.getSortId(),
                        filmRequestVO.getSourceId(),filmRequestVO.getYearId(),filmRequestVO.getCatId());
                break;

            case 2: filmVO = filmServiceApi.getSoonFilms(false,filmRequestVO.getPageSize(),
                    filmRequestVO.getNowPage(),filmRequestVO.getSortId(),
                    filmRequestVO.getSourceId(),filmRequestVO.getYearId(),filmRequestVO.getCatId());
                break;

            case 3:filmVO = filmServiceApi.getClassicFilms(filmRequestVO.getPageSize(),
                    filmRequestVO.getNowPage(),filmRequestVO.getSortId(),
                    filmRequestVO.getSourceId(),filmRequestVO.getYearId(),filmRequestVO.getCatId());
                break;

            default:
                filmVO = filmServiceApi.getHotFilms(false,filmRequestVO.getPageSize(),
                    filmRequestVO.getNowPage(),filmRequestVO.getSortId(),
                    filmRequestVO.getSourceId(),filmRequestVO.getYearId(),filmRequestVO.getCatId());
                break;

        }
        return ResponseVO.success(filmVO.getFilmInfo(),filmVO.getNowPage(),filmVO.getTotalPage(),IMG_PRE);

    }

    @RequestMapping(value = "/films/{searchParam}",method = RequestMethod.GET)
    public ResponseVO getFilmDetails(@PathVariable("searchParam") String searchParam,int searchType){

        //根据searchParam 判断searchType
        //不同的查询类型，传入查询的条件不同
        //查询影片的详细信息 dubbo --> 异步获取
        FilmDetailVO filmDetail = filmServiceApi.getFilmDetail(searchType, searchParam);
        if (filmDetail == null) {
            return ResponseVO.fail("查询失败，无影片可加载");
        }
        if (StringUtils.isBlank(filmDetail.getFilmId())){
            return ResponseVO.fail("系统出现异常，请联系管理员");
        }

        String filmId = filmDetail.getFilmId();

        ImgVO imgs = filmServiceApi.getImgs(filmId);
        ActorVO dectInfo = filmServiceApi.getDectInfo(filmId);
        List<ActorVO> actors = filmServiceApi.getActors(filmId);
        FilmDescVO filmDesc = filmServiceApi.getFilmDesc(filmId);

        InfoRequestVO infoRequestVO = new InfoRequestVO();
        ActorRequestVO actorRequestVO = new ActorRequestVO();
        actorRequestVO.setActors(actors);
        actorRequestVO.setDirector(dectInfo);

        infoRequestVO.setActors(actorRequestVO);
        infoRequestVO.setFilmId(filmId);
        infoRequestVO.setImgVO(imgs);
        infoRequestVO.setBiopgraphy(filmDesc.getBiography());

        filmDetail.setInfo04(infoRequestVO);

        return ResponseVO.success(IMG_PRE,filmDetail);

    }



}