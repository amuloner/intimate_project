package com.wu.intimate.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.intimate.model.Comments;
import com.wu.intimate.model.Essay;
import com.wu.intimate.model.User;
import com.wu.intimate.service.CommentsService;
import com.wu.intimate.service.EssayService;
import com.wu.intimate.service.UserService;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/im/comments")
@Api("与评论有关的接口")
@Transactional
public class CommentsController {
    @Autowired
    private CommentsService commentsServiceImpl;
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private EssayService essayServiceImpl;

    /**
     * 分页获取所有的评论信息
     * @param param 是否通过审核status（0|1） 评论类型isReply（0|1）、评论人fromName、文章essayTitle、pageIndex、pageSize
     * @return  json字符串id、essayTitle、fromName、toName、content、date、status
     */
    @ApiOperation("分页获取所有的评论信息")
    @PostMapping("/getAllComments")
    public MyResult getAllComments(@RequestBody Map<String,String> param){
        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        //分页查询评论信息
        Page<Map<String, Object>> pageParam = new Page<>(pageIndex,pageSize);
        IPage<Map<String, Object>> iPage = commentsServiceImpl.getCommentsByOpr(pageParam,param);

        //检索相关文章信息
        List<Map<String, Object>> commentsList = iPage.getRecords();
        for (Map<String, Object> comments : commentsList) {
            comments.put("essay", essayServiceImpl.getById((int) comments.get("essayId")));
            comments.put("fromUser", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", comments.get("fromId"))));
            comments.put("toUser", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", comments.get("toId"))));
        }
        iPage.setRecords(commentsList);
        return MyResult.ok(iPage);
    }

    /**
     * 根据文章id获取评论数组
     * 评论分两种：1.关于文章的评论，2.对评论的回复
     * @param essayId
     * @return
     */
    @ApiOperation("根据文章id获取评论信息")
    @GetMapping("/getEssayCom")
    public MyResult getCommentListByEssayId(String essayId){
        //获取关于文章的评论，文章id
        List<Map<String, Object>> comMapList = commentsServiceImpl.listMaps(
                new QueryWrapper<Comments>()
                        .eq("essayId", essayId));

        //从所有评论中分离一级评论、评论回复，添加用户信息
        List<Map<String, Object>> resultComList = new ArrayList<>();
        List<Map<String, Object>> replyList = new ArrayList<>();
        List<Map<String, Object>> list;
        for(Map<String, Object> map : comMapList){
            map.put("fromUser", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", map.get("fromId"))));
            map.put("toUser", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", map.get("toId"))));
            if (map.get("replyId").equals(0)) {
                resultComList.add(map);
            } else {
                replyList.add(map);
            }
        }

        //遍历添加用户信息、对评论的回复
        for(Map<String, Object> map : resultComList){
            list = new ArrayList<>();
            for (Map<String, Object> map2 : replyList) {
                if(map2.get("replyId").equals(map.get("id"))){
                    list.add(map2);
                }
            }
            map.put("replyList", list);
        }
        return MyResult.ok(resultComList);
    }

    /**
     * essayId 文章id
     * content 评论内容
     * fromId 评论者id
     * toId 如果是回复，则传递回复对象的id
     * date 评论时间
     * replyId 如果是回复，传递所属主评论的id
     * @return
     */
    @ApiOperation("发表评论")
    @PostMapping("/sendComment")
    public MyResult sendComment(@RequestBody Map<String, String> params) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = commentsServiceImpl.save(new Comments(null, Integer.parseInt(params.get("essayId")),
                params.get("content"), Integer.parseInt(params.get("fromId")), Integer.parseInt(params.get("toId")),
                1, new Date(sdf.parse(params.get("date")).getTime()), Integer.parseInt(params.get("replyId"))));
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据id通过审核
     * @param commentsId
     * @return
     */
    @ApiOperation("根据评论id通过审核")
    @GetMapping("/editCommentsById/{commentsId}")
    public MyResult editCommentsById(@PathVariable("commentsId") Integer commentsId){
        Comments comments = commentsServiceImpl.getById(commentsId);
        comments.setStatus(comments.getStatus() == 1 ? 0 : 1);
        boolean flag = commentsServiceImpl.updateById(comments);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据评论id批量删除评论数据
     * @param ids
     * @return
     */
    @ApiOperation("根据评论id数组删除评论数据")
    @PostMapping("/deleteCommentsByIds")
    public MyResult deleteCommentsByIds(@RequestBody List<Integer> ids){
        boolean flag = commentsServiceImpl.removeByIds(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }
}
