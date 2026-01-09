package com.github.wisniewsky00.github_repositories_proxy;

public record ErrorResponse(
        Integer status,
        String message
) {}
