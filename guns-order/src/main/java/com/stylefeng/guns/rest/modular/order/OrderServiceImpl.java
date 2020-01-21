package com.stylefeng.guns.rest.modular.order;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.FieldOrderVO;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.FilmInfoVO;
import com.stylefeng.guns.api.order.OrderServiceApi;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.core.constant.OrderStatus;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.FileUtil;
import com.stylefeng.guns.core.util.UUIDUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-16 22:15
 * @Description
 **/
@Component
@Service(interfaceClass = OrderServiceApi.class)
public class OrderServiceImpl implements OrderServiceApi {

    @Autowired
    private MoocOrderTMapper orderMapper;


    @Reference(interfaceClass = CinemaServiceApi.class,check = false)
    private CinemaServiceApi cinemaSerice;
    @Reference(interfaceClass = FilmServiceApi.class,check = false)
    private FilmServiceApi filmService;

    @Override
    public Boolean isTrueSeats(String seats, String fieldId) {
        Boolean flag = true;
        String fileAddress = orderMapper.getSeatsByFieldId(Integer.valueOf(fieldId));
        String address = "/project4/server/resources/" + fileAddress ;
        String jsonString = null;
        try {
            jsonString = FileUtil.getContent(address);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String ids = (String)jsonObject.get("ids");
        String[] strings = seats.split(",");
            for (int i= 0; i<strings.length;i++) {
            if (!ids.contains(strings[i])) {
                flag = false;
                break;
            }
        }
        return flag;
}

    @Override
    public Boolean isNotSoldSeats(String fieldId, String seats) {
        Boolean flag = true;
        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("field_id",fieldId);
        List<MoocOrderT> orders = orderMapper.selectList(entityWrapper);
        String[] strings = seats.split(",");
        for (int i= 0; i<strings.length;i++) {
            for (MoocOrderT moocOrderT : orders) {
                String seatsIds = moocOrderT.getSeatsIds();
                if (seatsIds.contains(strings[i])){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId,Integer discountPrice) {
        FieldOrderVO field = cinemaSerice.getFieldById(fieldId);
        MoocOrderT order = buildOrder(field,soldSeats,seatsName,userId,discountPrice);
        Integer insert = orderMapper.insert(order);
        if (1 != insert) {
            throw new RuntimeException("保存订单失败！");
        }else {
            OrderVO vo = buildOrderVO(field,order);
            return vo;
        }
    }

    private OrderVO buildOrderVO(FieldOrderVO field, MoocOrderT order) {
        FilmInfoVO filmInfo = filmService.getFilmInfoVOById(field.getFilmId() + "");
        CinemaInfoVO cinemaInfo = cinemaSerice.getCinemaInfo(field.getCinemaId());
        OrderVO vo = new OrderVO();
        vo.setOrderId(order.getUuid());
        vo.setFilmName(filmInfo.getFilmName());
        String formatDate = DateUtil.formatDate(order.getOrderTime(), "yyyy-MM-dd HH:mm");
        vo.setFieldTime(formatDate);
        vo.setCinemaName(cinemaInfo.getCinemaName());
        vo.setSeatsName(order.getSeatsName());
        vo.setOrderPrice(order.getOrderPrice().toString());
        vo.setOrderTimestamp(order.getOrderTime().getTime()+"");
        vo.setOrderStatus(OrderStatus.valueOf(OrderStatus.class,order.getOrderStatus()).getDescription());
        return vo;
    }

    /**
     * 构建Order参数
     * @param field
     * @param soldSeats
     * @param seatsName
     * @param userId
     * @return
     */
    private MoocOrderT buildOrder(FieldOrderVO field, String soldSeats, String seatsName, Integer userId,Integer discountPrice) {
        MoocOrderT order = new MoocOrderT();
        order.setUuid(UUIDUtil.getUUID());
        order.setCinemaId(field.getCinemaId());
        order.setFieldId(field.getUuid());
        order.setFilmId(field.getFilmId());
        order.setSeatsIds(soldSeats);
        order.setSeatsName(seatsName);
        order.setFilmPrice(field.getPrice().doubleValue());
        BigDecimal size = new BigDecimal(soldSeats.split(",").length);
        BigDecimal price;
        if (discountPrice != null) {
            price = new BigDecimal(discountPrice);
        }else {
            price = new BigDecimal(field.getPrice());
        }

        BigDecimal result = size.multiply(price);
        result.setScale(2,RoundingMode.HALF_UP);
        order.setOrderPrice(result.doubleValue());
        order.setOrderTime(new Date());
        order.setOrderUser(userId);
        order.setOrderStatus(OrderStatus.NOT_PAY.getIndex());

        return order;
    }

    @Override
    public List<OrderVO> getOrdersByUserId(Integer userId,Integer nowPage,Integer pageSize) {

        Integer startIndex = (nowPage - 1) * pageSize;
        List<OrderVO> orderList = orderMapper.getOrderListByUserId(userId,startIndex,pageSize);
        return orderList;
    }
    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("field_id",fieldId);
        List<MoocOrderT> orderList = orderMapper.selectList(entityWrapper);
        if (CollectionUtils.isEmpty(orderList)){
            return null;
        }
        Boolean flag = false;
        StringBuffer stringBuffer = new StringBuffer();
        for (MoocOrderT order : orderList) {
            String seatsIds = order.getSeatsIds();
            if (flag){
            stringBuffer.append(",");
            }
            stringBuffer.append(seatsIds);
            flag = true;
        }
        return stringBuffer.toString();
    }


    @Override
    public OrderVO getOrderVOById(String orderId) {

        OrderVO vo = orderMapper.getOrderVOById(orderId);
        return vo;
    }

    @Override
    public Boolean paySuccess(String orderId) {
        MoocOrderT order = new MoocOrderT();
        order.setUuid(orderId);
        order.setOrderStatus(OrderStatus.PAYED.getIndex());
        Integer integer = orderMapper.updateById(order);
        if (integer == 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean payFail(String orderId) {
        MoocOrderT order = new MoocOrderT();
        order.setUuid(orderId);
        order.setOrderStatus(OrderStatus.CLOSED.getIndex());
        Integer integer = orderMapper.updateById(order);
        if (integer == 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}