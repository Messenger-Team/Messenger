package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.dto.response.*;

public interface ChatService {

    ChatCreateWithTwoUsersResponse createChatWithNameAndTwoUsers(CreateChatWithTwoUsersDto createChatDto);

    ChatCreateResponse createChatWithName(CreateChatDto createChatDto);

    ChatJoinResponse joinUserToChat(String chatId, UserNameDto userName);

    ChatGetMessagesResponse getMessagesByChatId(String chatId, Integer limit, String from);

    ChatSendMessageResponse sendMessageToChat(String chatId, String userId, String text);
}