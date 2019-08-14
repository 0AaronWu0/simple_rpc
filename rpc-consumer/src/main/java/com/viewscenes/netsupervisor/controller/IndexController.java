package com.viewscenes.netsupervisor.controller;
import	java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.viewscenes.netsupervisor.annotation.TimeLog;
import com.viewscenes.netsupervisor.entity.InfoUser;
import com.viewscenes.netsupervisor.service.InfoUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by MACHENIKE on 2018-12-03.
 */
@Controller
@RequestMapping("hello")
public class IndexController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    InfoUserService userService;

    AtomicLong id = new AtomicLong(10000);

    @RequestMapping(value = "index", method = RequestMethod.GET)
    @ResponseBody
    @TimeLog("123")
    public String index(@RequestParam Map<String, Object> map){
        System.out.println(map);
        return  "GET" + map.toString();
    }

    @RequestMapping(value = "index" , method = RequestMethod.POST)
    @ResponseBody
    @TimeLog("123")
    public String indexPost(@RequestBody String str,@RequestParam Map<String, Object> reqMap){
        System.out.println(str);
        if(str != null){
            Map map = JSON.parseObject(str);
            System.out.println(map);
        }
        return  str + reqMap.toString()+ new Date().toString();
    }

    @RequestMapping(value = "insert", method = RequestMethod.PUT)
    @ResponseBody
    public List<InfoUser> addUser() throws Exception {

        long start = System.currentTimeMillis();
        String ip = InetAddress.getLocalHost().getHostAddress();
        int thread_count = 100;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);
/*        for (int i=0;i<thread_count;i++){
            new Thread(() -> {
                InfoUser infoUser = new InfoUser(id.incrementAndGet(), ip, System.currentTimeMillis()
                        , "Jeen", "", "BeiJing");
                List<InfoUser> users = userService.insertInfoUser(infoUser);
                logger.info("返回用户信息记录:{}", JSON.toJSONString(users));
                countDownLatch.countDown();
            }).start();
        }*/
        InfoUser infoUser = new InfoUser(id.incrementAndGet(), ip, System.currentTimeMillis()
                , "Jeen", "", "BeiJing");
        userService.insertInfoUser(infoUser);
        logger.info("返回用户信息记录:{}", JSON.toJSONString(infoUser));
        countDownLatch.await();
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",thread_count,(end-start));
        return null;
    }

    @RequestMapping("getById")
    @ResponseBody
    public InfoUser getById(String id){
        logger.info("根据ID查询用户信息:{},{}",id,userService.getInfoUserById(id));
        return userService.getInfoUserById(id);
    }

    @RequestMapping("getNameById")
    @ResponseBody
    public String getNameById(String id){
        logger.info("根据ID查询用户名称:{}",id);
        return userService.getNameById(id);
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public Map<String,InfoUser> getAllUser() throws InterruptedException {

        long start = System.currentTimeMillis();
        int thread_count = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);
        for (int i=0;i<thread_count;i++){
            new Thread(() -> {
                Map<String, InfoUser> allUser = userService.getAllUser();
                logger.info("查询所有用户信息：{}",JSONObject.toJSONString(allUser));
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",thread_count,(end-start));

        return null;
    }
}
