package com.gupaoedu.vip;

public class App {
    public static void main(String[] args) {
        IHelloService helloService = new HelloServiceImpl();

        RpcProxyServer proxyServer = new RpcProxyServer();
        proxyServer.publishService(helloService, 8080);
    }
}
