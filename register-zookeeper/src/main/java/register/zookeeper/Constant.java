package register.zookeeper;

/**
 * 常量
 * Created by loovee on 2018/3/14.
 */
public interface Constant {

    public static int ZK_SESSION_TIMEOUT 	= 5000;

    public static int ZK_CONNECTION_TIMEOUT = 1000;

    public static String ZK_REGISTRY_PATH = "/zookeeper-rpc-registry";
}