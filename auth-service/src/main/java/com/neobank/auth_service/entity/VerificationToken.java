package com.neobank.auth_service.entity;

import com.neobank.auth_service.entity.enums.VerificationType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public VerificationType getType() {
        return type;
    }

    public void setType(VerificationType type) {
        this.type = type;
    }

    @Id
    private UUID id;

    @Column(nullable = false)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VerificationType type;

    @Column(nullable = false)
    private Boolean used;

    public VerificationToken() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        createdAt = LocalDateTime.now();

        used = false;
    }
}