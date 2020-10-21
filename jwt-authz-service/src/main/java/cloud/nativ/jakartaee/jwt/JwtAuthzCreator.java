package cloud.nativ.jakartaee.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.ParseException;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class JwtAuthzCreator {

    @Inject
    private JwtAuthzConfiguration configuration;

    @Context
    private SecurityContext securityContext;

    private Clock clock;
    private Client client;

    @PostConstruct
    void init() {
        clock = Clock.systemUTC();
        client = ClientBuilder.newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public String create(String upn) {
        // construct minimum claim set according to MP-JWT specification
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(configuration.getIssuer())                 // the token issuer
                .subject(getSubject())    // the subject of this token
                .jwtID(UUID.randomUUID().toString())           // the unique JWT ID
                .audience(configuration.getAudience())    // the intended audience
                .expirationTime(new Date(clock.millis() + configuration.getExpirationWindow()))
                .claim("upn", upn)
                .claim("groups", getGroups())
                .build();

        return sign(claimsSet, getRsaKey());
    }

    private String sign(JWTClaimsSet claimSet, RSAKey rsaKey) {
        try {
            JWSSigner signer = new RSASSASigner(rsaKey);
            SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(rsaKey.getKeyID())
                    .type(JOSEObjectType.JWT)
                    .build(), claimSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new ForbiddenException("Unexpected error during JWT signing.", e);
        }
    }

    private RSAKey getRsaKey() {
        Invocation.Builder request = client.target(configuration.getJwkUrl()).request(MediaType.APPLICATION_JSON);

        try (Response response = request.get()){
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new ForbiddenException("Error retrieving JWK.");
            }
            return RSAKey.parse(response.readEntity(String.class));
        } catch (ParseException e) {
            throw new ForbiddenException("Error parsing RSA key from JWK.", e);
        }
    }

    private String getSubject() {
        if (securityContext.getUserPrincipal() != null) {
            return securityContext.getUserPrincipal().getName();
        } else {
            return "unknown";
        }
    }

    private String[] getGroups() {
        Set<String> groups = new HashSet<>();
        if (securityContext.isUserInRole("Administrator")) {
            groups.add("Administrator");
        }
        if (securityContext.isUserInRole("Developer")) {
            groups.add("Developer");
        } else {
            groups.add("Unknown");
        }
        return groups.toArray(new String[0]);
    }
}
