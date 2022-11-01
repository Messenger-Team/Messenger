package com.example.demo;

import com.example.demo.service.ChatService;
import com.example.demo.dto.CreateChatWithTwoUsersDto;
import com.example.demo.dto.Message;
import com.example.demo.dto.MessageDto;
import com.example.demo.dto.response.ChatCreateWithTwoUsersResponse;
import com.example.demo.dto.response.ChatGetMessagesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessengerApplicationTests {

    public static final String CHAT_NAME = "chat_1";
    public static final String USER_NAME_1 = "user_1";
    public static final String USER_NAME_2 = "user_2";
    public static final String MESSAGE_1 = "hello";
    public static final String MESSAGE_2 = "goodbye";
    private static final String ID = "1";

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
        String firstUserId = dto.getUserNameFirst();
        String secondUserId = dto.getUserNameSecond();

        service.sendMessageToChat(chatId, firstUserId, MESSAGE_1);
        service.sendMessageToChat(chatId, secondUserId, MESSAGE_2);

        ChatGetMessagesResponse chatGetMessagesResponse = service.getMessagesByChatId(chatId, 100, "");
        String[] sentMessages = new String[]{MESSAGE_1, MESSAGE_2};
        Object[] savedMessages = chatGetMessagesResponse.getMessages().stream().map(Message::getText).toArray();

        Assertions.assertArrayEquals(sentMessages, savedMessages);
    }

    @Test
    public void sendMessageToChatTest() {
        String chatId = setupChat();
        ChatGetMessagesResponse chatGetMessagesResponse = service.getMessagesByChatId(chatId, 100, "");
        String[] sentMessages = new String[]{MESSAGE_1, MESSAGE_2};
        Object[] savedMessages = chatGetMessagesResponse.getMessages().stream().map(Message::getText).toArray();

        Assertions.assertArrayEquals(sentMessages, savedMessages);
    }


    public String setupChat() {
        CreateChatWithTwoUsersDto dto = new CreateChatWithTwoUsersDto(CHAT_NAME, USER_NAME_1, USER_NAME_2);
        ChatCreateWithTwoUsersResponse response = service.createChatWithNameAndTwoUsers(dto);

        String chatId = response.getChatId();
        String firstUserId = dto.getUserNameFirst();
        String secondUserId = dto.getUserNameSecond();

        service.sendMessageToChat(chatId, firstUserId, MESSAGE_1);
        service.sendMessageToChat(chatId, secondUserId, MESSAGE_2);

        return chatId;
    }
}
