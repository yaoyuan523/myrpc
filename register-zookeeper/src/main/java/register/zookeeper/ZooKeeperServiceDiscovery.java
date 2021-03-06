package register.zookeeper;

import common.util.CollectionUtil;
import register.ServiceDiscovery;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 基于 ZooKeeper 的服务发现接口实现
 * Created by loovee on 2018/3/14.
 */
public class ZooKeeperServiceDiscovery implements ServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperServiceDiscovery.class);

    private String zkAddress;

    private String registryPath;

    public ZooKeeperServiceDiscovery(String zkAddress) {
        this.zkAddress = zkAddress;
        registryPath = Constant.ZK_REGISTRY_PATH;
    }

    public ZooKeeperServiceDiscovery(String zkAddress, String registryPath) {
        this.zkAddress = zkAddress;
        this.registryPath = registryPath;
    }

    @Override
    public String discover(String name) {
        // 创建 ZooKeeper 客户端
        ZkClient zkClient = null;
        LOGGER.debug("connect zookeeper");
        try {
            zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
            // 获取 service 节点
            String servicePath = registryPath + "/" + name;
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtil.isEmpty(addressList)) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }
            // 获取 address 节点
            String address;
            int size = addressList.size();
            if (size == 1) {
                // 若只有一个地址，则获取该地址
                address = addressList.get(0);
                LOGGER.debug("get only address node: {}", address);
            } else {
                // 若存在多个地址，则随机获取一个地址
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("get random address node: {}", address);
            }
            // 获取 address 节点的值
            String addressPath = servicePath + "/" + address;
            String data = zkClient.readData(addressPath);
            return data;
        }catch (Exception e){
            LOGGER.error("read zkTempNode error",e);
            return null;
        }
        finally {
            if (null != zkClient) {
                zkClient.close();
            }
        }
    }
}
