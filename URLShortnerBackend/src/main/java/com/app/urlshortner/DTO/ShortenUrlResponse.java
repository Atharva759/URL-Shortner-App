package com.app.urlshortner.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortenUrlResponse {
    private String shortCode;
    private String shortUrl;
}
