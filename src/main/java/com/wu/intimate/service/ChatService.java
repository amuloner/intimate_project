package com.wu.intimate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.intimate.model.Chat;
import com.wu.intimate.model.TestRule;

import java.util.Map;

public interface ChatService extends IService<Chat> {

    IPage<Chat> getChatByOpr(Page<Chat> pageParam, Map<String, String> param);

    Chat getLastChat(Integer fromId, Integer toId);
}
