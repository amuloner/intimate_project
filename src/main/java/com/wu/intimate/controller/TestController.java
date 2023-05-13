package com.wu.intimate.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.wu.intimate.model.*;
import com.wu.intimate.service.*;
import com.wu.intimate.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/im/test")
@Api("测评相关接口")
public class TestController {

    @Autowired
    private TestListService testListServiceImpl;
    @Autowired
    private TestAnsService testAnsServiceImpl;
    @Autowired
    private TestQuesService testQuesServiceImpl;
    @Autowired
    private TestRuleService testRuleServiceImpl;
    @Autowired
    private TestResultService testResultServiceImpl;
    @Autowired
    private UserService userServiceImpl;

    /**
     * 分页获取测评列表
     *
     * @param param pageIndex、pageSize、label、orderType、status、title
     * @return
     */
    @ApiOperation("分页获取测评列表")
    @PostMapping("/getTestList")
    public MyResult getTestList(@RequestBody Map<String, String> param) {

        String pageIndex = param.get("pageIndex");
        String pageSize = param.get("pageSize");

        if (StringUtils.isNullOrEmpty(pageIndex))
            pageIndex = "1";
        if (StringUtils.isNullOrEmpty(pageSize))
            pageSize = "10";

        Page<TestList> pageParam = new Page<>(Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
        IPage<TestList> iPage = testListServiceImpl.getTestTitleByOpr(pageParam, param);
        return MyResult.ok(iPage);
    }

    /**
     * 获取测评类别列表
     *
     * @return
     */
    @ApiOperation("获取测评类别列表")
    @PostMapping("/getLabelList")
    public MyResult getLabelList() {
        List<String> labels = testListServiceImpl.getTestLabels();
        System.out.println(labels);
        return MyResult.ok(labels);
    }

    /**
     * 根据id更新测试列表信息
     *
     * @param testList
     * @return
     */
    @ApiOperation("根据测试列表id修改测试列表数据")
    @PostMapping("/editTestById")
    public MyResult editTestById(@RequestBody TestList testList) {
        boolean flag = testListServiceImpl.updateById(testList);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据测试列表id批量删除测试列表数据
     *
     * @param ids
     * @return
     */
    @ApiOperation("根据测试列表id数组删除测试列表数据")
    @PostMapping("/deleteTestByIds")
    public MyResult deleteTestByIds(@RequestBody List<Integer> ids) {
        boolean flag = testListServiceImpl.removeByIds(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }


    /**
     * 分页获取测评结果
     *
     * @param param pageIndex pageSize userId
     * @return
     */
    @ApiOperation("分页获取测评结果")
    @PostMapping("/getTestRList")
    public MyResult getTestRList(@RequestBody Map<String, String> param) {

        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        if (String.valueOf(pageIndex).equals(""))
            pageIndex = 1;
        if (String.valueOf(pageSize).equals(""))
            pageIndex = 10;

        Page<Map<String, Object>> pageParam = new Page<>(pageIndex, pageSize);
        IPage<Map<String, Object>> iPage = testResultServiceImpl.getTestResultByOpr(pageParam, param);

        //检索相关信息
        List<Map<String, Object>> testResultList = iPage.getRecords();
        for (Map<String, Object> testResult : testResultList) {
            testResult.put("test", testListServiceImpl.getById((int) testResult.get("testId")));
            testResult.put("userInfo", userServiceImpl.getMap(new QueryWrapper<User>().eq("id", testResult.get("userId"))));
        }
        iPage.setRecords(testResultList);
        return MyResult.ok(iPage);
    }

    /**
     * {testName=测试, label=家庭, direction="", testAList=[{title=111, ansList=[{direction=d, score=2}]}]}
     *
     * @param param
     * @return
     */
    @ApiOperation("添加问卷")
    @PostMapping("/addTest")
    @Transactional
    public MyResult addTest(@RequestBody Map<String, Object> param) {
        System.out.println(param);
        String rule = "[{\"core\":98,\"low\":93,\"up\":-1,\"result\":\"这个分数表你确实正以极度的压力反应在伤害你自己的健康。你需要专业心理治疗师给予一些忠告， 他可以帮助你消减你对於压力器的知觉， 并帮助你改良生活的品质。\"},{\"core\":87,\"low\":82,\"up\":92,\"result\":\"这个分数表示你正经历太多的压力， 这正在损害你的健康，并令你的人际关系发生问题。 你的行为会伤己， 也可能会影响其他人。 因此， 对你来说， 学习如何减除自己的压力反应是非常重要的。 你可能必须花许多时间做练习， 学习控制压力， 也可以寻求专业的帮助。\"},{\"core\":76,\"low\":71,\"up\":81,\"result\":\"这个分数显示的压力程度中等， 可能正开始对健康不利。 你可以仔细反省自己对压力器如何作出反应， 并学习在压力器出现时， 控制自己的肌肉紧张， 以消除生理激活反应。 好老师对你有帮助，要不然就选用适合的肌肉松弛录音带。\"},{\"core\":65,\"low\":60,\"up\":70,\"result\":\"这个分数指出你生活中的兴奋与压力量也许是相当适中的。 偶而会有一段时间压力太多， 但你也许有能力去享受压力， 并且很快回到平静的状态， 因此对你的健康并不会造成威胁。 做一些松弛的练习仍是有益的。\"},{\"core\":54,\"low\":49,\"up\":59,\"result\":\"这个分数表示你能\uE0D8控制你自己的压力反应， 你是一个相当放松的人。 也许你对於所遇到的各种压力器， 并没有将它们解释为威胁， 所以你很容易与人相处， 可以毫无惧怕地胜任工作， 也没有失去自信。\"},{\"core\":43,\"low\":38,\"up\":48,\"result\":\"这个分数表示你对所遭遇的压力器很不易为所动， 甚至是不当一回事， 好像并没有发生过一样。 这对你的健康不会有甚麼负面的影响， 但你的生活缺乏适度的兴奋， 因此趣味也就有限。\"},{\"core\":32,\"low\":27,\"up\":37,\"result\":\"这个分数表示你的生活可能是相当沉闷的， 即使刺激或有趣的事情发生了， 你也很少作反应。 可能你必须参与更多的社会活动或娱乐活动， 以增加你的压力激活反应。\"},{\"core\":21,\"low\":\"21\",\"up\":26,\"result\":\"如果你的分数只落在这个范围内， 也许意味你在生活中所经历的压力经验不够， 或是你并没有正确地分析自己。 你最好更主动些， 在工作、 社交、 娱乐等活动上多寻求些刺激。 做松弛练习对没有甚麼用， 但找一些辅导也许会有帮助。\"},{\"core\":\"16\",\"low\":-1,\"up\":\"20\",\"result\":\"如果你的分数只落在这个范围内， 那么你的生活非常健康！\"}]";

//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        TestList test = new TestList();
        test.setTitle((String) param.get("testName"));
        test.setLabel((String) param.get("label"));
        test.setDirection((String) param.get("direction"));
        test.setCtime(date);
        //在问卷表中增加
        testListServiceImpl.save(test);
        List<Map<String, Object>> ques = (List<Map<String, Object>>) param.get("testAList");
        int qnum = 1;
        //在选项表中添加
        for (Map<String, Object> que : ques) {
            TestQues testQues = new TestQues();
            testQues.setTestId(test.getId());
            testQues.setTitle((String) que.get("title"));
            testQues.setQnum(qnum++);
            testQuesServiceImpl.save(testQues);

            List<Map<String, String>> ans = (List<Map<String, String>>) que.get("ansList");
            //在选项表中添加
            for (Map<String, String> an : ans) {
                TestAns testAns = new TestAns();
                testAns.setQId(String.valueOf(testQues.getId()));
                testAns.setDirection(an.get("direction"));
                testAns.setScore(Integer.parseInt(an.get("score")));
                testAnsServiceImpl.save(testAns);
            }
        }

        TestRule testRule = new TestRule();
        testRule.setTestId(test.getId());
        testRule.setRule(rule);
        testRule.setCtime(date);
        testRuleServiceImpl.save(testRule);
        return MyResult.ok();
    }

    /**
     * 根据id更新测试结果信息
     *
     * @param testResult
     * @return
     */
    @ApiOperation("根据测试列表id修改测试结果数据")
    @PostMapping("/editTestRById")
    public MyResult editTestRById(@RequestBody TestResult testResult) {
        boolean flag = testResultServiceImpl.updateById(testResult);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据测试结果id批量测评结果数据
     *
     * @param ids
     * @return
     */
    @ApiOperation("根据测试结果id批量测评结果数据")
    @PostMapping("/deleteTestRByIds")
    public MyResult deleteTestRByIds(@RequestBody List<Integer> ids) {
        boolean flag = testResultServiceImpl.removeByIds(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据问题ID获取测评选项、选项答案、选项分数
     *
     * @param 、pageIndex、pageSize、testId
     * @return
     */
    @ApiOperation("根据问题ID获取测评选项、选项答案、选项分数")
    @PostMapping("/getTestQuesAndAns")
    public MyResult getTestQuesAndAns(@RequestBody Map<String, String> param) {

        int pageIndex = Integer.parseInt(param.get("pageIndex"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        //问题数据
        Page<Map<String, Object>> pageParam = new Page<>(pageIndex, pageSize);
        IPage<Map<String, Object>> iPage = testQuesServiceImpl.getTestQuesAndAnsByPage(pageParam, param);


        //遍历问题数据获取问题选项
        List<Map<String, Object>> newQuesList = new ArrayList<>();
        //遍历问题数据获取问题选项
        for (Map<String, Object> map : iPage.getRecords()) {
            map.put("ansList", testAnsServiceImpl.list(new QueryWrapper<TestAns>().eq("qId", map.get("id"))));
            newQuesList.add(map);
        }
        System.out.println(newQuesList);
        iPage.setRecords(newQuesList);
        return MyResult.ok(iPage);
    }

    /**
     * 根据问题ID修改测评选项、选项答案、选项分数
     *
     * @param 、
     * @return
     */
    @ApiOperation("根据问题ID修改测评选项、选项答案、选项分数")
    @PostMapping("/editTestQAndAById")
    @Transactional
    public MyResult editTestQAndAById(@RequestBody Map<String, Object> param) {

        TestQues testQues = new TestQues();
        testQues.setTitle(String.valueOf(param.get("title")));
        testQues.setId(Integer.parseInt(String.valueOf(param.get("id"))));

        //更新问题项
        testQuesServiceImpl.updateById(testQues);
        List<Map<String, Object>> ans = (List<Map<String, Object>>) param.get("ansList");

        //在选项表中添加
        for (Map<String, Object> an : ans) {
            TestAns testAns = new TestAns();
            testAns.setId((Integer) an.get("id"));
            testAns.setDirection(String.valueOf(an.get("direction")));
            testAns.setScore(Integer.parseInt(String.valueOf(an.get("score"))));
            testAnsServiceImpl.updateById(testAns);
        }
        return MyResult.ok();
    }

    /**
     * 根据测试选项id批量测评问题项数据
     *
     * @param ids
     * @return
     */
    @ApiOperation("根据测试选项id批量测评问题项数据")
    @PostMapping("/deleteTestQAndAByIds")
    @Transactional
    public MyResult deleteTestQAndAByIds(@RequestBody List<Integer> ids) {
        boolean flag = testQuesServiceImpl.removeByIds(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    @ApiOperation("根据问题ID获取测评选项")
    @GetMapping("/getTestAns/{qId}")
    public MyResult getTestAns(@PathVariable("qId") Integer qId) {
        List<TestAns> testAnsList = testAnsServiceImpl.list(new QueryWrapper<TestAns>().eq("qId", qId));
        return !testAnsList.isEmpty() ? MyResult.ok(testAnsList) : MyResult.fail();
    }


    @ApiOperation("根据测评ID获取测评问题")
    @GetMapping("/getTestQues/{testId}")
    public MyResult getTestQues(@PathVariable("testId") Integer testId) {
        List<TestQues> testQuesList = testQuesServiceImpl.list(new QueryWrapper<TestQues>().eq("testId", testId));
        return !testQuesList.isEmpty() ? MyResult.ok(testQuesList) : MyResult.fail();
    }

    @ApiOperation("根据测评数据计算测评结果")
    @PostMapping("/getTestResult")
    public MyResult getTestResult(@RequestBody Map<String, String> selectResult) {//{testId:,selList:[{id:,qid,score},userId,userName]}
        int score = 0,
                testId = Integer.parseInt(selectResult.get("testId")),
                userId = Integer.parseInt(selectResult.get("userId"));
        String userName = selectResult.get("userName");
        String message = null;
        Map<String, String> result = new HashMap<>();

        //分数计算
        List<Map> selList = JSONObject.parseArray(selectResult.get("selList"), Map.class);
        for (Map sel : selList) {
            score += (int) sel.get("score");
        }

        //规则判定
        TestRule rule = testRuleServiceImpl.getOne(new QueryWrapper<TestRule>().eq("testId", 1));
        List<Map> ruleList = JSONObject.parseArray(rule.getRule(), Map.class);
        for (Map ruleItem : ruleList) {
            int low = (int) ruleItem.get("low");
            int up = (int) ruleItem.get("up");
            if ((low == -1 && score <= up)
                    || (up == -1 && score >= low)
                    || (up >= score && low <= score)) {//最小值、最大
                message = (String) ruleItem.get("result");
                score = (int) ruleItem.get("core");
                break;
            }
        }

        System.out.println(score);
        System.out.println(message);
        //处理结果
        if (score != 0 && !StringUtils.isNullOrEmpty(message)) {
            result.put("score", String.valueOf(score));
            result.put("message", message);
            //存入数据库
            TestResult testResult = new TestResult();
            testResult.setTestId(testId);
            testResult.setUserId(userId);
            testResult.setUsername(userName);
            testResult.setResultJson(JSONObject.toJSONString(result));

            testResultServiceImpl.save(testResult);
        }

        return !result.isEmpty() ? MyResult.ok(result) : MyResult.fail();
    }

    /**
     * 分页获取测评规则列表
     *
     * @param param pageIndex、pageSize、title
     * @return
     */
    @ApiOperation("分页获取测评规则列表")
    @PostMapping("/getTestRuList")
    public MyResult getTestRuList(@RequestBody Map<String, String> param) {

        String pageIndex = param.get("pageIndex");
        String pageSize = param.get("pageSize");

        if (StringUtils.isNullOrEmpty(pageIndex))
            pageIndex = "1";
        if (StringUtils.isNullOrEmpty(pageSize))
            pageSize = "10";

        Page<Map<String, String>> pageParam = new Page<>(Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
        IPage<Map<String, String>> iPage = testRuleServiceImpl.getTestRuleList(pageParam, param);
        return MyResult.ok(iPage);
    }

    /**
     * 根据测试id更新测试规则信息
     *
     * @param
     * @return
     */
    @ApiOperation("根据测试id更新测试规则信息")
    @PostMapping("/editTestRuById")
    public MyResult editTestRuById(@RequestBody TestRule testRule) {
        boolean flag = testRuleServiceImpl.updateById(testRule);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * 根据测试规则id批量删除测试规则数据
     *
     * @param ids
     * @return
     */
    @ApiOperation("根据测试规则id批量删除测试规则数据")
    @PostMapping("/deleteTestRuByIds")
    public MyResult deleteTestRuByIds(@RequestBody List<Integer> ids) {
        boolean flag = testRuleServiceImpl.removeByIds(ids);
        return flag ? MyResult.ok() : MyResult.fail();
    }

    /**
     * @param
     * @return
     */
    @ApiOperation("添加问卷规则")
    @PostMapping("/addTestRu")
    @Transactional
    public MyResult addTestRu(@RequestBody TestRule testRule) {
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        testRule.setCtime(date);
        testRuleServiceImpl.save(testRule);
        return MyResult.ok();
    }

}