package com.kgc.pubsub;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MyProducer {
    //队列名 交换机名
    private static final  String eamilname="eamiltopic";
    private static final  String smsname="smstopic";
    private static final  String exchange="exchange";

    public static void main(String[] args) throws  Exception{
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        //连接
        Connection connection = factory.newConnection();
        //会话
        Channel channel = connection.createChannel();
        //声明交换机
        //1、交换机名称  2、交换机类型fanout、topic、direct、headers
        channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
        //声明队列
        channel.queueDeclare(eamilname,true,false,false,null);
        channel.queueDeclare(smsname,true,false,false,null);

        //交换机和队列绑定
        //队列名  ，交换机名，路由key
        channel.queueBind(eamilname,exchange,"");
        channel.queueBind(smsname,exchange,"");
        //发消息
        for (int i = 0; i < 10; i++) {
            String msg="hello69_"+i;
            //交换机名，路由key， 消息属性，消息内容
            channel.basicPublish(exchange,"",null,msg.getBytes());
        }
        //关闭
        channel.close();
        connection.close();


    }

}
