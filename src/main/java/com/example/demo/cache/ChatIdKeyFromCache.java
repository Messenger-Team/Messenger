package com.example.demo.cache;

import lombok.Data;

@Data
public class ChatIdKeyFromCache {

    private final String _chatId;
    private final Integer _limit;
    private final String _from;
}
