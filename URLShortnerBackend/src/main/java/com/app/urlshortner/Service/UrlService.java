package com.app.urlshortner.Service;

import com.app.urlshortner.DetailsRepo;
import com.app.urlshortner.Entity.Details;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UrlService {

    private final DetailsRepo detailsRepo;

    public UrlService(DetailsRepo detailsRepo) {
        this.detailsRepo = detailsRepo;
    }


    public String findUrl(String code) {
        return detailsRepo.findById(code)
                .map(Details::getOriginalUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short code not found"));
    }



    public String getShortenUrl(String url) {
        String uniqueCode;
        do {
            uniqueCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        } while (detailsRepo.existsById(uniqueCode));

        Details details = new Details();
        details.setCode(uniqueCode);
        details.setOriginalUrl(url);

        detailsRepo.save(details);

         return uniqueCode;
    }
}
