package com.github.wisniewsky00.github_repositories_proxy;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class GitHubClient {

    private final RestClient restClient;

    GitHubClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Repository> fetchAllRepositories(String username) {
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<Branch> fetchAllBranches(String ownerLogin, String repoName) {
        return restClient.get()
                .uri("/repos/{ownerLogin}/{repoName}/branches", ownerLogin, repoName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
