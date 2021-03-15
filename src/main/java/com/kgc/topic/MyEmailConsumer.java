package com.kgc.topic;


import com.rabbitmq.client.*;

import java.io.IOException;

public class MyEmailConsumer {
    //队列名
    private static final  String eamilname="eamiltopicqueue";
    private static final  String exchange="topicexchange";

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
        channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
        //声明队列
        channel.queueDeclare(eamilname,true,false,false,null);
        //交换机和队列绑定
        //队列名  ，交换机名，路由key
        channel.queueBind(eamilname,exchange,"inform.#.email.#");
        //消费者
        DefaultConsumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //消息内容
                String msg = new String(body,"utf-8");
                System.out.println(msg);
            }
        };
        //监听设置
        /**
         * 1、队列名称
         * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
         * 3、消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(eamilname,true,consumer);
    }
}
