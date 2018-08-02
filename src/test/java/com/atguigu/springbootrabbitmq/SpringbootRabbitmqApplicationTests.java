package com.atguigu.springbootrabbitmq;

import com.atguigu.springbootrabbitmq.bean.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRabbitmqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Test
    public void contextLoads() {
/*
        RabbitTemplate使用send方法发送消息，
        String类型的交换器名称，String类型的routingKey，以及Message类型的message
        但是要自己构造Message类
*/
//      rabbitTemplate.send(exchangeName,routingKey,message);

/*
        RabbitTemplate使用convertAndSend方法发送消息，
        String类型的交换器名称，String类型的routingKey，以及Object类型的message
        由于使用默认MessageConverter，传入到队列的内容不易查看，可以使用json的序列化工具，自定义
*/
//      测试p2p的direct模式
        Map<String,Object> map = new HashMap<>();
        map.put("msg","helloWorld");
        map.put("list", Arrays.asList("123",true));
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",map);
    }


    @Test
    public void testReceive(){
//      使用RabbitTemplate的receiveAndConvert方法来获取并转换内容
/*
        class java.util.HashMap
        {msg=helloWorld, list=[123, true]}
*/
        Object receiveAndConvert = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(receiveAndConvert.getClass());
        System.out.println(receiveAndConvert);
    }

    @Test
    public void sendBookMessage(){
        rabbitTemplate.convertAndSend("exchange.direct","atguigu",new Book("西游记","吴承恩"));
    }

    @Test
    public void userAmqpAdmin(){
        //create DirectExchange
        amqpAdmin.declareExchange(new DirectExchange("aj"));
        //create Queue
        amqpAdmin.declareQueue(new Queue("aj-queue"));
        //create Binding bind Exchange&Queue
        /*
            Binding(String destination, Binding.DestinationType destinationType,
            String exchange, String routingKey, Map<String, Object> arguments)
         */
        amqpAdmin.declareBinding(new Binding("aj-queue", Binding.DestinationType.QUEUE,"aj","keyAJ",null));
    }
}
