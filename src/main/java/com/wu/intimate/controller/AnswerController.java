package com.wu.intimate.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.intimate.model.Answer;
import com.wu.intimate.model.Comments;
import com.wu.intimate.model.Answer;
import com.wu.intimate.model.User;
import com.wu.intimate.service.AnswerService;
import com.wu.intimate.service.AnswerService;
import com.wu.intimate.service.QuestionService;
import com.wu.intimate.service.UserService;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/im/answer")
@Api("与回答有关的接口")
public class AnswerController {

    @Autowired
    private AnswerService answerServiceImpl;
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private QuestionService questionServiceImpl;


    /**
     * 分页获取所有的回复信息
     * @param param pageIndex、pageSize
     * @return
     */
    @ApiOperation("分页获取所有的回复信息")
    @PostMapping("/getAnswerList")
    public MyResult getAnswerList(@RequestBody Map<String,String> param){
        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        //分页查询回复信息
        Page<Map<String, Object>> pageParam = new Page<>(pageIndex,pageSize);
        IPage<Map<String, Object>> iPage = answerServiceImpl.getAnswerByOpr(pageParam,param);

        //检索相关信息
        List<Map<String, Object>> answerList = iPage.getRecords();
        for (Map<String, Object> answer : answerList) {
            answer.put("question", questionServiceImpl.getById((int) answer.get("questionId")));
            answer.put("fromUser", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", answer.get("fromId"))));
            answer.put("toUser", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", answer.get("toId"))));
        }
        iPage.setRecords(answerList);
        return MyResult.ok(iPage);
    }

    /**
     * 根据id修改回复
     * @param answer
     * @return
     */
//    @ApiOperation("根据评论id回复问题")
//    @PostMapping("/editAnswerById")
//    public MyResult editAnswerById(@RequestBody Answer answer){
//        boolean flag = answerServiceImpl.updateById(answer);
//        return flag ? MyResult.ok() : MyResult.fail();
//    }

    /**
     * 根据id通过审核
     * @param answerId
     * @return
     */
    @ApiOperation("根据评论id通过审核")
    @GetMapping("/editAnswerById/{answerId}")
    public MyResult editAnswerById(@PathVariable("answerId") Integer answerId){
        Answer answer = answerServiceImpl.getById(answerId);
        answer.setStatus(answer.getStatus() == 1 ? 0 : 1);
        boolean flag = answerServiceImpl.updateById(answer);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据评论id批量删除回复数据
     * @param ids
     * @return
     */
    @ApiOperation("根据评论id数组删除回复数据")
    @PostMapping("/deleteAnswerByIds")
    public MyResult deleteAnswerByIds(@RequestBody List<Integer> ids){
        boolean flag = answerServiceImpl.removeByIds(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据问题id获取回答数据
     * @param questionId
     * @return
     */
//    @ApiOperation("根据问题id获取回答数据")
//    @GetMapping("/getAnswerList")
//    public MyResult getAnswerList(String questionId){
//        //获取回答
//        List<Map<String, Object>> answerList = answerServiceImpl.listMaps(
//                new QueryWrapper<Answer>()
//                        .eq("questionId", questionId));
//
//        //添加用户信息
//        for(Map<String, Object> map : answerList){
//            map.put("fromUser", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", map.get("fromId"))));
//            map.put("toUser", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", map.get("toId"))));
//        }
//        return MyResult.ok(answerList);
//    }

    /**
     * qId 问题id
     * content 评论内容
     * fromId 评论者id
     * date 评论时间
     *
     * toId 如果是回复，则传递回复对象的id
     * answerId 如果是回复，传递所属主评论的id
     * @return
     */
    @ApiOperation("发表回答")
    @PostMapping("/sendAnswer")
    public MyResult sendComment(@RequestBody Map<String, String> params) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = answerServiceImpl.save(new Answer(null, Integer.parseInt(params.get("qId")),
                params.get("content"), Integer.parseInt(params.get("fromId")), 0, new Date(sdf.parse(params.get("date")).getTime()), 0, 1));
        return flag ? MyResult.ok() : MyResult.fail();
    }
}
