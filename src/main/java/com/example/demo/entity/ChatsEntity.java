package com.example.demo.entity;

import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "chats", schema = "public", catalog = "postgres")
public class ChatsEntity {

    private String chatId;
    private String chatName;
    private Timestamp createdAt;
    private Collection<ChatsUsersEntity> chatsUsersByChatId;
    private Collection<MessagesEntity> messagesByChatId;

    @Id
    @Column(name = "chat_id")
    public String getChatId() {
        return chatId;
    }

    @Basic
    @Column(name = "chat_name")
    public String getChatName() {
        return chatName;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @OneToMany(mappedBy = "chatsByChatId")
    public Collection<ChatsUsersEntity> getChatsUsersByChatId() {
        return chatsUsersByChatId;
    }

    @OneToMany(mappedBy = "chatsByChatId")
    public Collection<MessagesEntity> getMessagesByChatId() {
        return messagesByChatId;
    }
}
