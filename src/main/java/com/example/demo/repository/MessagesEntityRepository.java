package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.List;

import com.example.demo.entity.ChatsEntity;
import com.example.demo.entity.MessagesEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MessagesEntityRepository extends JpaRepository<MessagesEntity, String> {

    @Query("select m from MessagesEntity m "
            + "where m.chatsByChatId = :chat "
            + "and m.messagesStatus <> 'DELETED' "
            + "and m.createdAt > :iterator "
            + "order by m.createdAt asc")
    List<MessagesEntity> findMessagesByLimitFromCursor(
            @Param("chat") ChatsEntity chat, @Param("iterator") Timestamp iterator, Pageable pageable);

    @Query("select m from MessagesEntity m "
            + "where m.chatsByChatId = :chat "
            + "and m.messagesStatus <> 'DELETED' "
            + "order by m.createdAt asc")
    List<MessagesEntity> findMessagesByLimit(@Param("chat") ChatsEntity chat, Pageable pageable);
}