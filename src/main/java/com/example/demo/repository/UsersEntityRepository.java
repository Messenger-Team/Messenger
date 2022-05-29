package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersEntityRepository extends JpaRepository<UsersEntity, String> {

    Optional<UsersEntity> findUsersEntityByUserName(String userName);
}