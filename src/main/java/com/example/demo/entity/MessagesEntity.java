package com.example.demo.entity;

import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "messages")
public class MessagesEntity {

    private String _messageId;
    private String _message;
    private Timestamp _createdAt;
    private String _messagesStatus;
    private UsersEntity _usersBySenderId;
    private ChatsEntity _chatsByChatId;

    @Id
    @Column(name = "message_id")
    public String get_messageId() {
        return _messageId;
    }

    @Basic
    @Column(name = "message")
    public String get_message() {
        return _message;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp get_createdAt() {
        return _createdAt;
    }

    @Basic
    @Column(name = "messages_status")
    public String get_messagesStatus() {
        return _messagesStatus;
    }

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id", nullable = false)
    public UsersEntity get_usersBySenderId() {
        return _usersBySenderId;
    }

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    public ChatsEntity get_chatsByChatId() {
        return _chatsByChatId;
    }
}