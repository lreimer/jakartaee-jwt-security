package cloud.nativ.jakartaee.auth0;

import fish.payara.security.annotations.ClaimsDefinition;
import fish.payara.security.annotations.OpenIdAuthenticationDefinition;

@OpenIdAuthenticationDefinition(
        clientId = "#{auth0Configuration.clientId}",
        clientSecret = "#{auth0Configuration.clientSecret}",
        providerURI = "https://dev-yjwtg9k9.eu.auth0.com",
        redirectURI = "http://localhost:8080/callback",
        claimsDefinition = @ClaimsDefinition(
                callerGroupsClaim = "https://jakartaee.nativ.cloud/groups",
                callerNameClaim = "nickname"
        )
)
public class Auth0Authentication {
}
