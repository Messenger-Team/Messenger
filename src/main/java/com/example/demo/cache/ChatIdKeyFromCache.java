package com.example.demo.cache;

import lombok.Data;
import com.example.demo.dto.Cursor;

@Data
public class ChatIdKeyFromCache {

    private final String chatId;
    private final Integer limit;
    private final String from;
}
