package com.example.demo.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.demo.dto.*;
import com.example.demo.dto.response.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.demo.cache.ChatIdKeyFromCache;
import com.example.demo.entity.ChatsEntity;
import com.example.demo.entity.ChatsUsersEntity;
import com.example.demo.entity.MessagesEntity;
import com.example.demo.entity.MessagesStatus;
import com.example.demo.entity.UsersEntity;
import com.example.demo.repository.ChatsEntityRepository;
import com.example.demo.repository.ChatsUsersEntityRepository;
import com.example.demo.repository.MessagesEntityRepository;
import com.example.demo.repository.UsersEntityRepository;
import com.example.demo.service.ChatCacheService;
import com.example.demo.service.ChatService;
import com.example.demo.exception.NotFoundCrudException;

@Service
@AllArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private static final String DEFAULT_TIMEZONE = "Europe/Moscow";
    // todo separate to different services
    private final ChatCacheService chatCacheService;
    private final UsersEntityRepository usersEntityRepository;
    private final MessagesEntityRepository messagesEntityRepository;
    private final ChatsUsersEntityRepository chatsUsersEntityRepository;
    private final ChatsEntityRepository chatsEntityRepository;

    @Override
    public ChatCreateResponse createChatWithName(CreateChatDto createChatDto) {
        ChatsEntity chatsEntity = chatsEntityRepository.save(generateChatsEntityByName(createChatDto));

        return new ChatCreateResponse(chatsEntity.getChatId());
    }

    private ChatsEntity generateChatsEntityByName(CreateChatDto createChatDto) {
        String chatId = UUID.randomUUID().toString();
        ChatsEntity chatsEntity = new ChatsEntity();
        chatsEntity.setChatName(createChatDto.getChatName());
        chatsEntity.setChatId(chatId);
        chatsEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        return chatsEntity;
    }

    @Override
    public ChatJoinResponse joinUserToChat(String chatId, UserNameDto userNameDto) {
        String userName = userNameDto.getUserName();
        UsersEntity user;
        Optional<UsersEntity> userEntity = usersEntityRepository.findUsersEntityByUserName(userName);
        if (userEntity.isEmpty()) {
            user = new UsersEntity();
            user.setUserName(userName);
            user.setUserId(UUID.randomUUID().toString());
            user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
            user.setUserTimezone(DEFAULT_TIMEZONE);
            user = usersEntityRepository.save(user);
        } else {
            user = userEntity.get();
        }

        ChatsEntity chat = findChatByChatId(chatId);

        ChatsUsersEntity chatsUsersEntity = new ChatsUsersEntity();
        chatsUsersEntity.setUsersByUserId(user);
        chatsUsersEntity.setChatsByChatId(chat);
        chatsUsersEntity.setIsPresent(true);

        ChatsUsersEntity created = chatsUsersEntityRepository.save(chatsUsersEntity);

        return new ChatJoinResponse(created.getUsersByUserId().getUserId());
    }

    private List<MessagesEntity> repositoryMessagesRequest(ChatsEntity chat, Integer limit, String from) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        if (from != null && !from.isBlank()) {
            Timestamp iterator = Timestamp.valueOf(from);
            return messagesEntityRepository.findMessagesByLimitFromCursor(chat, iterator, pageRequest);
        } else {
            return messagesEntityRepository.findMessagesByLimit(chat, pageRequest);
        }
    }

    @Override
    public ChatGetMessagesResponse getMessagesByChatId(String chatId, Integer limit, String from) {
        // cache before ------------------------
        ChatIdKeyFromCache requestData = new ChatIdKeyFromCache(chatId, limit, from);

        Optional<ChatGetMessagesResponse> chatGetMessagesResponse = chatCacheService.getMessagesResponseCacheByKey(
                requestData);
        if (chatGetMessagesResponse.isPresent()) {
            return chatGetMessagesResponse.get();
        }

        // request
        ChatsEntity chat = findChatByChatId(chatId);
        List<MessagesEntity> messagesEntities = repositoryMessagesRequest(chat, limit, from);

        // cache after
        List<Message> messages = messagesEntities.stream()
                .map(m -> new Message(
                         m.getUserId() , m.getMessage() , m.getCreatedAt().toString()))
                .collect(Collectors.toList());

        Cursor iterator = null;
        if (!messages.isEmpty()) {
            iterator = new Cursor(messagesEntities.get(messagesEntities.size() - 1).getCreatedAt().toString());
        }

        ChatGetMessagesResponse response = new ChatGetMessagesResponse(messages, iterator);
        chatCacheService.messagesResponseCachePutData(requestData, response);

        return response;
    }

    @Override
    public ChatSendMessageResponse sendMessageToChat(String chatId, String userId,
                                                     MessageDto messageDto) {
        UsersEntity user = findUserByUserId(userId);
        ChatsEntity chat = findChatByChatId(chatId);

        MessagesEntity messagesEntity = new MessagesEntity();
        messagesEntity.setMessage(messageDto.getMessage().getText());
        messagesEntity.setChatsByChatId(chat);
        messagesEntity.setUsersBySenderId(user);
        messagesEntity.setMessagesStatus(MessagesStatus.CREATED.name());
        messagesEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        messagesEntity.setMessageId(UUID.randomUUID().toString());
        messagesEntity.setUserId(userId);

        MessagesEntity createdMessage = messagesEntityRepository.save(messagesEntity);

        chatCacheService.evictByChatId(chatId);
        return new ChatSendMessageResponse(createdMessage.getMessageId());
    }

    private ChatsEntity findChatByChatId(String chatId) {
        Optional<ChatsEntity> chat = chatsEntityRepository.findById(chatId);
        if (chat.isEmpty()) {
            throw new NotFoundCrudException("No chat found with id " + chatId);
        }
        return chat.get();
    }

    private UsersEntity findUserByUserId(String userId) {
        Optional<UsersEntity> user = usersEntityRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundCrudException("No user found with id " + userId);
        }
        return user.get();
    }

    @Override
    public ChatCreateWithTwoUsersResponse createChatWithNameAndTwoUsers(CreateChatWithTwoUsersDto createChatDto){
        String chatId = UUID.randomUUID().toString();
        ChatsEntity chatsEntity = new ChatsEntity();
        chatsEntity.setChatName(createChatDto.getChatName());
        chatsEntity.setChatId(chatId);
        chatsEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        ChatsEntity chatsEntityCreated = chatsEntityRepository.save(chatsEntity);
        ChatsUsersEntity usersEntityFirst = AddUserToChat(chatsEntityCreated, createChatDto.getUserNameFirst());
        ChatsUsersEntity usersEntitySecond = AddUserToChat(chatsEntityCreated, createChatDto.getUserNameSecond());

        return new ChatCreateWithTwoUsersResponse(chatsEntityCreated.getChatId(), usersEntityFirst.getUsersByUserId().getUserId(), usersEntitySecond.getUsersByUserId().getUserId());
    }

    private ChatsUsersEntity AddUserToChat(ChatsEntity chatsEntity, String userName){
        UsersEntity user;
        Optional<UsersEntity> userEntity = usersEntityRepository.findUsersEntityByUserName(userName);
        if (userEntity.isEmpty()) {
            user = new UsersEntity();
            user.setUserName(userName);
            user.setUserId(UUID.randomUUID().toString());
            user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
            user.setUserTimezone(DEFAULT_TIMEZONE);
            user = usersEntityRepository.save(user);
        } else {
            user = userEntity.get();
        }

        ChatsUsersEntity chatsUsersFirstEntity = new ChatsUsersEntity();
        chatsUsersFirstEntity.setUsersByUserId(user);
        chatsUsersFirstEntity.setChatsByChatId(chatsEntity);
        chatsUsersFirstEntity.setIsPresent(true);

        return chatsUsersEntityRepository.save(chatsUsersFirstEntity);
    }
}