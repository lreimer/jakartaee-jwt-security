package cloud.nativ.jakartaee.oidc;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * A simple test client to call the OpenId Configuration endpoint.
 */
@Slf4j
public class OpenIdConfigurationTestClient {

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://dev-yjwtg9k9.eu.auth0.com/.well-known/openid-configuration"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.info("OpenID Configuration: {}", response.body());
    }

}
