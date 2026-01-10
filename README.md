# GitHub Repositories Proxy
A Spring Boot application that exposes a REST API for fetching GitHub repositories of a given user.
The application acts as a proxy to the GitHub REST API and returns only non-fork repositories along with their branches and last commit SHAs.

## Features
* Fetches public GitHub repositories for a given user
* Filters out forked repositories
* Retrieves branches for each repository
* Proper error handling for non-existing users
* Covered with integration tests using WireMock

## Tech Stack
* Java 25
* Spring Boot 4.0.1
* Gradle (Kotlin DSL)
* Spring Web MVC 
* Spring RestClient
* WireMock (standalone) - integration testing
* JUnit 5

## Running the Application

### 1. Prerequisites
* Java 25
* Gradle
* GitHub Personal Access Token

### 2. Environment Variables
The application requires a GitHub Personal Access Token to communicate with the GitHub API. For more information, see [Creating fine-grained personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-fine-grained-personal-access-token)  

Set the environment variable:
```bash
export GITHUB_PERSONAL_ACCESS_TOKEN=your_token_here
```
or on Windows (PowerShell):
```powershell
$env:GITHUB_PERSONAL_ACCESS_TOKEN='your_token_here'
```

> [!WARNING]
> The token should never be committed to the repository or hardcoded in configuration files.
> Using environment variables is the recommended and safest approach.

#### Running without environment variables
For local testing only, the token can be provided directly in `application.properties`:
```properties
github.personal.access.token=your_token_here
```

### 3. Start the Application
```bash
./gradlew bootRun
```
The application will start on port 8080 by default.

## API Endpoints
### Get repositories for a user
```bash
GET /api/{username}/repositories
```

**Example request**
```bash
GET /api/wisniewsky00/repositories
```

**Example response**
```json
[
  {
    "repositoryName": "github-repositories-proxy",
    "ownerLogin": "wisniewsky00",
    "branches": [
      {
        "name": "master",
        "commit": {
          "sha": "53c9cf68d3491b13c65c312852ebfe760a7f0581"
        }
      }
    ]
  }
]
```

## Error Handling
### User not found

**Example request**
```bash
GET /api/invalid-user/repositories
```

**Response: HTTP 404**
```json
{
  "status": 404,
  "message": "GitHub user 'invalid-user' not found"
}
```

## Testing
### Integration Tests
The project contains integration tests that verify:
* Successful retrieval of repositories and branches
* Filtering out forked repositories 
* Proper handling of non-existing users (404)

External GitHub API calls are mocked using **WireMock**.

Run tests with:
```bash
./gradlew test
```

## Architecture Notes
* The GitHub API base URL is configurable in application.properties to allow easy testing and environment separation
* Error handling is implemented using domain-specific exceptions and a global controller advice