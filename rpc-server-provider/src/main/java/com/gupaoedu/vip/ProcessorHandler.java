package com.gupaoedu.vip;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class ProcessorHandler implements Runnable {
    private Socket socket;
    private Object serviceImpl;

    public ProcessorHandler(Socket socket, Object serviceImpl) {
        this.socket = socket;
        this.serviceImpl = serviceImpl;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        try {
            ois = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            Object result = callMethod(rpcRequest);

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                    ois = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                    oos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object callMethod(RpcRequest request) throws Exception {
        //反射调用
        Object[] prams = request.getParameters(); //拿到客户端请求的参数
        Class<?>[] types = new Class[prams.length]; //获得每个参数的类型
        for (int i = 0; i < prams.length; i++) {
            types[i] = prams[i].getClass();
        }
        Class clazz = Class.forName(request.getClassName()); //跟去请求的类进行加载
        Method method = clazz.getMethod(request.getMethodName(), types); //sayHello, saveUser找到这个类中的方法
        Object result = method.invoke(serviceImpl, prams);//HelloserviceImplImpl 进行反射调用
        return result;
    }
}
