package com.apex.talkcast.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class VaultService {

//    @Value("${zoho.vault.api-key}")
//    private String apiKey;
//
//    @Value("${zoho.vault.base-url}")
//    private String baseUrl;
//
//    public String getSecret(String secretId) {
//        String url = baseUrl + "/secrets/" + secretId;
//
//        return RestClient.create().get()
//                .uri(url)
//                .header(HttpHeaders.AUTHORIZATION, "Zoho-authtoken " + apiKey)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .retrieve()
//                .body(String.class);
//    }
}
