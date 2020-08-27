package com.hydeze.hypad.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hydeze.hypad.dto.LoginDto;
import com.hydeze.hypad.entity.Admin;
import com.hydeze.hypad.service.IAdminService;
import com.hydeze.hypad.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ma
 * @since 2020-08-25
 */
@RestController
@RequestMapping("/hypad")
public class AdminController {

    @Autowired
    IAdminService adminService;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq("username", loginDto.getUsername()));
        if (admin == null)
            return Result.fail("账号不存在");
        if (!admin.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            return Result.fail("密码错误");
        }
        return Result.success("登陆成功", MapUtil.builder()
                .put("id", admin.getId())
                .put("username", admin.getUsername()).map()
        );
    }

}
