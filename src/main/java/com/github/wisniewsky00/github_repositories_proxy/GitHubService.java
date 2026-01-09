package com.github.wisniewsky00.github_repositories_proxy;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubService {

    private final GitHubClient gitHubClient;

    GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    List<RepositoryResponse> getUserRepositories(String username) {
        List<RepositoryResponse> repositoryResponses = new ArrayList<>();

        List<Repository> repositoriesWithoutForks = getAllRepositories(username).stream()
                .filter(repository -> !repository.fork())
                .toList();

        repositoriesWithoutForks.forEach(repository -> {
            List<Branch> branches = getAllBranches(repository.owner().login(), repository.name());
            repositoryResponses.add(new RepositoryResponse(repository.name(), repository.owner().login(), branches));
        });

        return repositoryResponses;
    }

    private List<Repository> getAllRepositories(String username) {
        return gitHubClient.fetchAllRepositories(username);
    }

    private List<Branch> getAllBranches(String username, String repoName) {
        return gitHubClient.fetchAllBranches(username, repoName);
    }
}
