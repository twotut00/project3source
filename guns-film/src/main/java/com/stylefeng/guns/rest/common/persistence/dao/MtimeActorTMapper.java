package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.rest.common.persistence.model.MtimeActorT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author ciggar
 * @since 2019-06-12
 */
public interface MtimeActorTMapper extends BaseMapper<MtimeActorT> {

    List<ActorVO> getActors(@Param("filmId") String filmId);


}
