package com.wu.intimate.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.model.*;
import com.wu.intimate.service.*;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/im/con")
@Api("与咨询师有关的接口")
public class ConsultantController {

    @Autowired
    private ConsultantService consultantServiceImpl;
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private EvaluateService evaluateServiceImpl;
    @Autowired
    private OrderListService orderListServiceImpl;
    @Autowired
    private EssayService essayServiceImpl;
    @Autowired
    private CommentsService commentsServiceImpl;

    /**
     * 条件获取所有咨询师信息
     * @param param pageIndex、pageSize、name
     *    *  城市：address
     *    *  时间：data
     *    *  排序: sort
     *    *  性别：gender
     *    *  领域：realm
     * @return
     */
    @ApiOperation("获取所有咨询师信息")
    @PostMapping("/getAllCons")
    public MyResult getAllCons(@RequestBody Map<String,String> param){

        String pageIndex = param.get("pageIndex");
        String pageSize = param.get("pageSize");
        Page<Map<String, String>> pageParam = new Page<>(1, 50);
        if(!StringUtils.isNullOrEmpty(pageIndex) && !StringUtils.isNullOrEmpty(pageSize)){
            pageParam.setCurrent(Integer.parseInt(pageIndex));
            pageParam.setSize(Integer.parseInt(pageSize));
        }
        IPage<Map<String, String>> pageMaps = consultantServiceImpl.getConsultantByOpr(pageParam, param);

        //从咨询师表中，查询出所有过审的咨询师  废弃掉的结果拼接法
//        Page<Map<String, Object>> pageMaps = consultantServiceImpl.getConsultantByOpr(pageParam, param);
//        List<Map<String, Object>> conMapList = pageMaps.getRecords();
        //遍历所有查询结果，为每个咨询师查询并添加其他数据
//        List<Map<String, Object>> resultMapList = new ArrayList<>();
//        try {
//            for (Map<String,Object> map : conMapList){
//                Map<String, Object> resultMap = new HashMap<>(map);
//                //用户表数据
//                resultMap.putAll(BeanUtils.describe(userServiceImpl.getOne(
//                        new QueryWrapper<User>()
//                                .eq("id",map.get("id")))));
//                //评论查询
//                resultMap.put("evaluateList",evaluateServiceImpl.listMaps(
//                        new QueryWrapper<Evaluate>()
//                                .eq("toId",map.get("id"))));
//                //文章数查询
//                resultMap.put("essayNum",essayServiceImpl.count(
//                        new QueryWrapper<Essay>()
//                                .eq("author", map.get("name"))));
//                resultMapList.add(resultMap);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        pageMaps.setRecords(resultMapList);
        return MyResult.ok(pageMaps);
    }

    /**
     * 根据id获取咨询师信息
     * @param id 咨询师id
     * @return 用户表+咨询师表
     */
    @ApiOperation("根据id获取咨询师信息")
    @GetMapping("/getConById/{id}")
    public MyResult getConById(@PathVariable("id") Integer id){
        return MyResult.ok(consultantServiceImpl.getConsultantById(id));
    }

    /**用户id、nickname、time、orderIndex、conId
     * 预约咨询师，存入预约表，修改咨询师可约时间
     * orderIndex：全部时间串中从左到右的角标
     * @return
     */
    @ApiOperation("预约咨询师")
    @PostMapping("/orderCon")
    public MyResult orderCon(@RequestBody Map<String, String> params){
        Consultant consultant = consultantServiceImpl.getById(params.get("conId"));

        String dateStr = consultant.getConDate();
        StringBuilder sb = new StringBuilder(dateStr);
        sb.setCharAt(Integer.parseInt(params.get("orderIndex")), '1');
        consultant.setConDate(sb.toString());

        boolean insert_flag = orderListServiceImpl.save(new OrderList(null, Integer.parseInt(params.get("userId")), Integer.parseInt(params.get("conId")), params.get("username"), params.get("time"), Integer.parseInt(params.get("orderIndex")),0));
        boolean edit_flag = consultantServiceImpl.updateById(consultant);
        return insert_flag && edit_flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据id获取咨询预约表 用户获取咨询师数据+预约表数据 咨询师获取用户数据+预约表数据
     * @param params 用户id 用户权限authority
     * @return 目标头像、目标昵称、预约数据
     */
    @ApiOperation("根据id获取咨询预约表 用户获取咨询师数据+预约表数据 咨询师获取用户数据+预约表数据")
    @PostMapping("/getOrderListById")
    public MyResult getOrderListById(@RequestBody Map<String, String> params){
        Integer id = Integer.valueOf(params.get("id"));
        Integer type = Integer.valueOf(params.get("type"));
        String authority = params.get("authority");
        List<Map<String, String>> orderList = null;


        if(authority.equals("2")){//普通用户，查询咨询师数据+预约表 id为userId
            orderList = orderListServiceImpl.getOrderListByUserId(id,type);
        }else if(authority.equals("1")){//咨询师用户，查询用户数据+预约表 id为conId
            orderList = orderListServiceImpl.getOrderListByConId(id, type);
        }
        assert orderList != null;
        CopyOnWriteArrayList<Map<String, String>> list = new CopyOnWriteArrayList<>(orderList);

        if(params.get("type").equals("0")){
            for(Map map : orderList){
                String time = String.valueOf(map.get("time"));
                String timeSplit = time.split(" ")[0];
                //当前时间
                String cuTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                int i = cuTime.compareTo(timeSplit);//1则cuTime大
                if(i > 0){
                    OrderList order = new OrderList();
                    order.setId((Integer) map.get("id"));
                    order.setStatus(1);
                    orderListServiceImpl.updateById(order);
                    list.remove(map);
                }
            }
        }

        return MyResult.ok(list);
    }


    /**
     * 用户申请咨询师认证
     * @param params id name idCard certifiedUrl gender address
     * @return
     */
    @ApiOperation("申请咨询师认证")
    @PostMapping("/addCon")
    public MyResult addCon(@RequestBody Map<String, String> params){
        System.out.println(params);
        //添加咨询师表
        Consultant consultant = new Consultant(Integer.parseInt(params.get("id")), params.get("name"), params.get("idCard"), null, null, "00000000000000000000",
                0, 0, 0, null, null, 0, params.get("certifiedUrl"), 0);
        boolean conFlag = consultantServiceImpl.save(consultant);
        //更新用户表
        User user = new User();
        user.setId(Integer.parseInt(params.get("id")));
        user.setGender(params.get("gender").equals("男") ? "1" : "0");
        user.setAddress(params.get("address"));
        boolean userFlag = userServiceImpl.updateById(user);

        return conFlag && userFlag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据咨询师id修改咨询师信息
     * @param consultant
     * @return
     */
    @ApiOperation("根据id修改咨询师数据")
    @PostMapping("/editConById")
    public MyResult addCon(@RequestBody Consultant consultant){
        System.out.println(consultant);
        //更新咨询师表
        boolean conFlag = consultantServiceImpl.updateById(consultant);
        //更新用户表
        User user = new User();
        user.setId(consultant.getId());
        user.setNickname(consultant.getName());
        user.setAuthority("1");
        boolean userFlag = userServiceImpl.updateById(user);

        return conFlag && userFlag ? MyResult.ok() : MyResult.fail();
    }


    /**
     * 根据id数组删除咨询师
     * @param ids 用户id数组
     * @return
     */
    @ApiOperation("根据id数组删除咨询师")
    @PostMapping("/deleteConByIds")
    @Transactional//事务控制(不能再使用try-catch)
    public MyResult deleteConByIds(@RequestBody List<Integer> ids){
        System.out.println("取消资格");
        boolean flag = false;
        //取消资格-->1.根据id删除咨询师表相关数据 2.根据id修改user表的权限 3.删除相关文章的评论 4.删除咨询师相关文章

        //根据用户id查询到相关文章idList
        List<Essay> essayList = essayServiceImpl.getAllEssayByIds(ids);
        List<Integer> essayIds = new ArrayList<>();
        for(Essay essay : essayList){
            essayIds.add(essay.getId());
        }
        //如果没有文章信息，跳过3、4步骤
        if(essayList.size() < 1){
            return this.deleteAllCons(ids)? MyResult.ok() : MyResult.fail();
        }
        //根据文章id删除相关该文章的评论、回复内容
        if(commentsServiceImpl.deleteCommentsByIds(essayIds)){
            //删除相关文章
            if(essayServiceImpl.removeByIds(essayIds)){
                flag = this.deleteAllCons(ids);
            }
        }
        this.deleteAllCons(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据id数组 批量删除 咨询师
     * @param ids
     * @return
     */
    private boolean  deleteAllCons(List<Integer> ids){
        List<User> users = userServiceImpl.listByIds(ids);
        //删除咨询师表数据、修改user表权限
        for (User user : users){
            user.setAuthority("2");
        }
        return  consultantServiceImpl.removeByIds(ids) && userServiceImpl.updateBatchById(users);
    }
}
