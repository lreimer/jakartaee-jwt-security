package cloud.nativ.jakartaee.oidc;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * A simple test client to show how to obtain a JWK for a given Key ID using Auth0.
 */
@Slf4j
public class OpenIdConfigurationTestClient {

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/oidc/.well-known/openid-configuration"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.info("OpenID Configuration: {}", response.body());
    }

}
