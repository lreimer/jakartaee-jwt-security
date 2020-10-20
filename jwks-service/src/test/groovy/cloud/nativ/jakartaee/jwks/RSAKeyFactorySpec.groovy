package cloud.nativ.jakartaee.jwks

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.KeyUse
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

@Title('Test specification for the RSAKey factory')
class RSAKeyFactorySpec extends Specification {

    @Subject
    RSAKeyFactory factory

    void setup() {
        factory = new RSAKeyFactory()
    }

    @Unroll
    def "Create a new RSA Key instance #kid"() {
        when:
        def rsaKey = factory.create(keysize, use, alg, kid)

        then:
        rsaKey
        rsaKey.size() == keysize
        rsaKey.keyID == kid
        rsaKey.keyUse == use

        where:
        keysize | use               | alg                | kid
        1024    | KeyUse.SIGNATURE  | JWSAlgorithm.RS256 | "1024-Signature-RS256"
        2048    | KeyUse.SIGNATURE  | JWSAlgorithm.RS256 | "2048-Signature-RS256"
        1024    | KeyUse.SIGNATURE  | JWSAlgorithm.RS512 | "1024-Signature-RS512"
        2048    | KeyUse.SIGNATURE  | JWSAlgorithm.RS512 | "2048-Signature-RS512"
        2048    | KeyUse.ENCRYPTION | JWSAlgorithm.RS512 | "2048-Encryption-RS512"
        2048    | KeyUse.ENCRYPTION | JWSAlgorithm.HS512 | "2048-Encryption-HS512"
        4096    | KeyUse.ENCRYPTION | JWSAlgorithm.RS512 | "4096-Encryption-HS512"
    }
}
