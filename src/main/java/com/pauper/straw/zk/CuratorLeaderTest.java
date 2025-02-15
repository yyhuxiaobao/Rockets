package com.pauper.straw.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.EnsurePath;

public class CuratorLeaderTest {

    private static final String ZK_PATH = "/zktest";
    public static void main(String[] args) throws InterruptedException {
        LeaderSelectorListener listener = new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println(Thread.currentThread().getName() + " take leadership!");
                // takeLeadership() method should only return when leadership is being relinquished.
                Thread.sleep(5000L);
                System.out.println(Thread.currentThread().getName() + " relinquish leadership!");
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState state) {
            }
        };

        new Thread(() -> {
            registerListener(listener);
        }).start();

        new Thread(() -> {
            registerListener(listener);
        }).start();

        new Thread(() -> {
            registerListener(listener);
        }).start();

        Thread.sleep(Integer.MAX_VALUE);
    }

    private static void registerListener(LeaderSelectorListener listener) {
        //Ensure path
        try {
            new EnsurePath(ZK_PATH).ensure(ZkClient.getClient().getZookeeperClient());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Register listener
        LeaderSelector selector = new LeaderSelector(ZkClient.getClient(), ZK_PATH, listener);
        selector.autoRequeue();
        selector.start();
    }
}
