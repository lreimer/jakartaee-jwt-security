package cloud.nativ.jakartaee.jwks;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import javax.enterprise.context.ApplicationScoped;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

/**
 * A factory implementation to create {@link RSAKey} instances.
 */
@ApplicationScoped
public class RSAKeyFactory {

    /**
     * Create a fresh {@link RSAKey} instance from the given parameters.
     *
     * @param keysize the key size to use
     * @param use     the {@link KeyUse}
     * @param alg     the {@link Algorithm}
     * @param kid     the key ID
     * @return a fresh {@link RSAKey} instance
     */
    public RSAKey create(int keysize, KeyUse use, Algorithm alg, String kid) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keysize);

            KeyPair kp = generator.generateKeyPair();
            RSAPublicKey pub = (RSAPublicKey) kp.getPublic();
            RSAPrivateKey priv = (RSAPrivateKey) kp.getPrivate();

            RSAKey.Builder builder = new RSAKey.Builder(pub)
                    .privateKey(priv)
                    .keyUse(use)
                    .algorithm(alg);

            if (Objects.isNull(kid)) {
                builder = builder.keyIDFromThumbprint();
            } else {
                builder = builder.keyID(kid);
            }

            return builder.build();
        } catch (NoSuchAlgorithmException | JOSEException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
