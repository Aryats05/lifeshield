package com.lifeshield.state;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStateRepository
        extends JpaRepository<UserState, Long> {

    List<UserState> findAllByUser_Id(Long userId);

    Optional<UserState> findByUser_IdAndStateKey(
            Long userId,
            String stateKey
    );
}