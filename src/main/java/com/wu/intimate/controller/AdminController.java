package com.wu.intimate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wu.intimate.model.Admin;
import com.wu.intimate.model.LoginForm;
import com.wu.intimate.service.AdminService;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/im/admin")
@Api(value = "管理员登录控制")
public class AdminController {
    @Autowired
    private AdminService adminServiceImpl;

    /**
     * 登录校验
     * username
     * password
     * @return 返回类型定义为Result，中间进行转换
     */
    @ApiOperation(value = "管理员登录")
    @PostMapping("/toLogin")
    public MyResult login(@RequestBody Map< String, String> params){
        System.out.println("请求登录");
        Admin admin = adminServiceImpl.getOne(new QueryWrapper<Admin>().eq("username", params.get("username")).eq("password", params.get("password")));
        return admin != null ? MyResult.ok(admin) : MyResult.fail();
    }

    /**
     * 密码修改
     * newPass
     * @return
     */
    @ApiOperation(value = "管理员登录")
    @PostMapping("/editAdmin")
    public MyResult editAdmin(@RequestBody Map< String, String> params){
        System.out.println("请求修改");
        Admin admin = new Admin();
        admin.setId(Integer.valueOf(params.get("id")));
        admin.setPassword(params.get("newPass"));

        boolean b = adminServiceImpl.updateById(admin);
        return b ? MyResult.ok() : MyResult.fail();
    }

}
