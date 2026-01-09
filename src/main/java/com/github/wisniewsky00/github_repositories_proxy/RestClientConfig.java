package com.github.wisniewsky00.github_repositories_proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.springframework.web.client.RestClient.*;

@Configuration
class RestClientConfig {

    @Value("${github.personal.access.token}")
    private String githubPersonalAccessToken;

    @Bean
    RestClient restClient(@Value("${github.api.base-url}") String baseUrl) {

            if (githubPersonalAccessToken == null) {
                throw new IllegalStateException("GitHub personal access token cannot be null");
            }

            return builder()
                    .baseUrl(baseUrl)
                    .defaultHeader("Accept", "application/vnd.github+json")
                    .defaultHeader("Authorization", "Bearer " + githubPersonalAccessToken)
                    .build();
    }
}
