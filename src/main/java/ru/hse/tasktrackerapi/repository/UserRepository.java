package ru.hse.tasktrackerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.tasktrackerapi.store.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}