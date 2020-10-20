package cloud.nativ.jakartaee.auth0;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("auth0Configuration")
@ApplicationScoped
public class Auth0Configuration {

    @Inject
    @ConfigProperty(name = "client.id")
    private String clientId;

    @Inject
    @ConfigProperty(name = "client.secret")
    private String clientSecret;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
