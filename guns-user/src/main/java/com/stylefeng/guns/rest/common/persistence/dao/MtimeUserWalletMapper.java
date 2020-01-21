package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.user.vo.UserWalletOperateVO;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserWallet;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ciggar
 * @since 2019-08-05
 */
public interface MtimeUserWalletMapper extends BaseMapper<MtimeUserWallet> {

    //操作钱包接口
    Integer walletOperate(@Param(value = "walletOperateVO") UserWalletOperateVO walletOperateVO);
}
