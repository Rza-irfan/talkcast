package com.apex.talkcast.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class YouTubeClientInterceptor implements HandlerInterceptor {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "TalkCast";

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring("Bearer ".length());
            AccessToken token = new AccessToken(accessToken, null);
            GoogleCredentials credentials = GoogleCredentials.create(token);
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

            YouTube youtube = new YouTube.Builder(transport, JSON_FACTORY, requestInitializer)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Store the client in request attribute or thread-local for use in controllers/services
            request.setAttribute("youtubeClient", youtube);
        }
        return true;
    }
}
