package com.stylefeng.guns.rest.modular.vo;


import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.constant.ResponseStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: jia.xue
 * @create: 2019-06-11 20:21
 * @Description
 **/
@Slf4j
public class ResponseVO<M> {

    private int status;

    private String msg;

    //图片前缀
    private String imgPre;

    private M data;

    private Integer nowPage;

    private Integer totalPage;

    public ResponseVO() {
    }

    /**
     * 业务成功
     * @param m
     * @param <M>
     * @return
     */
    public static<M> ResponseVO success(M m){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(ResponseStatus.success.getIndex());
        responseVO.setData(m);
        log.info(JSON.toJSONString(responseVO));
        return responseVO;
    }

    /**
     * 业务成功
     * @param m
     * @param <M>
     * @param nowPage
     * @param totalPage
     * @return
     */
    public static<M> ResponseVO success(M m,Integer nowPage,Integer totalPage, String imgPre){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(ResponseStatus.success.getIndex());
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        responseVO.setNowPage(nowPage);
        responseVO.setTotalPage(totalPage);
        log.info(JSON.toJSONString(responseVO));
        return responseVO;
    }

    /**
     * 业务成功,需要图片前缀
     * @param m
     * @param <M>
     * @return
     */
    public static<M> ResponseVO success(String imgPre,M m){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(ResponseStatus.success.getIndex());
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        log.info(JSON.toJSONString(responseVO));
        return responseVO;
    }

    /**
     * 业务成功
     * @param msg
     * @param <M>
     * @return
     */
    public static<M> ResponseVO success(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(ResponseStatus.success.getIndex());
        responseVO.setMsg(msg);
        log.info(JSON.toJSONString(responseVO));
        return responseVO;
    }

    /**
     * 业务失败
     * @param msg
     * @param <M>
     * @return
     */
    public static<M> ResponseVO fail(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(ResponseStatus.fail.getIndex());
        responseVO.setMsg(msg);
        log.info(JSON.toJSONString(responseVO));
        return responseVO;
    }


    /**
     * token 过期
     * @param
     * @param <M>
     * @return
     */
    public static<M> ResponseVO expire() {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(ResponseStatus.expire.getIndex());
        responseVO.setMsg(ResponseStatus.expire.getDescription());
        log.info(JSON.toJSONString(responseVO));
        return responseVO;
    }

    /**
     * 系统异常
     * @param msg
     * @param <M>
     * @return
     */
    public static<M> ResponseVO exception(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(ResponseStatus.exception.getIndex());
        responseVO.setMsg(msg);
        log.info(JSON.toJSONString(responseVO));
        return responseVO;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public M getData() {
        return data;
    }

    public void setData(M data) {
        this.data = data;
    }

    public String getImgPre() {
        return imgPre;
    }

    public void setImgPre(String imgPre) {
        this.imgPre = imgPre;
    }

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
}