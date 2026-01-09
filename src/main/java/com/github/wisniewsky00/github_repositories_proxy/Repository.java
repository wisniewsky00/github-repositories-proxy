package com.github.wisniewsky00.github_repositories_proxy;

record Repository(
        String name,
        Owner owner,
        Boolean fork
) {
}
