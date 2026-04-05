package ru.winnca.spring_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.winnca.spring_security.models.MyUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> readByUsername(String username);
}
