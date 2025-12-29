package com.app.urlshortner.Service;

import com.app.urlshortner.Repository.DetailsRepo;
import com.app.urlshortner.Entity.Details;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UrlService {

    private final DetailsRepo detailsRepo;

    public UrlService(DetailsRepo detailsRepo) {
        this.detailsRepo = detailsRepo;
    }

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public String findUrl(String code) {
        return detailsRepo.findActiveByCode(code, LocalDateTime.now())
                .map(Details::getOriginalUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found or expired."));
    }


    public String getShortenUrl(String originalUrl,String customAlias,LocalDateTime expiresAt) {
        String code;
        boolean isCustom = false;

        if(customAlias!=null && !customAlias.isBlank()) {
            validateCustomAlias(customAlias);
            if(detailsRepo.existsByCode(customAlias)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Custom alias already taken.");
            }
            code = customAlias;
            isCustom = true;
        }else{
            code = generateUniqueCode();
        }
        Details details = Details.builder()
                .code(code)
                .OriginalUrl(originalUrl)
                .expiresAt(expiresAt)
                .customAlias(isCustom)
                .build();
        detailsRepo.save(details);
        return code;
    }

    private String generateUniqueCode() {
        String code;
        do{
            code = randomBase62(7);
        }while(detailsRepo.existsByCode(code));
        return code;
    }

    private String randomBase62(int length){
        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int i=0;i<length;i++){
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }
        return sb.toString();
    }

    private void validateCustomAlias(String customAlias) {
        if(!customAlias.matches("^[a-zA-Z0-9_-]{3,20}")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alias must be 3–20 chars (a-z, A-Z, 0-9, _, -)");
        }
    }
}
