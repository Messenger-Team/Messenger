package com.example.demo.repository;

import com.example.demo.entity.ChatsUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatsUsersEntityRepository extends JpaRepository<ChatsUsersEntity, Long> {

}