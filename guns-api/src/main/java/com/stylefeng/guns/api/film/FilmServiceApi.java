package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-12 17:23
 * @Description
 **/
public interface FilmServiceApi {


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


    List<BannerVO> getBanners();

    FilmVO getHotFilms(Boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    FilmVO getSoonFilms(Boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    List<FilmInfo> getBoxRanking();

    List<FilmInfo> getExpectRanking();

    List<FilmInfo> getTop();

    List<CatVO> getCats();

    List<SourceVO> getSources();

    List<YearVO> getYears();

    FilmDetailVO getFilmDetail(int searchType, String searchParam);

    FilmDescVO getFilmDesc(String filmId);

    ImgVO getImgs(String filmId);

    ActorVO getDectInfo(String filmId);

    List<ActorVO> getActors(String filmId);

    FilmInfoVO getFilmInfoVOById(String filmId);








}