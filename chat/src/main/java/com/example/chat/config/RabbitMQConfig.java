package com.example.chat.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String FRIEND_REQUEST_QUEUE = "friend_request_queue";
    public static final String FRIEND_REQUEST_EXCHANGE = "friend_request_exchange";
    public static final String ROUTING_KEY = "friend.request";

    @Bean
    Queue friendRequestQueue() {
        return new Queue(FRIEND_REQUEST_QUEUE, false);
    }

    @Bean
    DirectExchange friendRequestExchange() {
        return new DirectExchange(FRIEND_REQUEST_EXCHANGE);
    }

    @Bean
    Binding binding(Queue friendRequestQueue, DirectExchange friendRequestExchange) {
        return BindingBuilder.bind(friendRequestQueue).to(friendRequestExchange).with(ROUTING_KEY);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}