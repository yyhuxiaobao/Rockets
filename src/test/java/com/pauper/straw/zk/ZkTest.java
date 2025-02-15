package com.pauper.straw.zk;

import lombok.extern.java.Log;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Log
@SpringBootTest
public class ZkTest {

    @Autowired
    private CuratorFramework curatorFramework;

    @Test
    public void testCreateNode() throws Exception {
        // 创建临时的，任何人都有权限的节点
        String result = curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/rootNode/emNode", "hello".getBytes(StandardCharsets.UTF_8));
        log.info("创建节点: [{}]" + result);
    }

    @Test
    public void testUpdateNode() throws Exception {

        // 创建节点
        curatorFramework.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/rootNode/peNode", "hello".getBytes(StandardCharsets.UTF_8));
        // 不带版本修改节点
        Stat stat = curatorFramework.setData().forPath("/rootNode/peNode", "hello1".getBytes(StandardCharsets.UTF_8));
        log.info("修改后节点版本: [{}]" + stat.getVersion());

        // 带版本修改节点，版本不正确，报错
        stat = curatorFramework.setData().withVersion(10).forPath("/emNode", "hello1".getBytes(StandardCharsets.UTF_8));
        log.info("修改后节点版本: [{}]" + stat.getVersion());
    }

    @Test
    public void testGetNode() throws Exception {
        // 直接查询
        byte[] data = curatorFramework.getData().forPath("/rootNode/peNode");
        log.info("节点数据: [{}]"+ new String(data));

        // 同时获取节点属性，放到stat中
        Stat stat = new Stat();
        data = curatorFramework.getData().storingStatIn(stat).forPath("/rootNode/peNode");
        log.info("节点数据: [{}]" + new String(data));
        log.info("节点版本: [{}]" + stat.getVersion());

        // 查询的时候注册监听
        data = curatorFramework.getData().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent event) throws Exception {
                log.info("事件监听: [{}]" + event);
            }
        }).forPath("/rootNode/peNode");
        log.info("节点数据: [{}]" + new String(data));
        Thread.sleep(100000);
    }

    @Test
    public void testDeleteNode() throws Exception {
        // 创建节点
        curatorFramework.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/rootNode/emNode", "hello".getBytes(StandardCharsets.UTF_8));

        // 方法
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/rootNode/emNode");
    }

    @Test
    public void testWatchCache() throws Exception {
        try {
            String workPath = "/rootNode/testWatch/a";
            // 创建节点
            curatorFramework.create().creatingParentContainersIfNeeded()
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(workPath, "hello".getBytes(StandardCharsets.UTF_8));
            // 创建缓存
            CuratorCache curatorCache = CuratorCache.builder(curatorFramework, "/rootNode/testWatch").build();

            //老版本中分分三种监听器NodeCacheListener ：只监听单个节点,PathChildrenCacheListener ：只监听子节点,TreeCacheListener ：监听该节点以及子节点
            CuratorCacheListener curatorCacheListener = new CuratorCacheListener() {
                @Override
                public void event(Type type, ChildData oldData, ChildData data) {
                    log.info("curatorCacheListener start……………………………………………………");
                    log.info("change type: [{}]" + type);
                    log.info("ZNode节点状态改变, path={}" + (data ==null ? null : data.getPath()));
                    log.info("ZNode节点状态改变,before: " + (oldData == null ? null: new String(oldData.getData())));
                    log.info("ZNode节点状态改变,after: " + (data == null ? null : new String(data.getData())));
                }
            };
            curatorCache.listenable().addListener(curatorCacheListener);
            curatorCache.start();

            Thread.sleep(5000);

            // 第1次变更节点数据
            curatorFramework.setData().forPath("/rootNode/testWatch", "第1次更改内容".getBytes());
            Thread.sleep(5000);

            // 第2次变更节点数据
            curatorFramework.setData().forPath("/rootNode/testWatch/a", "第2次更改内容".getBytes());
            Thread.sleep(5000);

            // 第3次创建新节点
            curatorFramework.create().creatingParentContainersIfNeeded()
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/rootNode/testWatch/b", "hello".getBytes(StandardCharsets.UTF_8));
            Thread.sleep(5000);

        } catch (Exception e) {
            log.warning("创建Cache监听失败" + e);
        } finally {
            // 删除节点
            curatorFramework.delete().deletingChildrenIfNeeded().forPath("/rootNode/testWatch");
        }
    }


    @Test
    public void testTransaction() throws Exception{
        //定义几个基本操作
        CuratorOp createOp = curatorFramework.transactionOp().create()
                .forPath("/curator/one_path","some data".getBytes());

        CuratorOp setDataOp = curatorFramework.transactionOp().setData()
                .forPath("/curator","other data".getBytes());

        CuratorOp deleteOp = curatorFramework.transactionOp().delete()
                .forPath("/curator");

        //事务执行结果
        List<CuratorTransactionResult> results = curatorFramework.transaction()
                .forOperations(createOp,setDataOp,deleteOp);

        //遍历输出结果
        for(CuratorTransactionResult result : results){
            System.out.println("执行结果是： " + result.getForPath() + "--" + result.getType());
        }
    }

    @Test
    public void testLock() throws Exception{

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new InterProcessMutexRunnable(curatorFramework), "t" + i);
            t.start();
        }

        Thread.sleep(50*1000);
    }

}
