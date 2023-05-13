package com.wu.intimate.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.ChatMapper;
import com.wu.intimate.model.Chat;
import com.wu.intimate.service.ChatService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Override
    public IPage<Chat> getChatByOpr(Page<Chat> pageParam, Map<String, String> param) {

        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(param.get("chatId")))//根据chat_id查询信息数据
            queryWrapper.eq("chatId",param.get("chatId"));
        if(!StringUtils.isNullOrEmpty(param.get("chatId2")))//根据chat_id查询信息数据
            queryWrapper.or().eq("chatId",param.get("chatId2"));
        queryWrapper.orderByDesc("ctime");//时间戳降序

        IPage<Chat> chatPage = baseMapper.selectPage(pageParam, queryWrapper);

        //对数组进行重编
        List<Chat> chats = chatPage.getRecords();
        ArrayList<Chat> resultList = new ArrayList<>();//新数组
        int lastId = Integer.parseInt(param.get("lastId"));//记录待寻找的消息id
        //遍历
        while (!(resultList.size() == chats.size())){
            Chat chat = null;
            int finalLastId = lastId;
            if(pageParam.getCurrent() == 1 && resultList.size() == 0){//如果是第一次查询且当前结果中为0，先获取最后一条消息
                chat = chats.stream().filter(c -> c.getIslast() == 1).findAny().orElse(null);
                System.out.println("遍历完的chat："+chat);
            }else{//寻找上一条消息
                chat = chats.stream().filter(c -> c.getId() == finalLastId).findAny().orElse(null);
            }
            assert chat != null;
            resultList.add(0, chat);
            //更新对应的上一条信息id
            lastId = chat.getLastChat();
        }
        chatPage.setRecords(resultList);
        return chatPage;
    }

    @Override
    public Chat getLastChat(Integer fromId, Integer toId) {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(Wrapper -> Wrapper.eq("fromId" , fromId).or().eq("fromId", toId));
        queryWrapper.and(Wrapper -> Wrapper.eq("toId", toId).or().eq("toId", fromId));
        queryWrapper.eq("islast", 1);
        return baseMapper.selectOne(queryWrapper);
    }

}
