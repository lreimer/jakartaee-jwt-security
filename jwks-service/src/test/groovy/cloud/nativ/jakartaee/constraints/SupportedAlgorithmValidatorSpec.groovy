package cloud.nativ.jakartaee.constraints

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

@Title('Test spec for the SupportedAlgorithm constraint validator')
class SupportedAlgorithmValidatorSpec extends Specification {

    @Subject
    SupportedAlgorithmValidator validator

    void setup() {
        validator = new SupportedAlgorithmValidator()
    }

    @Unroll
    def "Validate #value is #valid"() {
        expect:
        validator.isValid(value, null) == valid

        where:
        value          | valid
        "RS256"        | true
        "RS384"        | true
        "RS512"        | true
        "RSA1_5"       | true
        "RSA-OAEP"     | true
        "RSA-OAEP-256" | true
        "NONE"         | false
    }
}
