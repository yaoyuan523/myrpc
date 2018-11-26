# 简介
一个简单的prc框架，底层采用netty通信。

## 模块划分

client 服务客户端，动态代理产生被调用类接口，将方法及参数信息传给服务端处理。  
register 服务注册接口  
register-zookeeper 采用zk实现的服务注册和发现  
rpc-common 公共包  
rpcserver 服务端，启动时加载所有远程服务接口实现类，收到客户端请求找到本地对应的实现类，并反射执行。

## demo
project-demo为实例，将zk配置修改为本地启动即可。
