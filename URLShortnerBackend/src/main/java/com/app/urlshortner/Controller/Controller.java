package com.app.urlshortner.Controller;
import com.app.urlshortner.Service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class Controller {

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
    public String getShortenUrl(@RequestBody String url){
        return urlService.getShortenUrl(url);
    }

}
