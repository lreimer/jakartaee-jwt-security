package cloud.nativ.jakartaee.jwks;

import cloud.nativ.jakartaee.constraints.SupportedAlgorithm;
import cloud.nativ.jakartaee.constraints.SupportedKeyUse;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;

/**
 * The REST endpoint to GET and DELETE JSON Web Keys (JWK).
 */
@ApplicationScoped
@Path("jwk")
public class JwkResource {

    @Inject
    private JwkRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@QueryParam("size") @DefaultValue("2048") @Min(2048) int keysize,
                           @QueryParam("use") @DefaultValue("sig") @SupportedKeyUse String use,
                           @QueryParam("alg") @DefaultValue("RS256") @SupportedAlgorithm String alg,
                           @QueryParam("kid") String kid) {
        JWK jwk;

        try {
            jwk = repository.create(keysize, KeyUse.parse(use), new Algorithm(alg), kid);
        } catch (ParseException e) {
            throw new BadRequestException(e);
        }

        return Response.ok(jwk.toJSONObject()).build();
    }

    @DELETE
    @Path("{kid}")
    public Response delete(@PathParam("kid") String kid) {
        boolean deleted = repository.delete(kid);
        if (deleted) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
