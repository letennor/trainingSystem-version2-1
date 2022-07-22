package com.trainingsystem.trainingSystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.pojo.NormalTest;
import com.trainingsystem.trainingSystem.pojo.SpecialTest;
import com.trainingsystem.trainingSystem.pojo.TestSet;
import com.trainingsystem.trainingSystem.service.NormalTestService;
import com.trainingsystem.trainingSystem.service.SpecialTestService;
import com.trainingsystem.trainingSystem.service.TestSetService;
import com.trainingsystem.trainingSystem.util.common.SnowFlakeGenerateIdWorker;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author letennor
 * @since 2022-02-26
 */
@RestController
public class TestSetController {
    @Autowired
    TestSetService testSetService;
    @Autowired
    NormalTestService normalTestService;
    @Autowired
    SpecialTestService specialTestService;


    //返回一个老师最近制作的套题的情况
    /*
    1. 没有制作过试卷，即setId是null  code:1
    2. 正在制作试卷，但是还没有完成，即setId有值，normalTestNumber有值，但是小于10 code:2
    3. 已经制作完成至少一张试卷，即setId有值，normalTestNumber有值，且为10    code:3
     */
    @RequestMapping("/queryTeacherSetInfo")
    public Result<?> queryTeacherSetInfo(@RequestBody JSONObject jsonObject) {
        Long teacherId = jsonObject.getLong("teacherId");
        Map<String, Object> map = testSetService.queryTeacherSetInfo(teacherId);

        Long setId = (Long) map.get("setId");

        //将Long类型的setId替换成String类型的setId，避免溢出
        String setIdString = setId + "";
        map.put("setId", setIdString);

        Integer normalTestNumber = (Integer) map.get("normalTestNumber");

        if (setId == null) {
            return ResultUtil.defineSuccess(1, map);
        }

        if (normalTestNumber >= 0 && normalTestNumber < 10) {
            return ResultUtil.defineSuccess(2, map);
        }

        if (normalTestNumber == 10) {
            return ResultUtil.defineSuccess(3, map);
        }

        return null;
    }


    //创建套题
    @RequestMapping("/createSet")
    @MyTransactional
    public Map<String, Object> insertTestSet(@RequestBody JSONObject jsonObject) {

        Integer setLevel = jsonObject.getInteger("setLevel");
        Integer threshold = jsonObject.getInteger("threshold");
        String teacherIdString = jsonObject.getString("teacherId");
        Long teacherId = Long.parseLong(teacherIdString);

        //雪花算法生成SpecialId
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker =
                new SnowFlakeGenerateIdWorker(0L, 0L);
        String idString = snowFlakeGenerateIdWorker.generateNextId();

        Long setId = Long.parseLong(idString);
        testSetService.insertTestSet(setId, setLevel, threshold, teacherId);


        Map<String, Object> map = new HashMap<>();
        map.put("setId", idString);
        return map;
    }


    //返回题库所有试卷信息，包含普通试卷和专向试卷
    @GetMapping("/getAllTest")
    public Result<?> getAllTest() {
        Map<String, Object> data = new HashMap<>();
        List<NormalTest> normalTestList = normalTestService.getAllNormalTest();//所有普通试卷
        List<SpecialTest> specialTestList = specialTestService.getAllSpecialTest();//所有专项试卷
        data.put("normalTestList", normalTestList);
        data.put("specialTestList", specialTestList);
        return ResultUtil.success(data);
    }


    //取得一个老师所有套题
    @RequestMapping("/getTeacherTestSet")
    public Result<?> getTeacherTestSet(@RequestBody JSONObject jsonObject) {
        Long teacherId = jsonObject.getLong("teacherId");
        List<TestSet> testSetList = testSetService.getTeacherTestSet(teacherId);
        return ResultUtil.success(testSetList);
    }

    //判断套题是否可修改
    @RequestMapping("/isTestSetChangable")
    public Result<?> isTestSetChangable(@RequestBody JSONObject jsonObject) {
        Long setId = jsonObject.getLong("setId");
        boolean isChangable = testSetService.isChangable(setId);

        if (isChangable) {
            return ResultUtil.success(1);
        } else {
            return ResultUtil.defineFail(0, "无法修改套题");
        }

    }


    //修改套题及格线
    @RequestMapping("/modifyThreshold")
    public Result<?> modifyThreshold(@RequestBody JSONObject jsonObject) {
        Long setId = jsonObject.getLong("setId");
        Integer threshold = jsonObject.getInteger("threshold");

        int i = testSetService.modifyThreshold(setId, threshold);

        return ResultUtil.success(i);

    }


    //删除一套题
    @RequestMapping("/deleteTestSet")
    @MyTransactional
    public Result<?> deleteTestSet(@RequestBody JSONObject jsonObject) {
        Long setId = jsonObject.getLong("setId");
        int i = testSetService.deleteTestSet(setId);
        return ResultUtil.success(i);
    }


    //取得一套套题的所有普通试卷
    @RequestMapping("/getAllNormalTestBySetId")
    public Result<?> getAllNormalTestBySetId(@RequestBody JSONObject jsonObject) {
        Long setId = jsonObject.getLong("setId");
        List<NormalTest> normalTestListBySetId = normalTestService.getNormalTestListBySetId(setId);

        return ResultUtil.success(normalTestListBySetId);
    }

}

