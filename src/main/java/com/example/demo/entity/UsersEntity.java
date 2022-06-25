package com.example.demo.entity;

import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    private String userId;
    private Timestamp createdAt;
    private String userTimezone;
    private Collection<ChatsUsersEntity> chatsUsersByUserId;
    private Collection<MessagesEntity> messagesByUserId;

    @Id
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Basic
    @Column(name = "user_timezone")
    public String getUserTimezone() {
        return userTimezone;
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<ChatsUsersEntity> getChatsUsersByUserId() {
        return chatsUsersByUserId;
    }

    @OneToMany(mappedBy = "usersBySenderId")
    public Collection<MessagesEntity> getMessagesByUserId() {
        return messagesByUserId;
    }
}