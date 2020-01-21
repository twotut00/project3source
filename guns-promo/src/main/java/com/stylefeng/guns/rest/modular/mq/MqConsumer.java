package com.stylefeng.guns.rest.modular.mq;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author: jia.xue
 * @create: 2019-08-13 15:36
 * @Description
 **/
@Slf4j
@Component
public class MqConsumer {


    private DefaultMQPushConsumer mqConsumer;

    @Value("${mq.nameserver.addr}")
    private String addr;
    @Value("${mq.topicname}")
    private String topicName;
    @Autowired
    private MtimePromoStockMapper promoStockMapper;

    /**
     * 初始化mqConsumer
     * @throws MQClientException
     */
    @PostConstruct
    public void init() throws MQClientException {
        log.info("mqConsumer ->初始化...,topic:{} ");
        mqConsumer = new DefaultMQPushConsumer("stock_consumer_group");
        mqConsumer.setNamesrvAddr(addr);
        mqConsumer.subscribe(topicName,"*");


        mqConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //实现真正的扣减库存的操作
                Message message = list.get(0);
                String jsonString = new String(message.getBody());
                Map<String,Object>bodyMap = JSON.parseObject(jsonString, Map.class);
                String promoId =(String) bodyMap.get("promoId");
                String amount = (String)bodyMap.get("amount");
                Integer affectedRows = promoStockMapper.decreaseStock(Integer.valueOf(promoId), Integer.valueOf(amount));
                if (affectedRows > 0) {
                    log.info("数据库扣减库存成功！promoId:{},amount:{}",promoId,amount);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        mqConsumer.start();
    }
}