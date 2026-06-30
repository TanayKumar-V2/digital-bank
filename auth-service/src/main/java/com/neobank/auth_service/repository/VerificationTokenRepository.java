package com.neobank.auth_service.repository;

import com.neobank.auth_service.entity.VerificationToken;
import com.neobank.auth_service.entity.User;
import com.neobank.auth_service.entity.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, UUID> {

    Optional<VerificationToken>
    findTopByUserAndTypeAndUsedFalseOrderByCreatedAtDesc(

            User user,

            VerificationType type

    );

}