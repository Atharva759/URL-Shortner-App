package com.app.urlshortner.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name="idx_expires_at",columnList = "expiresAt"),
        @Index(name = "idx_created_at", columnList = "createdAt")
})
public class Details {

    @Id
    @Column(length = 20,nullable = false,unique = true)
    private String code;

    @Column(nullable = false,length = 2048)
    private String OriginalUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean customAlias;

}
