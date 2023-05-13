package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.CommentsMapper;
import com.wu.intimate.mapper.EssayMapper;
import com.wu.intimate.mapper.UserMapper;
import com.wu.intimate.model.Comments;
import com.wu.intimate.model.Essay;
import com.wu.intimate.model.Comments;
import com.wu.intimate.model.User;
import com.wu.intimate.service.CommentsService;
import com.wu.intimate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EssayMapper essayMapper;

    @Override
    public IPage<Map<String, Object>> getCommentsByOpr(Page<Map<String, Object>> pageParam, Map<String, String> param) {
        System.out.println("查询条件"+param);
        String status = param.get("status");
        String isReply = param.get("isReply");
        String fromName = param.get("fromName");
        String essayTitle = param.get("essayTitle");
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();


        if(!StringUtils.isNullOrEmpty(status))
            queryWrapper.eq("status",status);
        if(!StringUtils.isNullOrEmpty(isReply)) {
            if (isReply.equals("0")) {
                queryWrapper.eq("replyId", 0);
            } else {
                queryWrapper.ne("replyId", 0);
            }
        }
        if(!StringUtils.isNullOrEmpty(fromName)){
            List<User> userList = userMapper.selectList(new QueryWrapper<User>().like("nickname", fromName));
            //根据id条件拼接查询
            for (int i = 0; i < userList.size(); i++){
                //条件拼接
                int id = userList.get(i).getId();
                if(i == 0){
                    queryWrapper.eq("fromId",id);
                }else{
                    queryWrapper.or(qw1 -> qw1.eq("fromId",id));
                }
            }
        }
        if(!StringUtils.isNullOrEmpty(essayTitle)){
            List<Essay> essayList = essayMapper.selectList(new QueryWrapper<Essay>().like("title", essayTitle));
            //根据id条件拼接查询
            for (int i = 0; i < essayList.size(); i++){
                //条件拼接
                int id = essayList.get(i).getId();
                if(i == 0){
                    queryWrapper.eq("essayId",id);
                }else{
                    queryWrapper.or(qw1 -> qw1.eq("essayId",id));
                }
            }
        }

        Page<Map<String, Object>> page = baseMapper.selectMapsPage(pageParam,queryWrapper);
        return page;
    }

    public Boolean deleteCommentsByIds(List<Integer> ids) {
        System.out.println("待删除文章id："+ids);
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        //根据id条件拼接查询
        for (int i = 0; i < ids.size(); i++){
            //条件拼接
            int id = ids.get(i);
            if(i == 0){
                queryWrapper.eq("essayId",id);
            }else{
                queryWrapper.or(qw1 -> qw1.eq("essayId",id));
            }
        }
        return baseMapper.delete(queryWrapper) != -1;
    }
}
