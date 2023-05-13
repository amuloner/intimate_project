package com.wu.intimate.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.intimate.model.*;
import com.wu.intimate.service.AnswerService;
import com.wu.intimate.service.QStarService;
import com.wu.intimate.service.QuestionService;
import com.wu.intimate.service.UserService;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/im/question")
@Api("与提问有关的接口")
public class QuestionController {

    @Autowired
    private QuestionService questionServiceImpl;
    @Autowired
    private AnswerService answerServiceImpl;
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private QStarService qStarServiceImpl;

    /**
     * 分页获取所有的提问信息
     * @param param pageIndex、pageSize、username、title
     * @return
     */
    @ApiOperation("分页获取所有的提问信息")
    @PostMapping("/getQuestionList")
    public MyResult getQuestionList(@RequestBody Map<String,String> param){
        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        Page<Map<String,Object>> pageParam = new Page<>(pageIndex,pageSize);
        IPage<Map<String,Object>> iPage = questionServiceImpl.getQuestionByOpr(pageParam,param);

        //查询回答数量、用户信息
        for (Map<String, Object>  map: iPage.getRecords()) {
            map.put("answerNum",answerServiceImpl.count(new QueryWrapper<Answer>().eq("questionId",map.get("id"))));
            map.put("userinfo",userServiceImpl.getById((Serializable) map.get("userId")));
        }
        return MyResult.ok(iPage);
    }

    /**
     * 根据id修改问题
     * @param question
     * @return
     */
    @ApiOperation("根据评论id修改问题")
    @PostMapping("/editQuestionById")
    public MyResult editQuestionById(@RequestBody Question question){
        boolean flag = questionServiceImpl.updateById(question);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据评论id批量删除问题数据
     * @param ids
     * @return
     */
    @ApiOperation("根据评论id数组删除问题数据")
    @PostMapping("/deleteQuestionByIds")
    public MyResult deleteQuestionByIds(@RequestBody List<Integer> ids){
        boolean flag = questionServiceImpl.removeByIds(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    @ApiOperation("根据问题id获取用户的点赞")
    @GetMapping("/getUserStar/{qId}/{userId}")
    public MyResult getUserStar(@PathVariable("qId") String qId, @PathVariable("userId") String userId){
        QStar qStar = qStarServiceImpl.getOne(new QueryWrapper<QStar>().eq("qId", Integer.parseInt(qId)).eq("userId", Integer.parseInt(userId)));
        return MyResult.ok(qStar);
    }

    @ApiOperation("根据问题id保存用户点赞")
    @GetMapping("/saveUserStar/{qId}/{userId}/{type}")
    public MyResult getUserStar(@PathVariable("qId") String qId, @PathVariable("userId") String userId, @PathVariable("type") String type){
        QStar qStar = qStarServiceImpl.getOne(new QueryWrapper<QStar>().eq("qId", Integer.parseInt(qId)).eq("userId", Integer.parseInt(userId)));
        if(qStar == null){
            qStar = new QStar(null, Integer.parseInt(qId), Integer.parseInt(userId), 0, 0);
        }
        Question question = questionServiceImpl.getById(qId);
        if(type.equals("hug")){
            qStar.setIsHug(1);
            question.setHugNum(question.getHugNum() + 1);
        }
        if(type.equals("same")){
            qStar.setIsSame(1);
            question.setSameNum(question.getSameNum() + 1);
        }
        return qStarServiceImpl.saveOrUpdate(qStar) && questionServiceImpl.updateById(question) ? MyResult.ok() : MyResult.fail();
    }

    @ApiOperation("新增提问")
    @PostMapping("/saveQuestion")
    public MyResult saveQuestion(@RequestBody Map<String, String> params) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = questionServiceImpl.save(new Question(null, Integer.parseInt(params.get("userId")), params.get("title"),
                params.get("content"), new Date(sdf.parse(params.get("date")).getTime()), 0, 0));
        return flag ? MyResult.ok() : MyResult.fail();
    }
}
