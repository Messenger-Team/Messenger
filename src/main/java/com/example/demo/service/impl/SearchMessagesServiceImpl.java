package com.example.demo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.demo.dto.Cursor;
import com.example.demo.dto.SearchMessageDto;
import com.example.demo.dto.response.GetHistoryResponse;
import com.example.demo.dto.response.GetTaskResponse;
import com.example.demo.dto.response.HistoryGetMessagesResponse;
import com.example.demo.service.SearchMessagesService;

@Service
@AllArgsConstructor
@Slf4j
public class SearchMessagesServiceImpl implements SearchMessagesService {

    @Override
    public GetHistoryResponse startSearchForMessages(SearchMessageDto searchMessageDto) {
        return new GetHistoryResponse();
    }

    @Override
    public GetTaskResponse checkTaskStatus(String taskId) {
        return new GetTaskResponse();
    }

    @Override
    public HistoryGetMessagesResponse getMessagesListFromTask(String taskId, Integer limit,
                                                              Cursor from) {
        return new HistoryGetMessagesResponse();
    }
}