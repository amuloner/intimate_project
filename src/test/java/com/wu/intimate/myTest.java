package com.wu.intimate;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.AdminMapper;
import com.wu.intimate.mapper.ConsultantMapper;
import com.wu.intimate.mapper.EssayMapper;
import com.wu.intimate.mapper.UserMapper;
import com.wu.intimate.model.*;
import com.wu.intimate.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class myTest {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EssayMapper essayMapper;
    @Autowired
    private EssayService essayServiceImpl;
    @Autowired
    private ConsultantMapper consultantMapper;
    @Autowired
    private ConsultantService cosultantServiceImpl;
    @Autowired
    private TestRuleService testRuleServiceImpl;
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private ChatService chatServiceImpl;
    @Autowired
    private TestQuesService testQuesServiceImpl;

    @Test
    public void HelloController() {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chatId",15);
        queryWrapper.orderByAsc("ctime");//时间戳降序

        Page<Chat> pageParam = new Page<>(1,5);
        IPage<Chat> chatPage = chatServiceImpl.page(pageParam, queryWrapper);
        //对数组进行重编
        List<Chat> chats = chatPage.getRecords();
        System.out.println(chats.stream().filter(c -> c.getIslast() == 1).findAny().orElse(null));
    }

    @Test
    public void testSelect() {
        System.out.println("----- selectAll method test ------");
        Page<Map<String, String>> pageParam = new Page<>(1,5);
        HashMap<String, String> hashMap = new HashMap<>();
        IPage<Map<String, String>> opr = cosultantServiceImpl.getConsultantByOpr(pageParam, hashMap);
        System.out.println(opr.getRecords());
    }
//    sql测试
    @Test
    public void testSelectEssayNum() {
//        System.out.println("----- selectAll method test ------");
//        QueryWrapper<Essay> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("authorId","1");
//        int conunt = essayMapper.selectEssayNum(queryWrapper);
//        System.out.println(conunt);
        System.out.println("----- selectAll method test ------");
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(9);
        System.out.println(ids);
        List<Essay> list = essayServiceImpl.getAllEssayByIds(ids);
        System.out.println(list);
    }
    @Test
    public void test() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        System.out.println(date);
    }
}
