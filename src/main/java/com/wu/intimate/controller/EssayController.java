package com.wu.intimate.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.intimate.model.Essay;
import com.wu.intimate.model.Star;
import com.wu.intimate.model.User;
import com.wu.intimate.service.*;
import com.wu.intimate.service.impl.EssayServiceImpl;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/im/essay")
@Api("与文章有关的接口")
public class EssayController {
    @Autowired
    private EssayService essayServiceImpl;
    @Autowired
    private StarService starServiceImpl;

    /**
     * 根据条件分页获取所有的文章信息
     * @param param author、title、content、pageIndex、pageSize、conId、status
     * @return
     */
    @ApiOperation("根据条件分页获取所有的文章信息")
    @PostMapping("/getEssayList")
    public MyResult getEssayList(@RequestBody Map<String,String> param){
        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        Page<Map<String,Object>> pageParam = new Page<>(pageIndex,pageSize);
        IPage<Map<String,Object>> iPage = essayServiceImpl.getEssayByOpr(pageParam,param);
        //查询点赞量
        for (Map<String, Object>  essayMap: iPage.getRecords()) {
                essayMap.put("essayLikes",starServiceImpl.count(new QueryWrapper<Star>().eq("essayId",essayMap.get("id"))));
        }
        return MyResult.ok(iPage);
    }


    /**
     * 发布文章
     * @param essay authorId title content uploadTime smallImg
     * @return
     */
    @ApiOperation("发布文章提交审核")
    @PostMapping("/addEssay")
    public MyResult addEssay(@RequestBody Essay essay){
        //author、title、content、pageIndex、pageSize、conId、status
        Essay newEssay = new Essay(null, essay.getAuthor(), essay.getAuthorId(), null, essay.getTitle(),
                essay.getContent(), essay.getUploadTime(), null, essay.getSmallImg(), 0, 0, 0);
        boolean flag = essayServiceImpl.save(newEssay);
        return flag ? MyResult.ok() : MyResult.fail();
    }



    /**
     * 根据id更新文章信息
     * @param essay
     * @return
     */
    @ApiOperation("根据文章id修改文章数据")
    @PostMapping("/editEssayById")
    public MyResult editEssayById(@RequestBody Essay essay){
        boolean flag = essayServiceImpl.updateById(essay);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据文章id批量删除文章数据
     * @param ids
     * @return
     */
    @ApiOperation("根据文章id数组删除文章数据")
    @PostMapping("/deleteEssayByIds")
    public MyResult deleteEssayByIds(@RequestBody List<Integer> ids){
//        List<Essay> essayList = essayServiceImpl.listByIds(ids);
//        for (Essay essay : essayList) {
//            essay.setIsDel(1);
//        }
//        boolean flag = essayServiceImpl.updateBatchById(essayList);
        boolean flag = essayServiceImpl.removeByIds(ids);
        essayServiceImpl.removeByIds(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }
}
