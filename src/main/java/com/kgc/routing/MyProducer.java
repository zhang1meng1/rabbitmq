package com.kgc.routing;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MyProducer {
    //队列名 交换机名
    private static final  String eamilname="eamilroutingtopic";
    private static final  String smsname="smsroutingtopic";
    private static final  String exchange="routingexchange";

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
        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
        //声明队列
        //队列名称 ，是否持久化，队列是否独占此连接，队列不再使用时是否自动删除此队列，队列参数
        channel.queueDeclare(eamilname,true,false,false,null);
        channel.queueDeclare(smsname,true,false,false,null);

        //交换机和队列绑定
        //队列名  ，交换机名，路由key
        channel.queueBind(eamilname,exchange,"eamilroutingkey");
        channel.queueBind(smsname,exchange,"smsroutingkey");
        //发消息
        for (int i = 0; i < 10; i++) {
            String msg1="email:"+i;
            String msg2="sms:"+i;
            //交换机名，路由key， 消息属性，消息内容
            channel.basicPublish(exchange,"eamilroutingkey",null,msg1.getBytes());
            channel.basicPublish(exchange,"smsroutingkey",null,msg2.getBytes());
        }
        //关闭
        channel.close();
        connection.close();


    }

}
