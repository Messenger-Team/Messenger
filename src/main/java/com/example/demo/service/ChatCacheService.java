package com.example.demo.service;

import java.util.Optional;
import com.example.demo.cache.ChatIdKeyFromCache;
import com.example.demo.dto.response.ChatGetMessagesResponse;

public interface ChatCacheService {

    Optional<ChatGetMessagesResponse> getMessagesResponseCacheByKey(ChatIdKeyFromCache requestData);

    void messagesResponseCachePutData(ChatIdKeyFromCache requestData, ChatGetMessagesResponse response);

    void evictByChatId(String chatId);
}