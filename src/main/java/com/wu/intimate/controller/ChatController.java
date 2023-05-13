package com.wu.intimate.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.model.Chat;
import com.wu.intimate.model.ChatList;
import com.wu.intimate.model.User;
import com.wu.intimate.service.ChatListService;
import com.wu.intimate.service.ChatService;
import com.wu.intimate.service.UserService;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/im/chat")
@Api("消息相关接口")
public class ChatController {
    @Autowired
    private ChatService chatServiceImpl;
    @Autowired
    private ChatListService chatListServiceImpl;
    @Autowired
    private UserService userServiceImpl;


    /**
     * 获取部分聊天数据
     * @param param pageIndex、pageSize、lastId、fromId、toId
     * @return
     */
    @ApiOperation("分页获取聊天数据")
    @PostMapping("/getChat")
    public MyResult getChat(@ApiParam("chatId、pageIndex、pageSize、lastId") @RequestBody Map<String,String> param){

        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        if(String.valueOf(pageIndex).equals(""))
            pageIndex = 1;
        if(String.valueOf(pageSize).equals(""))
            pageIndex = 10;

        //获取聊天对象的chatId
        ChatList chatListB = chatListServiceImpl.getOne(new QueryWrapper<ChatList>().eq("fromId", param.get("toId")).eq("toId", param.get("fromId")));
        if(chatListB != null)
            param.put("chatId2", String.valueOf(chatListB.getId()));

        //根据chatId获取到一部分评论数据，从评论数据中找到最后一次消息，根据lastChat栈入map
        Page<Chat> pageParam = new Page<>(pageIndex,pageSize);
        IPage<Chat> iPage = chatServiceImpl.getChatByOpr(pageParam,param);
        return MyResult.ok(iPage);
    }

    /**
     * 消息列表获取
     * @param params type(获取有未读信息 或 获取无未读信息)、userId
     * @return
     */
    @ApiOperation("消息列表获取")
    @PostMapping("/getChatList")
    public MyResult getChatList(@RequestBody Map<String, String> params){
        String type = params.get("type");
        int fromId = Integer.parseInt(params.get("userId"));

        QueryWrapper<ChatList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fromId", fromId);
        if (!StringUtils.isNullOrEmpty(type)){
            if(type.equals("未读")){
                queryWrapper.gt("unread", 0);//未读消息大于0
            }
            else if(type.equals("已读"))
                queryWrapper.eq("unread", 0);//未读数等于0
        }
        List<Map<String, Object>> chatLists = chatListServiceImpl.listMaps(queryWrapper);

        //为每个消息列表查询最后一条消息的信息
        for (Map<String, Object> chatList : chatLists) {
            //根据fromId、toId获取相关聊天记录中的最后一条
            Chat lastChat = chatServiceImpl.getLastChat((int)chatList.get("fromId"), (int)chatList.get("toId"));
            chatList.put("lastChat", lastChat);
        }
        return MyResult.ok(chatLists);
    }


    /**
     * 消息发送
     * @param params fromId、toId、content、lastChat
     * @return
     */
    @ApiOperation("消息发送")
    @PostMapping("/sendChat")
    @Transactional
    public MyResult sendChat(@RequestBody Map<String, String> params){
        System.out.println(params);
        int fromId = Integer.parseInt(params.get("fromId"));
        int toId = Integer.parseInt(params.get("toId"));
        String content = params.get("content");
        int lastChat = Integer.parseInt(params.get("lastChat"));//当前的最后一条消息的id
        String ctime = params.get("ctime");

        ChatList chatListA = chatListServiceImpl.getOne(new QueryWrapper<ChatList>().eq("fromId", fromId).eq("toId", toId));
        //验证是否存在双方消息列表  chatList，若存在使用id，不存在添加两列数据后返回当前chatId，添加时未读数为1
        if(chatListA == null){
            //添加A对B
            chatListA = new ChatList();
            User userB = userServiceImpl.getById(toId);
            chatListA.setFromId(fromId);
            chatListA.setToId(toId);
            chatListA.setToHead(userB.getHeadImg());
            chatListA.setToName(userB.getNickname());

            //添加B对A
            ChatList chatListB = new ChatList();
            User userA = userServiceImpl.getById(fromId);
            chatListB.setFromId(toId);
            chatListB.setToId(fromId);
            chatListB.setToHead(userA.getHeadImg());
            chatListB.setToName(userA.getNickname());
            chatListB.setIs_online(1);//A用户当前在线
            chatListB.setUnread(1);//A发的消息，B那边应该未读

            ChatList finalChatListA = chatListA;//浅拷贝
            chatListServiceImpl.saveBatch(new ArrayList<ChatList>(){{add(finalChatListA); add(chatListB);}});//插入数据后将会把id映射回来
            //新建消息列表，数据库中无双方历史消息，无须进行修改业务，直接添加
        }else{
            //存在消息列表 取消该A的chatId 的所有消息中的最后一条标识，即id为lastChat的标识
            chatServiceImpl.updateById(new Chat(lastChat,0));

            //B那边的未读加一
            ChatList chatListB = (ChatList) this.getChatListById(fromId, toId).getData();
            chatListB.setUnread(chatListB.getUnread() + 1);
            chatListServiceImpl.updateById(chatListB);
        }

        // 根据A的chatId添加消息，设定状态为已发送（1），设置为最后一条
        Chat chat = new Chat();
        chat.setChatId(chatListA.getId());
        chat.setFromId(fromId);
        chat.setToId(toId);
        chat.setContent(content);
        chat.setStatus(1);
        chat.setLastChat(lastChat);//设置该消息的上一条消息id
        chat.setIslast(1);//标识为最后一条
        chat.setCtime(ctime);
        return chatServiceImpl.save(chat) ? MyResult.ok(chatListA) : MyResult.fail();
    }

    /**
     * 修改消息状态
     * @param params 需要修改的消息idList、chatId
     * @return
     */
    @ApiOperation("消息修改")
    @PostMapping("/editChat")
    public MyResult editChat(@RequestBody Map<String, String> params){

        //修改状态，去除未读数
        JSONArray idList = JSONObject.parseArray(params.get("idList"));
        List<Chat> list = new ArrayList<>();
        for (Object id : idList) {
            Chat chat = new Chat();
            chat.setId((Integer) id);
            chat.setStatus(2);
            list.add(chat);
        }

        ChatList chatList = new ChatList();
        chatList.setId(Integer.valueOf(params.get("chatId")));
        chatList.setUnread(0);

        return chatListServiceImpl.updateById(chatList) && chatServiceImpl.updateBatchById(list) ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据fromId、toId查询chatList
     * @param fromId
     * @param toId
     * @return
     */
    @ApiOperation("根据用户id、聊天对象id查询消息列表")
    @GetMapping("/getChatListById")
    public MyResult getChatListById(@RequestParam("fromId") Integer fromId,@RequestParam("toId") Integer toId){
        ChatList chatList = chatListServiceImpl.getOne(new QueryWrapper<ChatList>().eq("fromId", fromId).eq("toId", toId));
        if(chatList != null){
            return MyResult.ok(chatList);
        }
        return MyResult.fail();
    }
}
