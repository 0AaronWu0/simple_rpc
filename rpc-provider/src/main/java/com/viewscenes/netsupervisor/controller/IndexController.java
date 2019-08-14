package com.viewscenes.netsupervisor.controller;

import com.alibaba.fastjson.JSONObject;
import com.viewscenes.netsupervisor.entity.InfoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: rpc-provider
 * @description: ${description}
 * @author: shiqizhen
 * @create: 2018-11-30 10:10
 **/
@Controller
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    AtomicLong id = new AtomicLong(10000);

    @RequestMapping("index")
    @ResponseBody
    public String index() throws Exception {

        String ip = InetAddress.getLocalHost().getHostAddress();
        InfoUser user = new InfoUser(id.incrementAndGet(), ip, System.currentTimeMillis()
                , "Jeen", "男", "123333123");
        String json = JSONObject.toJSONString(user);
        logger.info(json);
        return json;
    }
}
