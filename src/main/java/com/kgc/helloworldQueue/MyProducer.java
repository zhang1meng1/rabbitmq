package com.kgc.helloworldQueue;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MyProducer {
    //队列名
    private static final  String queuename="helloqueue";

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
        //声明队列
       /**
        声明队列，如果Rabbit中没有此队列将自动创建
         param1:队列名称
         param2:是否持久化
         param3:队列是否独占此连接
         param4:队列不再使用时是否自动删除此队列
         param5:队列参数
         */
       //队列名称 ，是否持久化，队列是否独占此连接，队列不再使用时是否自动删除此队列，队列参数
        channel.queueDeclare(queuename,true,false,false,null);
        String msg="hello69";
        //发消息

        /* 消息发布方法
         param1：Exchange的名称，如果没有指定，则使用Default Exchange
         param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
         param3:消息包含的属性
         param4：消息体*/
        /*
         这里没有指定交换机，消息将发送给默认交换机，每个队列也会绑定那个默认的交换机，但是不能显示绑定或解除绑定
         默认的交换机，routingKey等于队列名称
         */
        channel.basicPublish("",queuename,null,msg.getBytes());
        //关闭
        channel.close();
        connection.close();


    }

}
