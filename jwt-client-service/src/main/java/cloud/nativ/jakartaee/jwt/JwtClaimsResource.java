package cloud.nativ.jakartaee.jwt;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("claims")
public class JwtClaimsResource {

    @Inject
    private JsonWebToken jsonWebToken;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Developer")
    public Response getClaims() {
        return Response.ok(tokeInfo()).build();
    }

    private JsonObject tokeInfo() {
        return Json.createObjectBuilder()
                .add("name", jsonWebToken.getName())
                .add("issuer", jsonWebToken.getIssuer())
                .add("audience", Json.createArrayBuilder(jsonWebToken.getAudience()))
                .add("groups", Json.createArrayBuilder(jsonWebToken.getGroups()))
                .add("subject", jsonWebToken.getSubject())
                .add("raw", jsonWebToken.getRawToken())
                .build();
    }
}
