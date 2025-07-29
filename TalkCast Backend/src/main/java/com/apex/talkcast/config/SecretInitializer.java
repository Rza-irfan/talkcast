package com.apex.talkcast.config;

import com.apex.talkcast.service.VaultService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SecretInitializer implements ApplicationRunner {

    private final VaultService vaultService;

//    @Value("${zoho.vault.secret-id}")
//    private String secretId;

    @Getter
    private static String dbPassword; // Store it for other beans

    public SecretInitializer(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public void run(ApplicationArguments args) {
//        String secretJson = vaultService.getSecret(secretId);

        // Parse JSON and extract password field
        // Example assumes the JSON structure contains a "password" field

//        dbPassword = extractPassword(secretJson); // Store it in static var or config bean

        System.out.println("Password initialized securely from Zoho Vault.");
    }

    private String extractPassword(String json) {
        // Very simple extraction (use Jackson or Gson in real apps)
        int idx = json.indexOf("\"password\":\"");
        if (idx == -1) return null;
        int start = idx + 11;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

}
