package com.example.demo.repository;

import com.example.demo.entity.ChatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatsEntityRepository extends JpaRepository<ChatsEntity, String> {

}