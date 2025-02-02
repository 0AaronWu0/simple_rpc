package com.viewscenes.netsupervisor.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.viewscenes.netsupervisor.annotation.RpcService;
import com.viewscenes.netsupervisor.entity.InfoUser;
import com.viewscenes.netsupervisor.mapper.UserMapper;
import com.viewscenes.netsupervisor.service.InfoUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: rpc-provider
 * @description: ${description}
 * @author: shiqizhen
 * @create: 2018-11-30 16:55
 **/
@RpcService
public class InfoUserServiceImpl implements InfoUserService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    Map<String,InfoUser> infoUserMap = new ConcurrentHashMap<>();

    @Autowired
    UserMapper userMapper;

    @Override
    public void insertInfoUser(InfoUser infoUser) {
        logger.info("新增用户信息:{}", JSONObject.toJSONString(infoUser));
        //infoUserMap.put(infoUser.getId(),infoUser);
        userMapper.insertInfoUser(infoUser);
    }

    @Override
    public InfoUser getInfoUserById(String id) {
        InfoUser infoUser = infoUserMap.get(id);
        logger.info("查询用户ID:{}",id);
        return infoUser;
    }

    public List<InfoUser> getInfoUserList() {
        List<InfoUser> userList = new ArrayList<>();
        Iterator<Map.Entry<String, InfoUser>> iterator = infoUserMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, InfoUser> next = iterator.next();
            userList.add(next.getValue());
        }
        logger.info("返回用户信息记录:{}", JSON.toJSONString(userList));
        return userList;
    }

    @Override
    public void deleteInfoUserById(String id) {
        logger.info("删除用户信息:{}",JSONObject.toJSONString(infoUserMap.remove(id)));
    }

    @Override
    public String getNameById(String id){
        logger.info("根据ID查询用户名称:{}",id);
        //return infoUserMap.get(id).getName();
        return userMapper.getInfoUserById(id).getUserName();
    }
    @Override
    public Map<String,InfoUser> getAllUser(){
        logger.info("查询所有用户信息{}",JSONObject.toJSONString(infoUserMap));
        return infoUserMap;
    }
}
