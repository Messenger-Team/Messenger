package com.example.demo.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
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

        return new ChatCreateResponse(chatsEntity.get_chatId());
    }

    private ChatsEntity generateChatsEntityByName(CreateChatDto createChatDto) {
        String chatId = UUID.randomUUID().toString();
        ChatsEntity chatsEntity = new ChatsEntity();
        chatsEntity.set_chatName(createChatDto.getChatName());
        chatsEntity.set_chatId(chatId);
        chatsEntity.set_createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        return chatsEntity;
    }

    @Override
    public ChatJoinResponse joinUserToChat(String chatId, UserNameDto userNameDto) {
        String userName = userNameDto.getUserName();
        UsersEntity user;
        Optional<UsersEntity> userEntity = usersEntityRepository.findUsersEntityByUserId(userName);
        if (userEntity.isEmpty()) {
            user = new UsersEntity();
            user.set_userId(userName);
            user.set_createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
            user.set_userTimezone(DEFAULT_TIMEZONE);
            user = usersEntityRepository.save(user);
        } else {
            user = userEntity.get();
        }

        ChatsEntity chat = findChatByChatId(chatId);

        ChatsUsersEntity chatsUsersEntity = new ChatsUsersEntity();
        chatsUsersEntity.set_usersByUserId(user);
        chatsUsersEntity.set_chatsByChatId(chat);
        chatsUsersEntity.set_isPresent(true);

        ChatsUsersEntity created = chatsUsersEntityRepository.save(chatsUsersEntity);

        return new ChatJoinResponse(created.get_usersByUserId().get_userId());
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
                         m.get_usersBySenderId().get_userId() , m.get_message() , m.get_createdAt().toString()))
                .collect(Collectors.toList());

        Cursor iterator = null;
        if (!messages.isEmpty()) {
            iterator = new Cursor(messagesEntities.get(messagesEntities.size() - 1).get_createdAt().toString());
        }

        ChatGetMessagesResponse response = new ChatGetMessagesResponse(messages, iterator);
        chatCacheService.messagesResponseCachePutData(requestData, response);

        return response;
    }

    @Override
    public ChatSendMessageResponse sendMessageToChat(String chatId, String userId,
                                                     String text) {
        UsersEntity user = findUserByUserId(userId);
        ChatsEntity chat = findChatByChatId(chatId);

        MessagesEntity messagesEntity = new MessagesEntity();
        messagesEntity.set_message(text);
        messagesEntity.set_chatsByChatId(chat);
        messagesEntity.set_usersBySenderId(user);
        messagesEntity.set_messagesStatus(MessagesStatus.CREATED.name());
        messagesEntity.set_createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        messagesEntity.set_messageId(UUID.randomUUID().toString());

        MessagesEntity createdMessage = messagesEntityRepository.save(messagesEntity);

        chatCacheService.evictByChatId(chatId);
        return new ChatSendMessageResponse(createdMessage.get_messageId());
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
        Optional<UsersEntity> userEntityFirst = usersEntityRepository.findUsersEntityByUserId(createChatDto.getUserNameFirst());
        Optional<UsersEntity> userEntitySecond = usersEntityRepository.findUsersEntityByUserId(createChatDto.getUserNameSecond());
        if ((!userEntityFirst.isEmpty())&&(!userEntitySecond.isEmpty())) {
            UsersEntity userFirst = userEntityFirst.get();
            UsersEntity userSecond = userEntitySecond.get();
            Collection<ChatsUsersEntity>userFirstChats = userFirst.get_chatsUsersByUserId();
            Collection<ChatsUsersEntity>userSecondChats = userSecond.get_chatsUsersByUserId();
            for (ChatsUsersEntity chatUser:userFirstChats) {
                for(ChatsUsersEntity chatUserSec:userSecondChats){
                    if(chatUserSec.get_chatsByChatId().get_chatId().equals(chatUser.get_chatsByChatId().get_chatId())){
                        return new ChatCreateWithTwoUsersResponse(chatUserSec.get_chatsByChatId().get_chatId());
                    }
                }
            }
        }

        String chatId = UUID.randomUUID().toString();
        ChatsEntity chatsEntity = new ChatsEntity();
        chatsEntity.set_chatName(createChatDto.getChatName());
        chatsEntity.set_chatId(chatId);
        chatsEntity.set_createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
        ChatsEntity chatsEntityCreated = chatsEntityRepository.save(chatsEntity);
        ChatsUsersEntity usersEntityFirst = AddUserToChat(chatsEntityCreated, createChatDto.getUserNameFirst());
        ChatsUsersEntity usersEntitySecond = AddUserToChat(chatsEntityCreated, createChatDto.getUserNameSecond());

        return new ChatCreateWithTwoUsersResponse(chatsEntityCreated.get_chatId());
    }

    private ChatsUsersEntity AddUserToChat(ChatsEntity chatsEntity, String userName){
        UsersEntity user;
        Optional<UsersEntity> userEntity = usersEntityRepository.findUsersEntityByUserId(userName);
        if (userEntity.isEmpty()) {
            user = new UsersEntity();
            user.set_userId(userName);
            user.set_createdAt(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
            user.set_userTimezone(DEFAULT_TIMEZONE);
            user = usersEntityRepository.save(user);
        } else {
            user = userEntity.get();
        }

        ChatsUsersEntity chatsUsersFirstEntity = new ChatsUsersEntity();
        chatsUsersFirstEntity.set_usersByUserId(user);
        chatsUsersFirstEntity.set_chatsByChatId(chatsEntity);
        chatsUsersFirstEntity.set_isPresent(true);

        return chatsUsersEntityRepository.save(chatsUsersFirstEntity);
    }
}