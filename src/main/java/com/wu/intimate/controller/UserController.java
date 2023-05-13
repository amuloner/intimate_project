package com.wu.intimate.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.intimate.model.Comments;
import com.wu.intimate.model.Consultant;
import com.wu.intimate.model.Essay;
import com.wu.intimate.model.User;
import com.wu.intimate.service.*;
import com.wu.intimate.utils.MyResult;
import com.wu.intimate.utils.RestTemplateHttp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/im/user")
@Api("与用户有关的接口")
public class UserController {

    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private EssayService essayServiceImpl;
    @Autowired
    private CommentsService commentsServiceImpl;
    @Autowired
    private ConsultantService consultantServiceImpl;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 微信用户登录
     * @return
     */
    @ApiOperation("微信用户登录")
    @PostMapping("/toLogin")
    public MyResult toLogin(@RequestBody Map<String, String> params){
        String secret = params.get("secret");
        //根据临时code 获取微信的openID
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid="+ params.get("appID") + "&secret=" + secret + "&js_code=" + params.get("code") + "&grant_type=authorization_code";
        Map map = RestTemplateHttp.get(restTemplate, url);
        //根据openID查询用户数据
        Map<String, String> user = userServiceImpl.getUser(new QueryWrapper<Object>().eq("wechat", map.get("openid")));
        return user == null ? MyResult.fail(map) : MyResult.ok(user);
    }

    /**
     * 微信用户注册
     * @return
     */
    @ApiOperation("微信用户注册")
    @PostMapping("/toRegist")
    public MyResult toRegist(@RequestBody Map<String, String> params){
        System.out.println(params);
        User user = new User();
        user.setHeadImg(params.get("avatarUrl"));
        user.setNickname(params.get("nickName"));
        user.setWechat(params.get("openID"));
        user.setDate(Date.valueOf(LocalDate.now()));
        return userServiceImpl.save(user) ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 获取所有的用户信息
     * @return
     */
    @ApiOperation("分页获取所有的用户信息")
    @PostMapping("/getAllUser")
    public MyResult getAllUser(@RequestBody Map<String,String> param){
        //param包括authority、username、pageIndex、pageSize
        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        Page<User> pageParam = new Page<>(pageIndex,pageSize);
        IPage<User> iPage = userServiceImpl.getUserByOpr(pageParam,param);
        return MyResult.ok(iPage);
    }

    /**
     * 根据id更新用户信息
     * @param user
     * @return
     */
    @ApiOperation("根据用户id修改用户数据")
    @PostMapping("/editUserById")
    public MyResult editUserById(@RequestBody User user){
        boolean flag = userServiceImpl.updateById(user);
        User newUser = userServiceImpl.getUserById(user.getId());
        return flag ? MyResult.ok(newUser) : MyResult.fail();
    }


    /**
     * 根据用户id批量删除用户数据
     * @param ids
     * @return
     */
    @ApiOperation("根据用户id数组删除用户数据")
    @PostMapping("/deleteUserByIds")
    @Transactional
    public MyResult deleteUserByIds(@RequestBody List<Integer> ids){
        boolean flag = userServiceImpl.removeByIds(ids);
        boolean remove = consultantServiceImpl.removeByIds(ids);
        return MyResult.ok();
    }

    /**
     * 分页获取咨询师的数据
     * @param param
     * @return
     */
    @ApiOperation("分页获取所有的咨询师数据")
    @PostMapping("/getAllCon")
    public MyResult getAllCon(@RequestBody Map<String,String> param){
        //从user表中查询基本信息和咨询师相关数据
        //根据id查询出每个咨询师的文章总点赞数量
        //param包括username、pageIndex、pageSize
        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));
        Page<Map<String, String>> pageParam = new Page<>(pageIndex,pageSize);
        IPage<Map<String, String>> iPage = consultantServiceImpl.getConsultantByOpr(pageParam,param);
        //为每个con对象添加essayLikes
//        for(Consultant c : iPage.getRecords()){
//            c.setEssayLikes(essayServiceImpl.getEssayNumByUserId(c.getId()));
//        }
        return MyResult.ok(iPage);
    }




}
