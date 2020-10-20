package cloud.nativ.jakartaee.jwks

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.KeyUse
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import javax.cache.Cache
import javax.cache.CacheManager

@Title('Test spec for the JWK repository')
class JwkRepositorySpec extends Specification {

    @Subject
    JwkRepository repository
    Cache cache

    void setup() {
        repository = new JwkRepository()
        repository.keyFactory = new RSAKeyFactory()

        cache = Mock(Cache)
        CacheManager cacheManager = Stub(CacheManager)
        cacheManager.getCache('jwkCache', String, JWK) >> cache

        repository.cacheManager = cacheManager
        repository.initialize()
    }

    def "Create and cache a new JWK"() {
        when:
        def jwk = repository.create(2048, KeyUse.SIGNATURE, JWSAlgorithm.RS512, "testKeyID")

        then:
        jwk
        1 * cache.put("testKeyID", _)
    }

    def "Delete known JWK"() {
        when:
        boolean deleted = repository.delete("testKeyID")

        then:
        1 * cache.remove("testKeyID") >> true
        deleted
    }

    def "Delete unknown JWK"() {
        when:
        boolean deleted = repository.delete("testKeyID")

        then:
        1 * cache.remove("testKeyID") >> false
        !deleted
    }
}
