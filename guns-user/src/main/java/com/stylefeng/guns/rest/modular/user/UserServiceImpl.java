package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.api.user.UserApi;
import com.stylefeng.guns.api.user.UserInfoModel;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.api.user.vo.UserWalletOperateVO;
import com.stylefeng.guns.api.user.vo.UserWalletVO;
import com.stylefeng.guns.api.util.CodeCreator;
import com.stylefeng.guns.api.util.CodeCreatorPreConstants;
import com.stylefeng.guns.core.constant.UserWalletStatus;
import com.stylefeng.guns.core.constant.WalletOperType;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.CsUserMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserWalletMapper;
import com.stylefeng.guns.rest.common.persistence.dao.WalletOperLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.CsUser;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserWallet;
import com.stylefeng.guns.rest.common.persistence.model.WalletOperLog;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2019-06-11 16:44
 * @Description
 **/
@Component
@Service(interfaceClass = UserApi.class)
public class UserServiceImpl implements UserApi {
    private transient static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final Integer INIT_MONEY = 1000000;
    @Autowired
    private CsUserMapper csUserMapper;

    @Autowired
    private MtimeUserWalletMapper userWalletMapper;

    @Autowired
    private MtimeUserWalletMapper mtimeUserWalletMapper;

    @Autowired
    private WalletOperLogMapper walletOperLogMapper;

    @Reference(interfaceClass = CodeCreator.class,check = false)
    private CodeCreator codeCreator;

    public int login(String username, String password) {
        log.info("这个是user模块的打印, username:{}, password:{}",username,password);
        CsUser csUser = new CsUser();
        csUser.setUserName(username);
        CsUser user = csUserMapper.selectOne(csUser);
        if (user != null && user.getUuid() > 0 ){
            String userPwd = user.getUserPwd();
            String md5pwd = MD5Util.encrypt(password);
            if (md5pwd.equals(userPwd)) {
                log.info("系统登录成功！userName:[{}]",username);
                return user.getUuid();
            }
        }
        log.info("系统登录失败！userName:[{}]",username);
        return 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean register(UserModel userModel) {
        //将注册信息实体转换为数据实体

        CsUser csUser = convertToCsUser(userModel);
        //存入数据库
        Integer insert = csUserMapper.insert(csUser);
        if (insert != 1) {
            log.info("系统注册失败！ userModel:[{}]",JSON.toJSONString(userModel));
            throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
        }
        EntityWrapper<CsUser> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",userModel.getUsername());
        List<CsUser> csUserList = csUserMapper.selectList(entityWrapper);
        if (CollectionUtils.isEmpty(csUserList)) {
            log.info("查询用户失败！用户注册失败！");
            return false;
        }
        CsUser user = csUserList.get(0);
        MtimeUserWallet userWallet = new MtimeUserWallet();
        userWallet.setStatus(UserWalletStatus.NORMAL.getIndex());
        //因为做没有钱包加钱操作，所以给定一个比较大的初始值
        userWallet.setCurrency(new BigDecimal(INIT_MONEY));
        userWallet.setCreateTime(new Date());
        userWallet.setUserId(user.getUuid());
        Integer affectCount = userWalletMapper.insert(userWallet);
        if (affectCount<1) {
            throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
        }
        return Boolean.TRUE;
    }

    private CsUser convertToCsUser(UserModel userModel) {
        CsUser csUser = new CsUser();
        csUser.setUserName(userModel.getUsername());
        csUser.setUserPwd(MD5Util.encrypt(userModel.getPassword()));
        csUser.setAddress(userModel.getAddress());
        csUser.setBeginTime(new Date());
        csUser.setEmail(userModel.getEmail());
        csUser.setUserPhone(userModel.getMobile());
        csUser.setUpdateTime(new Date());
        return csUser;
    }

    @Override
    public Boolean checkUsername(String username) {
        EntityWrapper<CsUser> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",username);

        Integer count = csUserMapper.selectCount(entityWrapper);
        if (count<1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    @Override
    public UserInfoModel getUserInfo(int uuid) {
        CsUser csUser = new CsUser();
        csUser.setUuid(uuid);
        CsUser user = csUserMapper.selectOne(csUser);
        UserInfoModel userInfoModel = convertToUserInfoModel(user);
        return userInfoModel;
    }

    private UserInfoModel convertToUserInfoModel(CsUser user) {
        UserInfoModel model = new UserInfoModel();
        model.setAddress(user.getAddress());
        model.setUsername(user.getUserName());
        model.setUpdateTime(user.getUpdateTime());
        model.setBegainTime(user.getBeginTime());
        model.setNickname(user.getNickName());
        model.setEmail(user.getEmail());
        model.setPhone(user.getUserPhone());
        model.setSex(user.getUserSex());
        model.setBirthday(user.getBirthday());
        model.setLifeState(user.getLifeState()+"");
        model.setBiography(user.getBiography());
        model.setHeadAddress(user.getHeadUrl());
        model.setUuid(user.getUuid());

        return model;
    }

    @Override
    public UserInfoModel update(UserInfoModel userInfoModel) {

        //将传入的数据转换为CsUser
        CsUser user = convertToUserFromUserInfoModel(userInfoModel);

        //入库
        Integer count = csUserMapper.updateById(user);
        if (count> 0) {
            UserInfoModel userInfo = getUserInfo(user.getUuid());
            return userInfo;
        }
        //返回给前端
        return userInfoModel;
    }

    @Override
    @Transactional
    public UserWalletVO updateUserWallet(UserWalletOperateVO walletOperateVO) {

        //验证参数
        processParam(walletOperateVO);

        //新增钱包操作流水
        saveOperateLog(walletOperateVO);
        //操作钱包
        MtimeUserWallet userWallet = OperateWallet(walletOperateVO);
        //组合返回参数
        UserWalletVO userWalletVO = new UserWalletVO();
        userWalletVO.setCurrentCurrency(userWallet.getCurrency());
        userWalletVO.setUserId(walletOperateVO.getUserId());
        userWalletVO.setStatus(userWallet.getStatus());
        return userWalletVO;

    }

    //操作用户钱包
    private MtimeUserWallet OperateWallet(UserWalletOperateVO walletOperateVO) {
        Integer affectedRows = userWalletMapper.walletOperate(walletOperateVO);
        if (affectedRows <= 0) {
            log.info("更新用户钱包失败，walletOperateVO:{}",JSON.toJSONString(walletOperateVO));
            throw new GunsException(GunsExceptionEnum.WALLET_ERROR);
        }
        EntityWrapper entityWrapper = new EntityWrapper<MtimeUserWallet>();
        entityWrapper.eq("user_id",walletOperateVO.getUserId());
        List<MtimeUserWallet> userWalletList = mtimeUserWalletMapper.selectList(entityWrapper);
        if (CollectionUtils.isEmpty(userWalletList) || userWalletList.size()!= 1){
            log.info("回查用户钱包失败！userWalletList:{}, userId:{}",JSON.toJSONString(userWalletList),walletOperateVO.getUserId());
            throw new GunsException(GunsExceptionEnum.WALLET_ERROR);
        }
        return userWalletList.get(0);
    }

    //新增钱包操作流水
    private WalletOperLog saveOperateLog(UserWalletOperateVO walletOperateVO) {

        WalletOperLog log = new WalletOperLog();
        log.setUserId(walletOperateVO.getUserId());
        log.setLogNo(codeCreator.createNo(CodeCreatorPreConstants.WALLET_LOG_NO.getPRE()));
        log.setWalletOperType(walletOperateVO.getWalletOperateType());
        log.setOutOrderNo(walletOperateVO.getOrderId());
        log.setReqAmount(walletOperateVO.getReqAmount());
        log.setCreateTime(new Date());
        Integer affectedRows = walletOperLogMapper.insert(log);
        if (affectedRows != 1) {
            throw new GunsException(GunsExceptionEnum.WALLET_ERROR);
        }
        return log;
    }

    //验证参数
    private void processParam(UserWalletOperateVO walletOperateVO) {

        if (walletOperateVO == null || walletOperateVO.getOrderId() == null || StringUtils.isEmpty(walletOperateVO.getOrderId()) ||
                walletOperateVO.getUserId() == null || walletOperateVO.getWalletOperateType() == null || walletOperateVO.getReqAmount() == null){
            log.info("钱包操作，必要参数不能为空，walletOperateVO:{}",JSON.toJSONString(walletOperateVO));
            throw new GunsException(GunsExceptionEnum.WALLET_ERROR);
        }
        if (walletOperateVO.getWalletOperateType() != WalletOperType.ADD.getIndex() && walletOperateVO.getWalletOperateType() != WalletOperType.SUBSTRACT.getIndex()){
            log.info("钱包操作，操作类型不合法，walletOperateVO:{}",JSON.toJSONString(walletOperateVO));
            throw new GunsException(GunsExceptionEnum.WALLET_ERROR);
        }

        if (walletOperateVO.getReqAmount().longValue() <= 0){
            throw new GunsException(GunsExceptionEnum.WALLET_ERROR);
        }

    }

    private CsUser convertToUserFromUserInfoModel(UserInfoModel userInfoModel) {
        CsUser csUser = new CsUser();
        csUser.setUuid(userInfoModel.getUuid());
        csUser.setUserName(userInfoModel.getUsername());
        csUser.setLifeState(Integer.valueOf(userInfoModel.getLifeState()));
        csUser.setBirthday(userInfoModel.getBirthday());
        csUser.setBiography(userInfoModel.getBiography());
        csUser.setBeginTime(userInfoModel.getBegainTime());
        csUser.setHeadUrl(userInfoModel.getHeadAddress());
        csUser.setEmail(userInfoModel.getEmail());
        csUser.setAddress(userInfoModel.getAddress());
        csUser.setUserPhone(userInfoModel.getPhone());
        csUser.setUserSex(userInfoModel.getSex());
        csUser.setUpdateTime(new Date());
        return csUser;
    }
}