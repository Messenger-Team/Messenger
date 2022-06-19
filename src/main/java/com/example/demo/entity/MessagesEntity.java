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

    private String messageId;
    private String message;
    private Timestamp createdAt;
    private String messagesStatus;
    private String userId;
    private UsersEntity usersBySenderId;
    private ChatsEntity chatsByChatId;

    @Id
    @Column(name = "message_id")
    public String getMessageId() {
        return messageId;
    }

    @Basic
    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Basic
    @Column(name = "user_id")
    public String getUserId() { return  userId;}

    @Basic
    @Column(name = "messages_status")
    public String getMessagesStatus() {
        return messagesStatus;
    }

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id", nullable = false)
    public UsersEntity getUsersBySenderId() {
        return usersBySenderId;
    }

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    public ChatsEntity getChatsByChatId() {
        return chatsByChatId;
    }
}