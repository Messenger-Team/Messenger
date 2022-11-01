package com.example.demo.controller;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.example.demo.dto.*;
import com.example.demo.dto.response.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ChatService;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/v1/chats")
@Validated
@Slf4j
public class ChatController {

    private final ChatService _chatService;

    /**
     * POST /v1/chats создать чат с именем chat_name
     *
     * @param createChatDto (required)
     * @return action was completed successfully (status code 201) or * &#x60;bad-parameters&#x60; -
     * неправильный формат входных параметров  (status code 400) or unexpected server error (status
     * code 200)
     */
    @CrossOrigin
    @PostMapping
    public ResponseEntity<ChatCreateResponse> createChatWithName(
            @RequestBody @Valid CreateChatDto createChatDto) {
        return new ResponseEntity<>(_chatService.createChatWithName(createChatDto), HttpStatus.CREATED);
    }

    /**
     * POST /v1/chats/default получить чат с именем chat_name и добавить юзеров с именами user_name_first и user_name_second
     *
     * @param createChatWithTwoUsersDto (required)
     * @return action was completed successfully (status code 201) or * &#x60;bad-parameters&#x60; -
     * неправильный формат входных параметров  (status code 400) or unexpected server error (status
     * code 200)
     */
    @CrossOrigin
    @PostMapping("/default")
    public ResponseEntity<ChatCreateWithTwoUsersResponse> createChatWithNameAnd2Users(
            @RequestBody @Valid CreateChatWithTwoUsersDto createChatWithTwoUsersDto) {
        return new ResponseEntity<>(_chatService.createChatWithNameAndTwoUsers(createChatWithTwoUsersDto), HttpStatus.CREATED);
    }
    /**
     * POST /v1/chats/{chat_id}/users
     * добавить пользователя user_name в чат chat_id
     *
     * @param chatId id чата, полученное при создании чата (required)
     * @param userName  (required)
     * @return action was completed successfully (status code 201)
     *         or * &#x60;bad-parameters&#x60; - неправильный формат входных параметров  (status code 400)
     *         or * &#x60;chat-not-found&#x60; - указанный чат не существует  (status code 404)
     *         or unexpected server error (status code 200)
     */
    @CrossOrigin
    @PostMapping ("/{chat_id}/users")
    public ResponseEntity<ChatJoinResponse> joinUserToChat(
            @PathVariable("chat_id") String chatId,
            @RequestBody @Valid UserNameDto userName) {
        return new ResponseEntity<>(_chatService.joinUserToChat(chatId, userName), HttpStatus.CREATED);
    }

    /**
     * GET /v1/chats/{chat_id}/messages
     * получить список сообщений из чата chat_id
     *
     * @param chatId  (required)
     * @param limit не больше стольки сообщений хотим получить в ответе (required)
     * @param from указатель для сервера, обозначающий место, с которого стоит продолжить получение сообщений; если не указан, то сервер должен вернуть limit сообщений, начиная с самого первого сообщения в чате (optional)
     * @return action was completed successfully (status code 200)
     *         or * &#x60;bad-parameters&#x60; - неправильный формат входных параметров  (status code 400)
     *         or * &#x60;chat-not-found&#x60; - указанный чат не существует  (status code 404)
     *         or unexpected server error (status code 200)
     */
    @CrossOrigin
    @GetMapping ("/{chat_id}/messages")
    public ResponseEntity<ChatGetMessagesResponse> getMessagesByChatId(
            @PathVariable("chat_id") String chatId,
            @RequestParam(value = "limit") @NotNull @Min(1) @Max(1000) Integer limit,
            @RequestParam Optional<String> from) {
        log.info("************* id: {}, limit: {}, from: {}", chatId, limit, from.orElse(null));
        return new ResponseEntity<>(_chatService.getMessagesByChatId(chatId, limit, from.orElse(null)), HttpStatus.OK);
    }

    /**
     * POST /v1/chats/{chat_id}/messages
     * отправить в чат chat_id сообщение message
     *
     * @param chatId  (required)
     * @param userId  (required)
     * @param text  (required)
     * @return action was completed successfully (status code 201)
     *         or * &#x60;bad-parameters&#x60; - неправильный формат входных параметров  (status code 400)
     *         or * &#x60;chat-not-found&#x60; - указанный чат не существует * &#x60;user-not-found&#x60; - в указанном чате нет указанного пользователя  (status code 404)
     *         or unexpected server error (status code 200)
     */
    @CrossOrigin
    @PostMapping ("/{chat_id}/messages")
    public ResponseEntity<ChatSendMessageResponse> sendMessageToChat(
            @PathVariable("chat_id") String chatId,
            @RequestParam(value = "user_id") @NotNull @Valid String userId,
            @RequestParam(value = "text") @NotNull @Valid String text) {
        return new ResponseEntity<>(_chatService.sendMessageToChat(chatId, userId, text), HttpStatus.CREATED);
    }
}
