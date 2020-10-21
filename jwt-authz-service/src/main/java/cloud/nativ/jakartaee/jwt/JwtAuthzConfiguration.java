package cloud.nativ.jakartaee.jwt;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class JwtAuthzConfiguration {

    @Inject
    @ConfigProperty(name = "jwt.audience")
    private List<String> audience;

    @Inject
    @ConfigProperty(name = "jwt.issuer")
    private String issuer;

    @Inject
    @ConfigProperty(name = "jwt.expiration.window", defaultValue = "300000") // 5 minutes
    private long expirationWindow;

    @Inject
    @ConfigProperty(name = "jwk.url")
    private String jwkUrl;

    public List<String> getAudience() {
        return audience;
    }

    public String getIssuer() {
        return issuer;
    }

    public long getExpirationWindow() {
        return expirationWindow;
    }

    public String getJwkUrl() {
        return jwkUrl;
    }
}
