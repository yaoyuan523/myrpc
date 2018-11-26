package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import register.ServiceRegistry;
import register.zookeeper.ZooKeeperServiceRegistry;
import register.zookeeper.ZooKeeperServiceRegistryCurator;
import server.RpcServer;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class ProviderApplication {

	@Value("${rpc.service.name}")
	String serviceName;

	@Value("${rpc.zk.address}")
	String zkAddress;

	@Value("${rpc.host}")
	String host;

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}

	@Bean
	ServiceRegistry serviceRegistry(){
		return new ZooKeeperServiceRegistry(zkAddress);
	}

	/*@Bean
	ServiceRegistry serviceRegistryCurator(){
		return new ZooKeeperServiceRegistryCurator(zkAddress);
	}*/

	@Bean
	RpcServer rpcServer(){
		return new RpcServer(host,serviceRegistry());
	}
}
