package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.CommentsMapper;
import com.wu.intimate.mapper.ConsultantMapper;
import com.wu.intimate.model.Comments;
import com.wu.intimate.model.Consultant;
import com.wu.intimate.model.Essay;
import com.wu.intimate.service.CommentsService;
import com.wu.intimate.service.ConsultantService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ConsultantServiceImpl  extends ServiceImpl<ConsultantMapper, Consultant> implements ConsultantService {

    /**
     *    pageIndex、pageSize
     *    用户表：
     *    城市：address
     *    性别：gender
     *
     *    咨询师表：
     *    名字：name
     *    排序: sort
     *    时间：date 0~3对应4天
     *    领域：realm
     *    状态: status
     * @param pageParam
     * @param param
     * @return
     */
    @Override
    public IPage<Map<String, String>> getConsultantByOpr(Page<Map<String, String>> pageParam, Map<String, String> param) {
        String name = param.get("name");
        String address = param.get("address");
        String date = param.get("date");
        String sort = param.get("sort");
        String gender = param.get("gender");
        String realm = param.get("realm");
        String status = param.get("status");


        QueryWrapper<Map<String, String>> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(status))////咨询师状态
            queryWrapper.eq("c.status", status);
        else queryWrapper.eq("c.status", "1");

        if(!StringUtils.isNullOrEmpty(name))//名字查询
            queryWrapper.like("c.name", name);
        if(!StringUtils.isNullOrEmpty(address))//地址查询
            queryWrapper.like("u.address",address);
        if(!StringUtils.isNullOrEmpty(gender))//性别查询
            queryWrapper.like("u.gender",gender);
        if(!StringUtils.isNullOrEmpty(realm))//领域查询
            queryWrapper.like("c.realm",realm);
        if(!StringUtils.isNullOrEmpty(date)) {//时间查询  sql中substr()可截取字符串  instr()可检索字符串中字符位置
            //接受到0---》第一天内  11000 00000 00000 00000
            int startIndex = 1 + 5*Integer.parseInt(date);//字段截取的开始字符
            int subNum = 5;//截取字符数
            queryWrapper.apply("INSTR(SUBSTR(c.`conDate`,{0},{1}),0) > 0", startIndex, subNum);
        }

        if(!StringUtils.isNullOrEmpty(sort)) {//默认排序 按照点赞量排序       推荐排序 按照评论量排序
            String column;
            column = sort.equals("0") ? "conLikes" : "evaluateNum";
            queryWrapper.orderByDesc(column);
        }

        Page<Map<String, String>> page = baseMapper.findByPage(pageParam,queryWrapper);
        return page;
    }


    @Override
    public Map<String, String> getConsultantById(Integer conId) {
        return baseMapper.findById(new QueryWrapper<>().eq("c.id", conId));
    }
}
