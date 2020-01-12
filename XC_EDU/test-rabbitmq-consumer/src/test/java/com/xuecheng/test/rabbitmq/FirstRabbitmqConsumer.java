package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class FirstRabbitmqConsumer {

    private static final String QUEUE="hello_world";

    public static void main(String[] args) {
        //和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机
        connectionFactory.setVirtualHost("/");
        Connection connection=null;
        Channel channel=null;
        try {
            //建立连接
            connection=connectionFactory.newConnection();
            //创建通道
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE,true,false,false,null);
            //消费方法
            DefaultConsumer consumer=new DefaultConsumer(channel){
                //接受到消息后,此方法被调用
                @Override
                public void handleDelivery(String consumerTat, Envelope envelope,AMQP.BasicProperties properties,byte[] body)throws IOException {
                    //交换机
                    String exchange=envelope.getExchange();
                    //消息ID
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息内容
                    String message=new String(body,"utf-8");
                    System.out.println("recsive message:"+message);
                }
            };
            //监听队列
            channel.basicConsume(QUEUE,true,consumer);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
