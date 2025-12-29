package com.app.urlshortner.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShortenUrlRequest {
    private String originalUrl;
    private String customAlias;
    private LocalDateTime expiresAt;
}
