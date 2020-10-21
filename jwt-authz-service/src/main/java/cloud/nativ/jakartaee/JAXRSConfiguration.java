package cloud.nativ.jakartaee;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures a JAX-RS endpoint.
 */
@ApplicationPath("api")
@BasicAuthenticationMechanismDefinition(realmName = "jwt-authz-service")
@DeclareRoles({"Administrator", "Developer"})
public class JAXRSConfiguration extends Application {
}
