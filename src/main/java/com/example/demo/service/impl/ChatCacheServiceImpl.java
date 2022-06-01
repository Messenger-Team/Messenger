package com.example.demo.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import com.example.demo.cache.ChatIdKeyFromCache;
import com.example.demo.dto.response.ChatGetMessagesResponse;
import com.example.demo.service.ChatCacheService;

@Service
public class ChatCacheServiceImpl implements ChatCacheService {

    private final Cache<String, Cache<ChatIdKeyFromCache, ChatGetMessagesResponse>> chatIdCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();


    @Override
    public Optional<ChatGetMessagesResponse> getMessagesResponseCacheByKey(ChatIdKeyFromCache requestData) {
        Optional<Cache<ChatIdKeyFromCache, ChatGetMessagesResponse>> messagesResponseCache = Optional.ofNullable(
                chatIdCache.getIfPresent(requestData.getChatId()));
        if (messagesResponseCache.isPresent()) {
            return Optional.ofNullable(messagesResponseCache.get().getIfPresent(requestData));
        }

        return Optional.empty();
    }

    @Override
    public void messagesResponseCachePutData(ChatIdKeyFromCache requestData, ChatGetMessagesResponse response) {
        Optional<Cache<ChatIdKeyFromCache, ChatGetMessagesResponse>> messagesResponseCache = Optional.ofNullable(
                chatIdCache.getIfPresent(requestData.getChatId()));

        if (messagesResponseCache.isPresent()) {
            messagesResponseCache.get().put(requestData, response);
        } else {
            Cache<ChatIdKeyFromCache, ChatGetMessagesResponse> newMessagesResponseCache = Caffeine.newBuilder()
                    .expireAfterWrite(1, TimeUnit.MINUTES)
                    .maximumSize(100)
                    .build();

            newMessagesResponseCache.put(requestData, response);
            chatIdCache.put(requestData.getChatId(), newMessagesResponseCache);
        }
    }

    @Override
    public void evictByChatId(String chatId) {
        chatIdCache.invalidate(chatId);
    }
}