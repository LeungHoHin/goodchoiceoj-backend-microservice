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

/**
 * 用户服务
 *
 * @author 梁浩轩
 */

@FeignClient(value = "goodchoiceoj-backend-user-service")
public interface UserFeignClient {

    @GetMapping("/inner/get/{id}")
    User getById(@RequestParam("userId") @PathVariable("id") long userId);


    @GetMapping("/inner/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);


    @GetMapping("/inner/get/current_user")
    User getCurrentUser(HttpServletRequest request);

    @GetMapping("/inner/is_admin")
    boolean isAdmin(User user);

    @GetMapping("/inner/get/userVO")
    UserVO getUserVO(User user);


}
