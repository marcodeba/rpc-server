package com.gupaoedu.vip;

public class HelloServiceImpl implements IHelloService {

    @Override
    public String sayHello(String content) {
        System.out.println("server request in sayHello:" + content);
        return "server return sayHello:" + content;
    }

    @Override
    public String saveUser(User user) {
        System.out.println("server request in saveUser:" + user);
        return "SUCCESS";
    }
}
