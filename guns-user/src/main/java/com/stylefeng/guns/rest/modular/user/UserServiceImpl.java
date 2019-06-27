package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @date 2019/5/9 20:50
 */
@Component
@Service(interfaceClass = UserAPI.class,loadbalance = "roundrobin")
public class UserServiceImpl implements UserAPI {

    @Autowired
    private MoocUserTMapper moocUserTMapper;

    /**
     * 注册
     * @param userModel
     * @return
     */
    @Override
    public boolean register(UserModel userModel) {
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setUserPhone(userModel.getPhone());
        moocUserT.setAddress(userModel.getAddress());
        //数据加密(MD5混淆加密 + 盐值)
        String md5password = MD5Util.encrypt(userModel.getPassword());
        moocUserT.setUserPwd(md5password);
        //将数据实体入库
        Integer effectedNum = moocUserTMapper.insert(moocUserT);
        if (effectedNum > 0){
            return true;
        }
        return false;
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public int login(String userName, String password) {
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userName);
        MoocUserT result = moocUserTMapper.selectOne(moocUserT);
        if (result!=null && result.getUuid()>0){
            String md5Password = MD5Util.encrypt(password);
            if (md5Password.equals(result.getUserPwd())){
                return result.getUuid();
            }
        }
        return 0;
    }


    /**
     * 检查用户名
     * @param username
     * @return
     */
    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<MoocUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",username);
        Integer count = moocUserTMapper.selectCount(entityWrapper);
        if (count!=null&&count>0){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 查询用户信息
     * @param uuid
     * @return
     */
    @Override
    public UserInfoModel getUserInfo(int uuid) {
        MoocUserT moocUserT = moocUserTMapper.selectById(uuid);
        UserInfoModel userInfoModel = convert2UserInfo(moocUserT);
        return userInfoModel;
    }

    /**
     * 修改用户信息
     * @param userInfoModel
     * @return
     */
    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setNickName(userInfoModel.getNickname());
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setUserPhone(userInfoModel.getPhone());
        moocUserT.setUserSex(userInfoModel.getSex());
        moocUserT.setBirthday(userInfoModel.getBirthday());
        if (StringUtils.isNotBlank(userInfoModel.getLifeState())){
            moocUserT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        }
        moocUserT.setBiography(userInfoModel.getBiography());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setUpdateTime(time2Date(System.currentTimeMillis()));
        //更新
        Integer result = moocUserTMapper.updateById(moocUserT);
        if (result!=null&&result>0){
            UserInfoModel userInfo = getUserInfo(moocUserT.getUuid());
            return userInfo;
        }else {
            return userInfoModel;
        }

    }

    private Date time2Date(long time){
        Date date = new Date(time);
        return date;
    }

    /**
     *  MoocUserT转换为UserInfoModel
     * @param moocUserT
     * @return
     */
    private UserInfoModel convert2UserInfo(MoocUserT moocUserT){
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUsername(moocUserT.getUserName());
        userInfoModel.setNickname(moocUserT.getNickName());
        userInfoModel.setEmail(moocUserT.getEmail());
        userInfoModel.setPhone(moocUserT.getUserPhone());
        userInfoModel.setSex(moocUserT.getUserSex());
        userInfoModel.setBirthday(moocUserT.getBirthday());
        userInfoModel.setLifeState("" + moocUserT.getLifeState());
        userInfoModel.setBiography(moocUserT.getBiography());
        userInfoModel.setAddress(moocUserT.getAddress());
        userInfoModel.setHeadAddress(moocUserT.getHeadUrl());
        userInfoModel.setCreateTime(moocUserT.getBeginTime().getTime());
        userInfoModel.setUpdateTime(moocUserT.getUpdateTime().getTime());
        userInfoModel.setUuid(moocUserT.getUuid());
        return  userInfoModel;
    }
}
