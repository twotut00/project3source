package com.stylefeng.guns.rest.modular.util;

import com.alibaba.dubbo.config.annotation.Service;
import  com.stylefeng.guns.api.util.CodeCreator;
import com.stylefeng.guns.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author: jia.xue
 * @create: 2019-08-08 16:55
 * @Description
 **/

@Component
@Service(interfaceClass = CodeCreator.class)
public class CodeCreatorImpl implements CodeCreator {
    private transient final Logger logger = LoggerFactory.getLogger(CodeCreator.class);
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private MachineCode machineCode;

    private static Integer machineNo;

    //machineNo的缓存key
    private static final String MACHINE_NO_KEY = "GUNS_MACHINE_NO_FOR_CREATE_CODE";

    //分布式锁key
    private static final String MACHINE_LOCK_KEY = "GUNS_MACHINE_LOCK_FOR_CREATE_CODE";
    //分布式锁过期时间(3秒)
    private static final Long MACHINE_LOCK_VALUE_EXPIRE_TIME = 3000l;

    private static final Map<String, Object> syncObjects = new HashMap<String, Object>();
    private static final Map<String, List<String>> tagsCache = new ConcurrentHashMap<String, List<String>>();
    private static final DecimalFormat formatter = new DecimalFormat("00000");
    private static final Map<String, Integer> codesCache = new ConcurrentHashMap<String, Integer>();


    /**
     * 返回不重复的业务编码
     * @param tag 标识
     * 		每个标识代表一个业务
     * @return prefix + 随机数
     */
    @Override
    public String createNo(String tag) {
        // 每个业务创建单独的同步锁对象
        if (!syncObjects.containsKey(tag)) {
            syncObjects.put(tag, new Object());
        }
        synchronized (syncObjects.get(tag)) {
            String dateTime = DateUtil.getTime2();
            return tag + dateTime + getMachineNo() + getCode(tag, dateTime);
        }
    }

    /**
     * 获取业务编码后续编号
     * @param tag
     * @param dateTime
     * @return
     */
    private String getCode(String tag, String dateTime) {
        String codeKey = tag + dateTime;
        String codeValue = "00000";

        // 读取当前业务+时间（精确到秒）是否已经存在
        if (codesCache.containsKey(codeKey)) {
            if (codesCache.get(codeKey).intValue() >= 99999) {
                logger.info("{}当前业务编码溢出...", codeKey);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    logger.info("线程暂停，发生异常", e);
                }

                // 随机数已存在，重新获取
                return getCode(tag, DateUtil.getTime2());
            } else {
                // 保存当前值
                Integer codeInt = codesCache.get(codeKey) + 1;
                codeValue = formatter.format(codeInt);
                codesCache.put(codeKey, codeInt);
            }
        } else {
            // 创建新的缓存
            Integer codeInt = 1;
            codeValue = formatter.format(codeInt);
            codesCache.put(codeKey, codeInt);
        }

        // 初始化各个业务的业务前缀缓存
        if (tagsCache.get(tag) == null) {
            tagsCache.put(tag, new ArrayList<String>());
        }
        // 先删后加，缓存中最多保留三个元素
        if (tagsCache.get(tag).size() > 2) {
            String key = tagsCache.get(tag).remove(2);
            if (codesCache.containsKey(key)) {
                codesCache.remove(key);
            }
        }

        // 新的业务编码，保存到缓存中
        if (!tagsCache.get(tag).contains(codeKey)) {
            tagsCache.get(tag).add(0, codeKey);
        }

        return codeValue;
    }

    /**
     * 返回机器随机编码
     * @return
     */
    private int getMachineNo() {
        if (machineNo == null) {
            machineNo = getMachineNoFromProperties();
            logger.info("生成业务编号，机器获得编码：{}", machineNo);
        }

        return machineNo;
    }

    private synchronized int getMachineNoFromProperties() {
        if (machineNo != null) {
            return machineNo;
        }

        Integer configMachineNo = machineCode.getMachineNo();
        if (configMachineNo != null) {
            logger.info("从配置中读取机器码，得到编码：{}", configMachineNo);
            return configMachineNo;
        }

        return getMachineNoFromCache();
    }

    /**
     * 从缓存中读取
     * @return
     */
    private synchronized int getMachineNoFromCache() {
        if (machineNo != null) {
            return machineNo;
        }
        Integer cacheMachineNo = 0;
        Integer times = 0;

        //重试10次
        while (times < 10) {
            String  value = (System.currentTimeMillis() + MACHINE_LOCK_VALUE_EXPIRE_TIME) + "";
            boolean lock = redisLock.lock(MACHINE_LOCK_KEY, value);
            // 如果拿到了锁，就去获取缓存中存入的机器码
            if (lock) {
                String cacheNo = redisTemplate.opsForValue().get(MACHINE_NO_KEY);
                if (StringUtils.isEmpty(cacheNo) || Integer.valueOf(cacheNo) >= 9) {
                    logger.info("从缓存中没有获得机器编码，设定为初始值1");
                    redisTemplate.opsForValue().set(MACHINE_NO_KEY,"1",3000l,TimeUnit.SECONDS);
                    redisLock.unlock(MACHINE_LOCK_KEY,value);
                    break;
                }else {
                    logger.info("从缓存中取得机器编码，cacheNo:{}",cacheNo);
                    cacheMachineNo = Integer.valueOf(cacheNo) + 1;
                    redisTemplate.opsForValue().set(MACHINE_NO_KEY,cacheMachineNo.toString(),3000l,TimeUnit.SECONDS);
                    redisLock.unlock(MACHINE_LOCK_KEY,value);
                    break;
                }
            }
            times ++ ;
        }
        logger.info("从缓存中读取机器码，得到编码：{}", cacheMachineNo);
        return cacheMachineNo;
    }

}