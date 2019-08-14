package com.viewscenes.netsupervisor.connection;

import com.alibaba.fastjson.JSONObject;
import com.viewscenes.netsupervisor.zk.ZkCurator;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author as
 * 服务发现ss
 */
@Component
public class ServiceDiscovery {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${registry.address}")
    private String registryAddress;
    @Value("${zk.timeout}")
    private int sessionTimeout;

    @Autowired
    private ConnectManage connectManage;

    private CuratorFramework curator;

    // 服务地址列表
    private volatile List<String> addressList = new ArrayList<>();
    private static final String ZK_REGISTRY_PATH = "/rpc";
    private ZkClient client;
    @Autowired
    private ZkCurator zkCurator;


    @PostConstruct
    public void init(){
        /*curator = zkCurator.create(registryAddress, sessionTimeout, null );
        //开启连接
        curator.start();
        try {
            WatcherChildren(curator , ZK_REGISTRY_PATH);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
       client = connectServer();
        if (client != null) {
            watchNode(client);
        }
    }

    private ZkClient connectServer() {
        ZkClient client = new ZkClient(registryAddress,20000,20000);
        return client;
    }
    private void watchNode(final ZkClient client) {
        List<String> nodeList = client.subscribeChildChanges(ZK_REGISTRY_PATH, (s, nodes) -> {
            logger.info("监听到子节点数据变化{}",JSONObject.toJSONString(nodes));
            addressList.clear();
            getNodeData(nodes);
            updateConnectedServer();
        });
        getNodeData(nodeList);
        logger.info("已发现服务列表...{}", JSONObject.toJSONString(addressList));
        updateConnectedServer();
    }
    private void updateConnectedServer(){
        connectManage.updateConnectServer(addressList);
    }

    private void getNodeData(List<String> nodes){
        logger.info("/rpc子节点数据为:{}", JSONObject.toJSONString(nodes));
        for(String node:nodes){
            String address = client.readData(ZK_REGISTRY_PATH+"/"+node);
            addressList.add(address);
        }
    }

    /**
     * PathChildrenCache：监听子节点的新增、修改、删除操作
     * @param curator
     * @throws Exception
     */
    public void WatcherChildren(CuratorFramework curator,String path) throws Exception {
        //第三个参数表示是否接收节点数据内容
        PathChildrenCache childrenCache = new PathChildrenCache(curator, path, true);
        /**
         * 如果不填写这个参数，则无法监听到子节点的数据更新
         如果参数为PathChildrenCache.StartMode.BUILD_INITIAL_CACHE，则会预先创建之前指定的/super节点
         如果参数为PathChildrenCache.StartMode.POST_INITIALIZED_EVENT，效果与BUILD_INITIAL_CACHE相同，只是不会预先创建/super节点
         参数为PathChildrenCache.StartMode.NORMAL时，与不填写参数是同样的效果，不会监听子节点的数据更新操作
         */
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener((framework, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                default:
                    break;
            }
            logger.info("监听到子节点数据变化{}", JSONObject.toJSONString(framework.getChildren().forPath(ZK_REGISTRY_PATH)));
            addressList.clear();
            List<String> nodes = framework.getChildren().forPath(ZK_REGISTRY_PATH);
            logger.info("/rpc子节点数据为:{}", JSONObject.toJSONString(nodes));
            for(String node:nodes){
                String address = new String (framework.getData().forPath(ZK_REGISTRY_PATH +"/"+ node),"UTF-8");
                addressList.add(address);
            }
            updateConnectedServer();
        });
    }
}
