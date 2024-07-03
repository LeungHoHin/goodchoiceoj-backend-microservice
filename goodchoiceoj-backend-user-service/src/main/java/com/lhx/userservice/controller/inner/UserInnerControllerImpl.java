package com.lhx.userservice.controller.inner;

import com.lhx.common.common.ErrorCode;
import com.lhx.common.exception.BusinessException;
import com.lhx.model.entity.User;
import com.lhx.model.enums.UserRoleEnum;
import com.lhx.model.vo.UserVO;
import com.lhx.serviceclient.service.UserFeignClient;
import com.lhx.userservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.lhx.common.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/inner")
public class UserInnerControllerImpl implements UserFeignClient {

    @Resource
    private UserService userService;

    @Override
    @GetMapping("/get/{id}")
    public User getById(@RequestParam("userId") @PathVariable("id") long userId) {
        return userService.getById(userId);
    }

    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(Collection<Long> idList) {
        return userService.listByIds(idList);
    }

}
