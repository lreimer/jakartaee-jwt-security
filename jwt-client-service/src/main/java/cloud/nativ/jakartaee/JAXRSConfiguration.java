package cloud.nativ.jakartaee;

import org.eclipse.microprofile.auth.LoginConfig;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures a JAX-RS endpoint.
 */
@ApplicationScoped
@ApplicationPath("api")
@LoginConfig(authMethod = "MP-JWT")
@DeclareRoles({"Administrator", "Developer"})
public class JAXRSConfiguration extends Application {
}
