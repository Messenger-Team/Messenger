package com.example.demo.service;

import com.example.demo.dto.Cursor;
import com.example.demo.dto.SearchMessageDto;
import com.example.demo.dto.response.GetHistoryResponse;
import com.example.demo.dto.response.GetTaskResponse;
import com.example.demo.dto.response.HistoryGetMessagesResponse;

public interface SearchMessagesService {

    GetHistoryResponse startSearchForMessages(SearchMessageDto searchMessageDto);

    GetTaskResponse checkTaskStatus(String taskId);

    HistoryGetMessagesResponse getMessagesListFromTask(String taskId, Integer limit, Cursor from);
}
