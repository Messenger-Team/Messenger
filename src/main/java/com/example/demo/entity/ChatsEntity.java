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
@Table(name = "chats")
public class ChatsEntity {

    private String _chatId;
    private String _chatName;
    private Timestamp _createdAt;
    private Collection<ChatsUsersEntity> _chatsUsersByChatId;
    private Collection<MessagesEntity> _messagesByChatId;

    @Id
    @Column(name = "chat_id")
    public String get_chatId() {
        return _chatId;
    }

    @Basic
    @Column(name = "chat_name")
    public String get_chatName() {
        return _chatName;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp get_createdAt() {
        return _createdAt;
    }

    @OneToMany(mappedBy = "chatsByChatId")
    public Collection<ChatsUsersEntity> get_chatsUsersByChatId() {
        return _chatsUsersByChatId;
    }

    @OneToMany(mappedBy = "chatsByChatId")
    public Collection<MessagesEntity> get_messagesByChatId() {
        return _messagesByChatId;
    }
}
