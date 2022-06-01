package com.example.demo.service;

import com.example.demo.dto.response.ChatCreateResponse;
import com.example.demo.dto.response.ChatGetMessagesResponse;
import com.example.demo.dto.response.ChatJoinResponse;
import com.example.demo.dto.response.ChatSendMessageResponse;
import com.example.demo.dto.CreateChatDto;
import com.example.demo.dto.Cursor;
import com.example.demo.dto.MessageDto;
import com.example.demo.dto.UserNameDto;

public interface ChatService {

    ChatCreateResponse createChatWithName(CreateChatDto createChatDto);

    ChatJoinResponse joinUserToChat(String chatId, UserNameDto userName);

    ChatGetMessagesResponse getMessagesByChatId(String chatId, Integer limit, String from);

    ChatSendMessageResponse sendMessageToChat(String chatId, String userId, MessageDto messageDto);
}