package cloud.nativ.jakartaee.constraints

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

@Title('Test spec for the SupportedKeyUse constraint validator')
class SupportedKeyUseValidatorSpec extends Specification {

    @Subject
    SupportedKeyUseValidator validator

    void setup() {
        validator = new SupportedKeyUseValidator()
    }

    @Unroll
    def "Validate #value is #valid"() {
        expect:
        validator.isValid(value, null) == valid

        where:
        value  | valid
        "sig"  | true
        "enc"  | true
        "none" | false
        ""     | false
    }
}
