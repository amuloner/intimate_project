package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.mapper.EssayMapper;
import com.wu.intimate.model.Consultant;
import com.wu.intimate.model.Essay;
import com.wu.intimate.model.User;
import com.wu.intimate.service.EssayService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EssayServiceImpl extends ServiceImpl<EssayMapper, Essay> implements EssayService {
    @Override
    public Integer getEssayNumByUserId(Integer userId) {
        QueryWrapper<Essay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("authorId",userId);
        return baseMapper.selectEssayNum(queryWrapper);
    }

    @Override
    public List<Essay> getAllEssayByIds(List<Integer> ids) {
        System.out.println("待查询id："+ids);
        QueryWrapper<Essay> queryWrapper = new QueryWrapper<>();
        //根据id条件拼接查询
        for (int i = 0; i < ids.size(); i++){
            //条件拼接
            int id = ids.get(i);
            if(i == 0){
                queryWrapper.eq("authorId",id);
            }else{
                queryWrapper.or(qw1 -> qw1.eq("authorId",id));
            }
        }
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * author、title、content、pageIndex、pageSize、conId、status
     * @param pageParam
     * @param param
     * @return
     */
    @Override
    public IPage<Map<String,Object>> getEssayByOpr(Page<Map<String,Object>> pageParam, Map<String, String> param) {
        String id = param.get("id");//作者id检索
        String author = param.get("author");//作者检索
        String title = param.get("title");//标题检索
        String content = param.get("content");//内容检索
        String isHot = param.get("hot");
        String flag = param.get("flag");//排序方式
        String status = param.get("status");//审核状态检索

        QueryWrapper<Essay> queryWrapper = new QueryWrapper<Essay>().eq("isDel", 0);
        if(!StringUtils.isNullOrEmpty(author))
            queryWrapper.like("author",author);
        if(!StringUtils.isNullOrEmpty(title))
            queryWrapper.like("title",title);
        if(!StringUtils.isNullOrEmpty(content))
            queryWrapper.like("content",content);
        if(!StringUtils.isNullOrEmpty(id))
            queryWrapper.eq("authorId",id);
        if(!StringUtils.isNullOrEmpty(status))
            queryWrapper.eq("status",status.equals("1")? "0" : "1");

        if(!StringUtils.isNullOrEmpty(flag) && param.get("flag").equals("1"))//标识查询最新文章
            queryWrapper.orderByDesc("uploadTime");
        if(!StringUtils.isNullOrEmpty(isHot))//标识查询热点文章
            queryWrapper.orderByDesc("readNum");

        Page<Map<String,Object>> page = baseMapper.selectMapsPage(pageParam,queryWrapper);
        return page;
    }
}
