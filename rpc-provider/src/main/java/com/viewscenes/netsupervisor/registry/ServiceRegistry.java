package com.viewscenes.netsupervisor.registry;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author MACHENIKE
 * @date 2018-11-30
 */
@Component
public class ServiceRegistry {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${registry.address}")
    private String registryAddress;

    private static final String ZK_REGISTRY_PATH = "/rpc";

    public void register(String data) {
        if (data != null) {
            ZkClient client = connectServer();
            if (client != null) {
                addRootNode(client);
                createNode(client, data);
            }
        }
    }
    private ZkClient connectServer() {
        ZkClient client = new ZkClient(registryAddress,20000,20000);
        return client;
    }

    /**
     创建根目录/rpc
     */
    private void addRootNode(ZkClient client){
        boolean exists = client.exists(ZK_REGISTRY_PATH);
        if (!exists){
            client.createPersistent(ZK_REGISTRY_PATH);
            logger.info("创建zookeeper主节点 {}",ZK_REGISTRY_PATH);
        }
    }

    /**
    在/rpc根目录下，创建临时顺序子节点
     （有一点需要注意，子节点必须是临时节点。这样，生产者端停掉之后，才能通知到消费者，把此服务从服务列表中剔除）
    */
    private void createNode(ZkClient client, String data) {
        String path = null;
        path = client.create(ZK_REGISTRY_PATH + "/provider", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        logger.info("创建zookeeper数据节点 ({} => {})", path, data);
    }
}
