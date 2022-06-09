package com.example.demo;

import com.example.demo.service.ChatService;
import org.apache.commons.collections.CollectionUtils;
import com.example.demo.dto.CreateChatWithTwoUsersDto;
import com.example.demo.dto.Message;
import com.example.demo.dto.MessageDto;
import com.example.demo.dto.response.ChatCreateWithTwoUsersResponse;
import com.example.demo.dto.response.ChatGetMessagesResponse;
import com.example.demo.service.impl.ChatServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class MessengerApplicationTests {

	public static final String CHAT_NAME = "chat_1";
	public static final String USER_NAME_1 = "user_1";
	public static final String USER_NAME_2 = "user_2";
	public static final String MESSAGE_1 = "hello";
	public static final String MESSAGE_2 = "goodbye";

	@Autowired
	ChatService service;


	@Test
	public void messengerDefaultTest() {
		CreateChatWithTwoUsersDto dto = new CreateChatWithTwoUsersDto(CHAT_NAME, USER_NAME_1, USER_NAME_2);
		ChatCreateWithTwoUsersResponse response = service.createChatWithNameAndTwoUsers(dto);
		Assertions.assertNotNull(response);
	}

	@Test
	public void messengerMessagesDefaultTest() {
		CreateChatWithTwoUsersDto dto = new CreateChatWithTwoUsersDto(CHAT_NAME, USER_NAME_1, USER_NAME_2);
		ChatCreateWithTwoUsersResponse response = service.createChatWithNameAndTwoUsers(dto);

		String chatId = response.getChatId();
		String firstUserId = response.getUserFirstId();
		String secondUserId = response.getUserSecondId();

		MessageDto messageDto1 = new MessageDto(new Message(MESSAGE_1));
		MessageDto messageDto2 = new MessageDto(new Message(MESSAGE_2));

		service.sendMessageToChat(chatId, firstUserId, messageDto1);
		service.sendMessageToChat(chatId, secondUserId, messageDto2);

        ChatGetMessagesResponse chatGetMessagesResponse = service.getMessagesByChatId(chatId, 100, "");

        Message[] messages = new Message[] {new Message(MESSAGE_1), new Message(MESSAGE_2)};
        List<Message> messagesToCheck = Arrays.asList(messages);
		List<Message> a = chatGetMessagesResponse.getMessages();
        Assertions.assertTrue(CollectionUtils.isEqualCollection(messagesToCheck, chatGetMessagesResponse.getMessages()));
	}

}
