package com.trainingsystem.trainingSystem.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trainingsystem.trainingSystem.annotation.MyTransactional;
import com.trainingsystem.trainingSystem.mapper.UserMapper;
import com.trainingsystem.trainingSystem.pojo.User;
import com.trainingsystem.trainingSystem.service.UserService;
import com.trainingsystem.trainingSystem.util.result.Result;
import com.trainingsystem.trainingSystem.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @GetMapping("/redis")
    public String redisTest() {
        stringRedisTemplate.opsForValue().set("gender", "male");

        String major = stringRedisTemplate.opsForValue().get("major");
        String name = stringRedisTemplate.opsForValue().get("name");
        String age = stringRedisTemplate.opsForValue().get("age");

        return name;
    }


    @RequestMapping("/transationalTest")
    @MyTransactional
    public String transationalTest(@RequestBody JSONObject jsonObject) {

        Long userId = jsonObject.getLong("userId");
        User user = new User();
        user.setUserId(userId);
        user.setUserLevel(3);
        user.setEmail("");
        user.setAccount(123);
        user.setCollege("");
        user.setMajor("");
        user.setName("");
        user.setPwdId(110L);

        int i = 1 / 0;

        int insert = userMapper.insert(user);


        return "insert: " + insert;
    }

}
