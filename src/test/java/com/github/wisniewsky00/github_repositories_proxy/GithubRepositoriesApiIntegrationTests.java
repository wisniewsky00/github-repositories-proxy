package com.github.wisniewsky00.github_repositories_proxy;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8089)
class GithubRepositoriesApiIntegrationTests {

	@LocalServerPort
	private int port;

	private RestClient client;

    @BeforeEach
	void setUp() {
		client = RestClient.create("http://localhost:" + port);
	}

	@DynamicPropertySource
	static void overrideGithubBaseUrl(DynamicPropertyRegistry registry) {
		registry.add("github.api.base-url", () -> "http://localhost:8089");
		registry.add("github.personal.access.token", () -> "test-token");
	}

	@Test
	void getRepositories_whenUserExists_returnRepositories() {

		stubFor(get("/users/testuser/repos").willReturn(okJson("""
                [
                  {
                    "name": "my-repo",
                    "owner": { "login": "testuser" },
                    "fork": false
                  },
                  {
                    "name": "forked-repo",
                    "owner": { "login": "testuser" },
                    "fork": true
                  }
                ]
                """)));

		stubFor(get("/repos/testuser/my-repo/branches").willReturn(okJson(
				"""
				[
				  {
					"name": "main",
					"commit": { "sha": "abc123" }
				  }
				]
				""")));

		List<RepositoryResponse> responseList = client.get()
				.uri("/api/testuser/repositories")
				.retrieve()
				.body(new ParameterizedTypeReference<>() {});


		assertThat(responseList).hasSize(1);
		assertThat(responseList.get(0).repositoryName()).isEqualTo("my-repo");
		assertThat(responseList.get(0).ownerLogin()).isEqualTo("testuser");

		assertThat(responseList.get(0).branches()).hasSize(1);
		assertThat(responseList.get(0).branches().get(0).name()).isEqualTo("main");
		assertThat(responseList.get(0).branches().get(0).commit().sha()).isEqualTo("abc123");

		verify(getRequestedFor(urlEqualTo("/users/testuser/repos")));
		verify(getRequestedFor(urlEqualTo("/repos/testuser/my-repo/branches")));
	}

	@Test
	void getRepositories_whenUserNotFound_returns404() {

		stubFor(get(urlEqualTo("/users/invalid-user/repos"))
				.willReturn(notFound()));

		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() ->
				client.get()
						.uri("/api/invalid-user/repositories")
						.retrieve()
						.toEntity(ErrorResponse.class)
		);

		assertThat(exception)
				.isInstanceOf(org.springframework.web.client.HttpClientErrorException.NotFound.class);

		var ex = (org.springframework.web.client.HttpClientErrorException.NotFound) exception;

		assertThat(ex.getStatusCode().value()).isEqualTo(404);
		assertThat(ex.getResponseBodyAsString())
				.contains("GitHub user 'invalid-user' not found");

		verify(getRequestedFor(urlEqualTo("/users/invalid-user/repos")));
	}


}
