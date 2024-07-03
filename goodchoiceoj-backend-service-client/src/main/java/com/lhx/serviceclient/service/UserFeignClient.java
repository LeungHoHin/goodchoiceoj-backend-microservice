package com.lhx.serviceclient.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lhx.common.annotation.AuthCheck;
import com.lhx.common.common.BaseResponse;
import com.lhx.common.common.DeleteRequest;
import com.lhx.common.common.ErrorCode;
import com.lhx.common.common.ResultUtils;
import com.lhx.common.constant.UserConstant;
import com.lhx.common.exception.BusinessException;
import com.lhx.common.exception.ThrowUtils;
import com.lhx.model.dto.user.*;
import com.lhx.model.entity.User;
import com.lhx.model.enums.UserRoleEnum;
import com.lhx.model.vo.LoginUserVO;
import com.lhx.model.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.lhx.common.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author 梁浩轩
 */

@FeignClient(value = "goodchoiceoj-backend-user-service")
public interface UserFeignClient {

    @GetMapping("/user/inner/get/{id}")
    User getById(@RequestParam("userId") @PathVariable("id") long userId);


    @GetMapping("/user/inner/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);




    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getCurrentUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 可以考虑在这里做全局权限校验
        return currentUser;
    }

    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }


    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }


}
