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

import lombok.Setter;

@Entity
@Setter
@Table(name = "chats_users")
public class ChatsUsersEntity {

    private Long _chatsUsersId;
    private Boolean _isPresent;
    private ChatsEntity _chatsByChatId;
    private UsersEntity _usersByUserId;

    @Id
    @Column(name = "chats_users_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long get_chatsUsersId() {
        return _chatsUsersId;
    }

    @Basic
    @Column(name = "is_present")
    public Boolean get_isPresent() {
        return _isPresent;
    }

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    public ChatsEntity get_chatsByChatId() {
        return _chatsByChatId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public UsersEntity get_usersByUserId() {
        return _usersByUserId;
    }
}
