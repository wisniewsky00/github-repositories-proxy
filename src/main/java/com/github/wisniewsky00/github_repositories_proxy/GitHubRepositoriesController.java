package com.github.wisniewsky00.github_repositories_proxy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GitHubRepositoriesController {

    private final GitHubService gitHubService;

    public GitHubRepositoriesController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/{username}/repositories")
    public List<RepositoryResponse> getAllRepositories(@PathVariable String username) {
        return gitHubService.getUserRepositories(username);
    }
}
