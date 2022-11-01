package com.example.demo.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.Cursor;
import com.example.demo.dto.SearchMessageDto;
import com.example.demo.dto.response.GetHistoryResponse;
import com.example.demo.dto.response.GetTaskResponse;
import com.example.demo.dto.response.HistoryGetMessagesResponse;
import com.example.demo.service.SearchMessagesService;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/chats/search")
@Validated
public class ChatHistoryController {

    private final SearchMessagesService _searchMessagesService;

    /**
     * POST /v1/chats/search Запустить процесс поиска по истории сообщений для чатов, в которых есть
     * данный пользователь
     *
     * @param searchMessageDto (required)
     * @return Задача создана успешно (status code 201) or * &#x60;bad-parameters&#x60; - неправильный
     * формат входных параметров  (status code 400) or * &#x60;user-not-found&#x60; - данный
     * пользователь не найден  (status code 404) or unexpected server error (status code 200)
     */
    @PostMapping()
    public ResponseEntity<GetHistoryResponse> startSearchForMessages(
            @Valid @RequestBody SearchMessageDto searchMessageDto) {
        return new ResponseEntity<>(_searchMessagesService.startSearchForMessages(searchMessageDto),
                HttpStatus.CREATED);

    }

    /**
     * GET /v1/chats/search/status/{task_id} Получить статус таски на обработку
     *
     * @param taskId (required)
     * @return Получить статус задачи (status code 200) or * &#x60;bad-parameters&#x60; - неправильный
     * формат входных параметров  (status code 400) or * &#x60;task-not-found&#x60; - задача на
     * обработку не найдена  (status code 404) or unexpected server error (status code 200)
     */
    @GetMapping("/status/{task_id}")
    public ResponseEntity<GetTaskResponse> checkTaskStatus(@PathVariable("task_id") String taskId) {
        return new ResponseEntity<>(_searchMessagesService.checkTaskStatus(taskId), HttpStatus.OK);
    }

    /**
     * GET /v1/chats/search/{task_id}/messages получить список сообщений из чатов
     *
     * @param taskId (required)
     * @param limit  не больше стольки сообщений хотим получить в ответе (required)
     * @param from   указатель для сервера, обозначающий место, с которого стоит продолжить получение
     *               сообщений; если не указан, то сервер должен вернуть limit сообщений, начиная с
     *               самого последнего сообщения в истории (optional)
     * @return action was completed successfully (status code 200) or * &#x60;bad-parameters&#x60; -
     * неправильный формат входных параметров  (status code 400) or * &#x60;task-not-found&#x60; -
     * указанная задача не существует  (status code 404) or unexpected server error (status code 200)
     */
    @GetMapping("/{task_id}/messages")
    public ResponseEntity<HistoryGetMessagesResponse> getMessagesListFromTask(
            @PathVariable("task_id") String taskId,
            @NotNull @Min(1) @Max(200) @Valid @RequestParam(value = "limit") Integer limit,
            @Valid Cursor from) {
        return new ResponseEntity<>(_searchMessagesService.getMessagesListFromTask(taskId, limit, from),
                HttpStatus.OK);
    }
}