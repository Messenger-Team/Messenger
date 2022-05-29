package com.example.demo.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.Setter;

@Entity
@Setter
@Table(name = "chats_users", schema = "public", catalog = "postgres")
public class ChatsUsersEntity {

    private Long chatsUsersId;
    private Boolean isPresent;
    private ChatsEntity chatsByChatId;
    private UsersEntity usersByUserId;

    @Id
    @Column(name = "chats_users_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getChatsUsersId() {
        return chatsUsersId;
    }

    @Basic
    @Column(name = "is_present")
    public Boolean getIsPresent() {
        return isPresent;
    }

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    public ChatsEntity getChatsByChatId() {
        return chatsByChatId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public UsersEntity getUsersByUserId() {
        return usersByUserId;
    }
}
