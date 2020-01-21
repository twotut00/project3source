package com.stylefeng.guns.api.promo.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.FieldDetailInfoVO;
import com.stylefeng.guns.api.cinema.vo.FieldInfoVo;
import com.stylefeng.guns.api.cinema.vo.FieldsVo;
import com.stylefeng.guns.api.film.vo.FilmInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: jia.xue
 * @create: 2019-08-05 17:22
 * @Description
 *
 * */
@Data
public class PromoVO implements Serializable {
    private static final long serialVersionUID = 164298210673436521L;

    //秒杀活动编号
    private Integer uuid;

    //秒杀价格
    private Integer price;

    //活动开始时间
    private Date startTime;

    //活动结束时间
    private Date endTime;

    //活动状态
    private Integer status;

    //活动秒杀库存
    private Integer stock;

    //秒杀活动秒杀
    private String description;

    //秒杀活动关联影院id
    private Integer cinemaId;

    //影院名称
    private String cinemaName;

    //影院地址
    private String cinemaAddress;

    //影院图片
    private String imgAddress;


}