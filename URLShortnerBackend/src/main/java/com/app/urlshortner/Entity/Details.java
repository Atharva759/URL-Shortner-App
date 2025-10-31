package com.app.urlshortner.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Details {

    @Id
    private String code;
    private String OriginalUrl;


    public String getOriginalUrl() {
        return OriginalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        OriginalUrl = originalUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
