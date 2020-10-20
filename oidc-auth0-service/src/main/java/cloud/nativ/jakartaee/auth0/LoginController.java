package cloud.nativ.jakartaee.auth0;

import fish.payara.security.openid.api.OpenIdContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.JsonObject;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;
import java.util.Set;

@Named
@ApplicationScoped
public class LoginController {

    @Inject
    private SecurityContext securityContext;

    @Inject
    private OpenIdContext openIdContext;

    public String getCallerPrincipal() {
        Principal callerPrincipal = securityContext.getCallerPrincipal();
        return callerPrincipal != null ? callerPrincipal.getName() : "";
    }

    public String getClaimsJson() {
        JsonObject claimsJson = openIdContext.getClaimsJson();
        return claimsJson != null ? claimsJson.toString() : "";
    }

    public String getCallerGroups() {
        Set<String> callerGroups = openIdContext.getCallerGroups();
        return callerGroups != null ? callerGroups.toString() : "";
    }

}
