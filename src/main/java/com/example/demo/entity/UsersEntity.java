package com.example.demo.entity;

import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UsersEntity {

    private String _userId;
    private Timestamp _createdAt;
    private String _userTimezone;
    private Collection<ChatsUsersEntity> _chatsUsersByUserId;
    private Collection<MessagesEntity> _messagesByUserId;

    @Id
    @Column(name = "user_id")
    public String get_userId() {
        return _userId;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp get_createdAt() {
        return _createdAt;
    }

    @Basic
    @Column(name = "user_timezone")
    public String get_userTimezone() {
        return _userTimezone;
    }

    @OneToMany(mappedBy = "usersByUserId", fetch = FetchType.EAGER)
    public Collection<ChatsUsersEntity> get_chatsUsersByUserId() {
        return _chatsUsersByUserId;
    }

    @OneToMany(mappedBy = "usersBySenderId")
    public Collection<MessagesEntity> get_messagesByUserId() {
        return _messagesByUserId;
    }
}