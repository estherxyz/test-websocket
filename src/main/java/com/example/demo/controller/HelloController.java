package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.ChatWebSocketHandler;


@RestController
public class HelloController {
    
	private ChatWebSocketHandler socketHandler = new ChatWebSocketHandler();


    @GetMapping("/hello")
	public String hello() {
		socketHandler.sendMessageToUser("u123", "hello");

		return "Greetings from Spring Boot!";
	}


}
