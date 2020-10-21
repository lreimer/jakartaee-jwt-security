package cloud.nativ.jakartaee.jwt;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("jwt")
public class JwtAuthzResource {

    @Inject
    private JwtAuthzCreator jwtAuthzCreator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response authenticate(@QueryParam("upn") @DefaultValue("mario-leander.reimer@qaware.de") String upn) {
        return buildAccessTokenResponse(jwtAuthzCreator.create(upn));
    }

    private Response buildAccessTokenResponse(String accessToken) {
        // construct OAuth 2.0 compatible response structure
        JsonObject payload = Json.createObjectBuilder()
                .add("access_token", accessToken)
                .add("token_type", "Bearer")
                .build();

        return Response.ok(payload)
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .build();
    }
}
