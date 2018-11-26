package com.example.demo.service.impl;


import com.example.demo.bean.TestBean;
import com.example.demo.service.TestService;
import server.RpcService;

/**
 * Created by lenovo on 2018/11/26.
 */
@RpcService(value = TestService.class)
public class TestServiceImpl implements TestService {

    @Override
    public int nextYearAge(TestBean testBean) {
        return testBean.getAge()+1;
    }
}
