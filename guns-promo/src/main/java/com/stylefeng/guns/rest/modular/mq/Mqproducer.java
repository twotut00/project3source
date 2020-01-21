package com.stylefeng.guns.rest.modular.mq;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.api.promo.PromoServiceApi;
import com.stylefeng.guns.core.constant.StockLogStatus;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: jia.xue
 * @create: 2019-08-13 15:37
 * @Description
 **/
@Slf4j
@Component
public class Mqproducer {

    private DefaultMQProducer mqProducer;

    private TransactionMQProducer transactionMQProducer;

    @Value("${mq.nameserver.addr}")
    private String addr;
    @Value("${mq.topicname}")
    private String topicName;

    @Autowired
    private PromoServiceApi promoServiceApi;

    @Autowired
    private MtimeStockLogMapper mtimeStockLogMapper;

    /**
     * 初始化mqProducer
     * @throws MQClientException
     */
    @PostConstruct
    public void init() throws MQClientException {
        log.info("mqProducer ->初始化...");
        mqProducer = new DefaultMQProducer("producer_group");
        mqProducer.setNamesrvAddr(addr);
        mqProducer.start();

        transactionMQProducer = new TransactionMQProducer("transaction_producer_group");
        transactionMQProducer.setNamesrvAddr(addr);
        transactionMQProducer.start();
        log.info("transactionMQProducer ->初始化...");
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object args) {
                Integer promoId = (Integer)((Map<String,Object>)args).get("promoId");
                Integer amount = (Integer)((Map<String,Object>)args).get("amount");
                Integer userId = (Integer)((Map<String,Object>)args).get("userId");
                String stockLogId = (String) ((Map<String,Object>)args).get("stockLogId");
                try {
                    promoServiceApi.savePromoOrderVO(promoId,userId,amount,stockLogId);
                } catch (Exception e) {
                    e.printStackTrace();
                    //设置对应的stockLog为回滚状态
                    MtimeStockLog stockLog = mtimeStockLogMapper.selectById(stockLogId);
                    stockLog.setStatus(StockLogStatus.FAIL.getIndex());
                    Integer affectedRows = mtimeStockLogMapper.updateById(stockLog);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                String jsonString = new String(messageExt.getBody());
                Map<String,Object>bodyMap = JSON.parseObject(jsonString, Map.class);
                String promoId =(String) bodyMap.get("promoId");
                String amount = (String)bodyMap.get("amount");
                String stockLogId = (String)bodyMap.get("stockLogId");
                MtimeStockLog stockLog = mtimeStockLogMapper.selectById(stockLogId);
                if (stockLog == null) {
                    return LocalTransactionState.UNKNOW;
                }
                Integer status = stockLog.getStatus();
                if (StockLogStatus.INIT.getIndex() == status) {
                    return LocalTransactionState.UNKNOW;
                }else if (StockLogStatus.SUCCESS.getIndex() == status) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }else {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }
        });
    }

    //事务型同步库存扣减消息
    public Boolean  transactionAsyncReduceStock(Integer promoId, Integer userId,Integer amount,String stockLogId){
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("promoId",promoId+"");
        bodyMap.put("amount",amount+"");
        bodyMap.put("stockLogId",stockLogId);

        Map<String, Object> argsMap = new HashMap<>();
        argsMap.put("promoId",promoId);
        argsMap.put("amount",amount);
        argsMap.put("userId",userId);
        argsMap.put("stockLogId",stockLogId);
        Message message = new Message(topicName,"increase",JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));
        TransactionSendResult transactionSendResult = null;
        try {
            /**
             * 1.投递事务型消息,等待下面执行结果回调
             * 2.调用executeLocalTransaction
             */
            transactionSendResult = transactionMQProducer.sendMessageInTransaction(message, argsMap);

        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        }
        if (transactionSendResult.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE) {
            return true;
        }
        if (transactionSendResult.getLocalTransactionState() == LocalTransactionState.ROLLBACK_MESSAGE) {
            return false;
        }
        return false;
    }

    //同步库存扣减消息
    public Boolean  asyncReduceStock(Integer promoId, Integer amount){
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("promoId",promoId+"");
        bodyMap.put("amount",amount+"");
        Message message = new Message(topicName,"increase",JSON.toJSON(bodyMap).toString().getBytes(Charset.forName("UTF-8")));
        try {
            mqProducer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}