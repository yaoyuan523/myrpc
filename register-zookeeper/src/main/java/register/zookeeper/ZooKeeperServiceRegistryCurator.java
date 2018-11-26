package register.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import register.ServiceRegistry;

/**
 * 基于 ZooKeeper curator的服务注册接口实现
 * Created by loovee on 2018/3/14.
 */

public class ZooKeeperServiceRegistryCurator implements ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperServiceRegistry.class);


    private final CuratorFramework curator;


    public ZooKeeperServiceRegistryCurator(String zkAddress) {
        // 创建 ZooKeeper 客户端
        curator = CuratorFrameworkFactory.newClient(zkAddress, Constant.ZK_SESSION_TIMEOUT,
            Constant.ZK_CONNECTION_TIMEOUT, new RetryNTimes(5, 1000));
        LOGGER.debug("connect zookeeper OK ");
        curator.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) {

        try {
            // 创建 registry 节点目录（持久）
            String registryPath = Constant.ZK_REGISTRY_PATH;
            Stat statReg = curator.checkExists().forPath(registryPath);
            if(statReg == null) {
                curator.create().forPath(registryPath, new byte[0]);
            }

            // 创建  service  节点目录（持久）
            String servicePath = registryPath + "/" + serviceName;
            Stat statService = curator.checkExists().forPath(servicePath);
            if(statService == null) {
                curator.create().forPath(servicePath, new byte[0]);
            }

            // 创建  serviceAddress  节点（临时）
            String addressPath = servicePath + "/temp-address-";
            curator.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(addressPath, serviceAddress.getBytes());

        } catch (Exception e) {
            LOGGER.error("create zkNode error",e);
        }
    }



}