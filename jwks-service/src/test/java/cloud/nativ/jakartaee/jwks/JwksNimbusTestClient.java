package cloud.nativ.jakartaee.jwks;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

/**
 * A simple test client to show how to obtain a JWK for a given Key ID using Auth0.
 */
@Slf4j
public class JwksNimbusTestClient {

    public static void main(String[] args) throws Exception {
        JWKSet publicKeys = JWKSet.load(new URL("http://localhost:8080/.well-known/jwks.json"));
        JWK jwk = publicKeys.getKeyByKeyId("testKeyID");
        LOGGER.info("Found JWK {}", jwk);
    }

}
