package com.app.urlshortner.Controller;
import com.app.urlshortner.DTO.ShortenUrlRequest;
import com.app.urlshortner.DTO.ShortenUrlResponse;
import com.app.urlshortner.Service.UrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class Controller {

    @Value("${app.base-url}")
    private String baseUrl;

    private final UrlService urlService;

    public Controller(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String code){
        String originalUrl = urlService.findUrl(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }


    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> getShortenUrl(@RequestBody ShortenUrlRequest request){
        String code = urlService.getShortenUrl(
                request.getOriginalUrl(),
                request.getCustomAlias(),
                request.getExpiresAt()
        );
        String shortenUrl = baseUrl+"/api/"+code;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ShortenUrlResponse(code, shortenUrl));
    }

}
