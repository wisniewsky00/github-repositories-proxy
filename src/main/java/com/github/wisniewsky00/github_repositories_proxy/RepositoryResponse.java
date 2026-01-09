package com.github.wisniewsky00.github_repositories_proxy;

import java.util.List;

public record RepositoryResponse(
        String repositoryName,
        String ownerLogin,
        List<Branch> branches
) {
}
