package com.viewscenes.netsupervisor.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

public class ClientThatLocks {
    private final InterProcessMutex lock;
    //private final InterProcessSemaphoreMutex lock;
    private final String resource;
    private final String clientName;

    public ClientThatLocks(CuratorFramework framework, String path, String resource, String clientName) {
        this.lock = new InterProcessMutex(framework, path);
        //this.lock = new InterProcessSemaphoreMutex(framework, path);
        this.resource = resource;
        this.clientName = clientName;
    }

    public void doWork(long time, TimeUnit timeUnit) throws Exception {
        if(!lock.acquire(time, timeUnit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock!");
        }
        System.out.println(clientName + " has the lock");

        /*if(!lock.acquire(time, timeUnit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock!");
        }
        System.out.println(clientName + " has the lock");*/

        try {
            resource.toString();
        } finally {
            System.out.println(clientName + " releasing the lock");
            lock.release();
            //lock.release();
        }
    }

}
