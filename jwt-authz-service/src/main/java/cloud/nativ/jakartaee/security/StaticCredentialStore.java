package cloud.nativ.jakartaee.security;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class StaticCredentialStore implements IdentityStore {
    @Override
    public CredentialValidationResult validate(Credential credential) {
        try {
            String username = ((UsernamePasswordCredential) credential).getCaller();
            String password = ((UsernamePasswordCredential) credential).getPasswordAsString();

            LOGGER.info("Validate credential {}:{}", username, password);

            if (Objects.equals(username, password)) {
                return new CredentialValidationResult(username);
            } else {
                return CredentialValidationResult.INVALID_RESULT;
            }
        } catch (SecurityException e) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return Collections.singleton(ValidationType.VALIDATE);
    }
}
