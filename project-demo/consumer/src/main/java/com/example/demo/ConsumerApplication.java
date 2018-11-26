package com.example.demo;

import client.RpcProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import register.ServiceDiscovery;
import register.zookeeper.ZooKeeperServiceDiscovery;

@SpringBootApplication
public class ConsumerApplication {

	@Value("${rpc.zk.address}")
	String zkAddress;

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@Bean
	ServiceDiscovery serviceDiscovery(){
		return new ZooKeeperServiceDiscovery(zkAddress);
	}

	@Bean
	RpcProxy rpcProxy(){
		return new RpcProxy(serviceDiscovery());
	}
}
