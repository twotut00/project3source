package com.stylefeng.guns.test;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.promo.PromoServiceApi;
import com.stylefeng.guns.api.promo.vo.PromoVO;
import com.stylefeng.guns.api.util.CodeCreator;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: jia.xue
 * @create: 2019-08-08 15:42
 * @Description
 **/
@Slf4j
public class PromoTest extends BaseTest {

    @Autowired
    private PromoServiceApi promoService;

    @Autowired
    private CodeCreator codeCreator;

    private ExecutorService executorService;

    @Autowired
    private MtimeStockLogMapper mtimeStockLogMapper;

    @Autowired
    private MtimePromoStockMapper stockMapper;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(100);
    }

    @Test
    public void test01(){
        List<PromoVO> promoByCinemaId = promoService.getPromoByCinemaId(2);
        System.out.println(JSON.toJSONString(promoByCinemaId));
    }

    public static void main(String[] args) {
        String ip = NetUtils.getLocalAddress().getHostAddress();
        System.out.println(ip);
    }


    @Test
    public void test02(){
        for (int i=0;i<1000;i++){
                String orderNo = codeCreator.createNo("OD");
                System.err.println("----------------------------------orderNo: " + orderNo);
        }
    }

    /**
     * 测试秒杀保存订单接口
     */
    @Test
    public void test03(){
//        PromoOrderVO promoOrderVO = promoService.savePromoOrderVO(1, 12, 2);
//        System.out.println(JSON.toJSONString(promoOrderVO));
    }

    /**
     * 测试优化后的秒杀接口
     */
    @Test
    public void test04() {
//        Boolean aBoolean = promoService.publishPromoStock(2);
//        if (aBoolean) {
//            System.out.println("发布库存到缓存中成功！");
//        }
//        PromoOrderVO promoOrderVO = promoService.savePromoOrderVO(1, 12, 2);
//        System.out.println(JSON.toJSONString(promoOrderVO));
    }


    @Test
    public void test05() throws Exception {
        String stockLogId = promoService.initStockLog(1, 2);
        if (StringUtils.isEmpty(stockLogId)) {
            log.info("初始化订单流水状态失败！");
        }
        Boolean savePromoOrderRet = promoService.transactionSavePromoOrderVO(1, 12, 2, stockLogId);
        if (savePromoOrderRet) {
            System.out.println("创建事务型订单流水成功！");
        }
    }

    @Test
    public void test06() throws Exception {
        EntityWrapper<MtimeStockLog> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("uuid","SL20190814222713200001");
        List<MtimeStockLog> stockLogs = mtimeStockLogMapper.selectList(entityWrapper);
//        MtimeStockLog stockLog = mtimeStockLogMapper.selectById("SL20190814222713200001");
//        System.out.println(stockLog);
    }

    @Test
    public void test07() throws Exception {
        MtimePromoStock promoStock = stockMapper.selectById(1);
        System.out.println(promoStock);
    }

}