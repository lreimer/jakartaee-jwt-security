package cloud.nativ.jakartaee.jwks;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * The JWK repository bean acts as a DAO component for basic CRUD operations on JWKs.
 * All created JWK instances are cached.
 */
@Slf4j
@ApplicationScoped
public class JwkRepository {

    @Inject
    private CacheManager cacheManager;
    private Cache<String, JWK> jwkCache;

    @Inject
    private RSAKeyFactory keyFactory;

    /**
     * Initialize the JCache instance for the public JWK.
     */
    @PostConstruct
    void initialize() {
        jwkCache = cacheManager.getCache("jwkCache", String.class, JWK.class);
    }

    /**
     * Create a JWK using the given parameters and cache the key in case a
     * key ID has been specified.
     *
     * @param keysize the keysize
     * @param use     the {@link KeyUse}
     * @param alg     the {@link Algorithm}
     * @param kid     the Key ID
     * @return a fresh JWK instance
     */
    public JWK create(int keysize, KeyUse use, Algorithm alg, String kid) {
        RSAKey key = keyFactory.create(keysize, use, alg, kid);

        String keyID = key.getKeyID();
        if (keyID != null) {
            LOGGER.debug("Cache public JWK for key ID {}", keyID);
            jwkCache.put(keyID, key.toPublicJWK());
        }

        return key;
    }

    /**
     * Remove a JWK by its key ID.
     *
     * @param kid the key ID
     * @return true of removed, false if key ID did not exist
     */
    public boolean delete(String kid) {
        boolean remove = jwkCache.remove(kid);
        LOGGER.debug("Removed JWK with key ID {} -> {}", kid, remove);
        return remove;
    }

    /**
     * Retrieve and return the collection of public JWKs as JSON object type maps.
     *
     * @return a collection of all known public JWKs
     */
    public Collection<Map<String, Object>> publicJWKs() {
        LOGGER.info("Get all known public JWKs.");
        Collection<Map<String, Object>> jwks = new ArrayList<>();
        jwkCache.forEach((entry) -> jwks.add(entry.getValue().toJSONObject()));
        return jwks;
    }
}
