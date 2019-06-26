package com.viewscenes.netsupervisor.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ZkCurator {

    @Value("${registry.address}")
    private String registryAddress;

    @Value("${zk.timeout}")
    private int sessionTimeout;

    /**
     * 创建curator
     */
    public CuratorFramework create(String address, int timeOut, RetryPolicy policy){
        //重试策略，初试时间1秒，重试10次
        policy = new ExponentialBackoffRetry(1000, 10);
        //通过工厂创建Curator
        return CuratorFrameworkFactory.builder()
                .connectString(address)
                .sessionTimeoutMs(timeOut)
                .retryPolicy(policy)
                .build();
    }

    public void helloworld(CuratorFramework curator) throws Exception {
        //开启连接
        curator.start();

        ExecutorService executor = Executors.newFixedThreadPool(5);

        /**创建节点，creatingParentsIfNeeded()方法的意思是如果父节点不存在，则在创建节点的同时创建父节点；
         * withMode()方法指定创建的节点类型，跟原生的Zookeeper API一样，不设置默认为PERSISTENT类型。
         * create创建节点方法可选的链式项：creatingParentsIfNeeded（是否同时创建父节点）、withMode（创建的节点类型）、forPath（创建的节点路径）、withACL（安全项）
         *delete删除节点方法可选的链式项：deletingChildrenIfNeeded（是否同时删除子节点）、guaranteed（安全删除）、withVersion（版本检查）、forPath（删除的节点路径）
         * */
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground((framework, event) -> { //添加回调
                    System.out.println("Code：" + event.getResultCode());
                    System.out.println("Type：" + event.getType());
                    System.out.println("Path：" + event.getPath());
                }, executor).forPath("/super/c1", "c1内容".getBytes());
        //为了能够看到回调信息
        Thread.sleep(5000);


        //获取节点数据
        String data = new String(curator.getData().forPath("/super/c1"));
        System.out.println(data);

        //判断指定节点是否存在
        Stat stat = curator.checkExists().forPath("/super/c1");
        System.out.println(stat);

        //更新节点数据
        curator.setData().forPath("/super/c1", "c1新内容".getBytes());
        data = new String(curator.getData().forPath("/super/c1"));
        System.out.println(data);

        //获取子节点
        List<String> children = curator.getChildren().forPath("/super");
        for (String child : children) {
            System.out.println(child);
        }

        //放心的删除节点，deletingChildrenIfNeeded()方法表示如果存在子节点的话，同时删除子节点
        curator.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");
        curator.close();
    }

    /**
     * PathChildrenCache：监听子节点的新增、修改、删除操作
     * @param curator
     * @throws Exception
     */
    public void watcherChildren(CuratorFramework curator, String path) throws Exception {
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
        });

        Stat stat = curator.checkExists().forPath("/super");
        /*if(stat == null){
            curator.create().forPath("/super", "123".getBytes());
        }else{
            System.out.println(new String(curator.getData().forPath("/super")));
        }*/

        curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/super/c1", "c1内容".getBytes());
        //经测试，不会监听到本节点的数据变更，只会监听到指定节点下子节点数据的变更
        curator.setData().forPath("/super", "456".getBytes());
        curator.setData().forPath("/super/c1", "c1新内容".getBytes());
        curator.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");
        Thread.sleep(5000);
        curator.close();

    }

    public static void main(String[] args) throws Exception {
        ZkCurator curator = new ZkCurator();
        CuratorFramework curatorFramework = curator.create("192.168.125.119:2181", 50000,null);
        //开启连接
//        curatorFramework.start();
//        curator.watcherChildren(curatorFramework, "/super");
        curator.helloworld(curatorFramework);
    }
}