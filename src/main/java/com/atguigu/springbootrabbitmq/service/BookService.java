package com.atguigu.springbootrabbitmq.service;

import com.atguigu.springbootrabbitmq.bean.Book;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @RabbitListener(queues = "atguigu")
    public void receive(Book book){
        System.out.println(book);
    }

    @RabbitListener(queues = "atguigu")
    public void receiveMessageProperties(Message message){
        System.out.println(message.getBody());
        System.out.println(message.getMessageProperties());
    }
}
