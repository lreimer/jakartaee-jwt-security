package cloud.nativ.jakartaee.oidc;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.UUID;

import fish.payara.security.openid.api.OpenIdConstant;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

@Slf4j
@ApplicationScoped
@Path("oidc")
public class OidcResource {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private static final String AUTH_CODE_VALUE = "sample_auth_code";
    private static final String ACCESS_TOKEN_VALUE = "sample_access_token";
    public static final String CLIENT_ID_VALUE = "sample_client_id";
    public static final String CLIENT_SECRET_VALUE = "sample_client_secret";
    private static final String SUBJECT_VALUE = "sample_subject";

    private static String nonce;

    @GET
    @Path("/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfiguration() {
        String result;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("oidc-configuration.json")) {
            result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(joining("\n"));
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Unable to read OIDC configuration.", e);
            return Response.serverError().build();
        }

        return Response.ok(result)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    @GET
    @Path("/auth")
    public Response authEndpoint(@QueryParam(OpenIdConstant.CLIENT_ID) String clientId,
                                 @QueryParam(OpenIdConstant.SCOPE) String scope,
                                 @QueryParam(OpenIdConstant.RESPONSE_TYPE) String responseType,
                                 @QueryParam(OpenIdConstant.NONCE) String nonce,
                                 @QueryParam(OpenIdConstant.STATE) String state,
                                 @QueryParam(OpenIdConstant.REDIRECT_URI) String redirectUri) throws URISyntaxException {

        StringBuilder returnURL = new StringBuilder(redirectUri);
        returnURL.append("?&" + OpenIdConstant.STATE + "=").append(state);
        returnURL.append("&" + OpenIdConstant.CODE + "=" + AUTH_CODE_VALUE);

        this.nonce = nonce;

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        if (!OpenIdConstant.CODE.equals(responseType)) {
            jsonBuilder.add(OpenIdConstant.ERROR_PARAM, "invalid_response_type");
            return Response.serverError().entity(jsonBuilder.build()).build();
        }
        if (!CLIENT_ID_VALUE.equals(clientId)) {
            jsonBuilder.add(OpenIdConstant.ERROR_PARAM, "invalid_client_id");
            return Response.serverError().entity(jsonBuilder.build()).build();
        }
        return Response.seeOther(new URI(returnURL.toString())).build();
    }

    @POST
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response tokenEndpoint(
            @FormParam(OpenIdConstant.CLIENT_ID) String clientId,
            @FormParam(OpenIdConstant.CLIENT_SECRET) String clientSecret,
            @FormParam(OpenIdConstant.GRANT_TYPE) String grantType,
            @FormParam(OpenIdConstant.CODE) String code,
            @FormParam(OpenIdConstant.REDIRECT_URI) String redirectUri) {

        Response.ResponseBuilder builder;
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        if (!CLIENT_ID_VALUE.equals(clientId)) {
            jsonBuilder.add(OpenIdConstant.ERROR_PARAM, "invalid_client_id");
            builder = Response.serverError();
        } else if (!CLIENT_SECRET_VALUE.equals(clientSecret)) {
            jsonBuilder.add(OpenIdConstant.ERROR_PARAM, "invalid_client_secret");
            builder = Response.serverError();
        } else if (!OpenIdConstant.AUTHORIZATION_CODE.equals(grantType)) {
            jsonBuilder.add(OpenIdConstant.ERROR_PARAM, "invalid_grant_type");
            builder = Response.serverError();
        } else if (!AUTH_CODE_VALUE.equals(code)) {
            jsonBuilder.add(OpenIdConstant.ERROR_PARAM, "invalid_auth_code");
            builder = Response.serverError();
        } else {
            Date now = new Date();
            JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                    .issuer("http://localhost:8080/api/oidc")
                    .subject(SUBJECT_VALUE)
                    .audience(asList(CLIENT_ID_VALUE))
                    .expirationTime(new Date(now.getTime() + 1000 * 60 * 10))
                    .notBeforeTime(now)
                    .issueTime(now)
                    .jwtID(UUID.randomUUID().toString())
                    .claim(OpenIdConstant.NONCE, nonce)
                    .build();

            PlainJWT idToken = new PlainJWT(jwtClaims);
            jsonBuilder.add(OpenIdConstant.IDENTITY_TOKEN, idToken.serialize());
            jsonBuilder.add(OpenIdConstant.ACCESS_TOKEN, ACCESS_TOKEN_VALUE);
            jsonBuilder.add(OpenIdConstant.TOKEN_TYPE, BEARER_TYPE);

            builder = Response.ok();
        }

        return builder.entity(jsonBuilder.build()).build();
    }

    @GET
    @Path("/userinfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userinfoEndpoint(@HeaderParam(AUTHORIZATION_HEADER) String authorizationHeader) {
        String accessToken = authorizationHeader.substring(BEARER_TYPE.length() + 1);

        Response.ResponseBuilder builder;
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        if (ACCESS_TOKEN_VALUE.equals(accessToken)) {
            builder = Response.ok();

            // return claims
            jsonBuilder.add(OpenIdConstant.SUBJECT_IDENTIFIER, SUBJECT_VALUE)
                    .add("name", "Mario-Leander")
                    .add("family_name", "Reimer")
                    .add("given_name", "Mario-Leander Reimer")
                    .add("email", "mario-leander.reimer@qaware.de")
                    .add("email_verified", true)
                    .add("profile", "https://lreimer.github.io")
                    .add("gender", "male")
                    .add("locale", "de");
        } else {
            jsonBuilder.add(OpenIdConstant.ERROR_PARAM, "invalid_access_token");
            builder = Response.serverError();
        }

        return builder.entity(jsonBuilder.build().toString()).build();
    }
}
