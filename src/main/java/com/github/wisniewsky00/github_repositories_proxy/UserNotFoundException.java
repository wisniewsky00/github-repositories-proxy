package com.github.wisniewsky00.github_repositories_proxy;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("GitHub user '" + username + "' not found");
    }
}
