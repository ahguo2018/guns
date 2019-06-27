package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2019/6/4 21:21
 */
@RestController
@RequestMapping("/user")
public class UserController {
    //Dubbo特性:启动检查
    /**
     * Dubbo 缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止 Spring 初始化完成，以便上线时，
     * 能及早发现问题，默认 check="true"。
     * 可以通过 check="false" 关闭检查，比如，测试时，有些服务不关心，或者出现了循环依赖，必须有一方先启动。
     *
     */
    @Reference(interfaceClass = UserAPI.class,check = false)
    private UserAPI userAPI;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResponseVO register(UserModel userModel){
        if (StringUtils.isBlank(userModel.getUsername())){
            return ResponseVO.serviceFail("用户名不能为空");
        }
        if (StringUtils.isBlank(userModel.getPassword())){
            return ResponseVO.serviceFail("密码不能为空");
        }
        boolean isSuccess = userAPI.register(userModel);
        if (isSuccess){
            return ResponseVO.success("注册成功");
        }else {
            return ResponseVO.success("注册失败");
        }
    }


    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public ResponseVO check(String username){
        if (StringUtils.isNotBlank(username)){
            boolean checkResult = userAPI.checkUsername(username);
            if (checkResult){
                return ResponseVO.success("用户名不存在");
            }else {
                return ResponseVO.serviceFail("用户名已存在");
            }
        }else {
            return ResponseVO.serviceFail("用户名不能为空");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ResponseVO logout(String username){
        /*
            应用：
                1、前端存储JWT 【七天】 ： JWT的刷新
                2、服务器端会存储活动用户信息【30分钟】
                3、JWT里的userId为key，查找活跃用户
            退出：
                1、前端删除掉JWT
                2、后端服务器删除活跃用户缓存
            现状：
                1、前端删除掉JWT
         */
        return ResponseVO.success("用户退出成功");
    }

    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    public ResponseVO getUserInfo(){
        String uuid = CurrentUser.getCurrentUser();
        if (StringUtils.isNotBlank(uuid)){
            UserInfoModel userInfo = userAPI.getUserInfo(Integer.parseInt(uuid));
            if (userInfo==null){
                return ResponseVO.serviceFail("用户信息查询失败");
            }else {
                return ResponseVO.success(userInfo);
            }
        }else {
            return ResponseVO.serviceFail("用户未登录");
        }
    }

    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    public ResponseVO updateUserInfo(UserInfoModel userInfoModel){
        String uuid = CurrentUser.getCurrentUser();
        if (StringUtils.isNotBlank(uuid)){
            if (uuid.equals(userInfoModel.getUuid())){
                return ResponseVO.serviceFail("请修改你的个人信息");
            }
            UserInfoModel userInfo = userAPI.updateUserInfo(userInfoModel);
            if (userInfo==null){
                return ResponseVO.serviceFail("用户信息修改失败");
            }else {
                return ResponseVO.success(userInfo);
            }
        }else {
            return ResponseVO.serviceFail("用户未登录");
        }
    }
}
