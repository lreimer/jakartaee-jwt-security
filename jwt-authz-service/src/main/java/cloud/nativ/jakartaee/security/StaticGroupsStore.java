package cloud.nativ.jakartaee.security;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class StaticGroupsStore implements IdentityStore {

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        LOGGER.info("Getting caller groups for {}", validationResult.getCallerPrincipal().getName());

        String callerPrincipalName = validationResult.getCallerPrincipal().getName();
        if ("admin".equalsIgnoreCase(callerPrincipalName)) {
            return new HashSet<>(Arrays.asList("Administrator", "Developer"));
        } else {
            return Collections.singleton("Developer");
        }
    }

    @Override
    public int priority() {
        return 101;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return Collections.singleton(ValidationType.PROVIDE_GROUPS);
    }
}