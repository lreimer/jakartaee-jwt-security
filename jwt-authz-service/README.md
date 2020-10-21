# Soteria Security Service on Jakarta EE 8

A simple service to showcase some of the Soteria Jakarta EE 8 security APIs.

## Usage

To run everything locally, you need to first compile and build the showcase image.
```
$ ./gradlew assemble
$ docker-compose up --build
```

## Add BASIC authentication to JAX-RS application

First, we add the following annotations to the JAX-RS application class to enable BASIC authentication
as well as declare the used roles.

```java
@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "soteria-service")
@DeclareRoles({"admin", "developer"})
```

## Secure the REST resource using Java EE Security annotations

Now we add the following REST resources to the codebase. We use annotations to specify
the access rights and required roles for the different GET methods. Also we make use of
the the `SecurityContext` to get information about the auth.

```java
@ApplicationScoped
@Path("security")
public class SecurityResource {

    @Context
    private SecurityContext securityContext;

    @GET
    @PermitAll
    public Response info() {
        if (securityContext.getUserPrincipal() == null) {
            return Response.noContent().build();
        }

        JsonObject jsonObject = getSecurityJsonObject();
        return Response.ok(jsonObject).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"admin", "developer"})
    public Response allRoles() {
        JsonObject jsonObject = getSecurityJsonObject();
        return Response.ok(jsonObject).build();
    }

    @GET
    @Path("admin")
    @RolesAllowed({"admin"})
    public Response adminOnly() {
        JsonObject jsonObject = getSecurityJsonObject();
        return Response.ok(jsonObject).build();
    }

    private JsonObject getSecurityJsonObject() {
        return Json.createObjectBuilder()
                .add("authenticationScheme", securityContext.getAuthenticationScheme())
                .add("secure", securityContext.isSecure())
                .add("userPrincipal", securityContext.getUserPrincipal().getName())
                .add("admin", securityContext.isUserInRole("admin"))
                .build();
    }
}
``` 

## Implement custom identity and group store

We want to implement our own simple custom `IdentityStore` implementations to authenticate the
user and obtain the groups for the users. Add the following class to implement each store.

```java
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
```

```java
@Slf4j
@ApplicationScoped
public class StaticGroupsStore implements IdentityStore {

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        LOGGER.info("Getting caller groups for {}", validationResult.getCallerPrincipal().getName());

        String callerPrincipalName = validationResult.getCallerPrincipal().getName();
        if ("admin".equalsIgnoreCase(callerPrincipalName)) {
            return new HashSet<>(Arrays.asList("admin", "developer"));
        } else {
            return Collections.singleton("developer");
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
```

## Perform authenticated and unauthenticated REST calls

Once you compiled and deployed the service, use a REST client to invoke the REST endpoints with
and without credentials.

```
$ curl http://localhost:8080/api/security
$ curl --basic --user user:user http://localhost:8080/api/security

$ curl --basic --user user:user http://localhost:8080/api/security/all
$ curl --basic --user user:wrongpwd http://localhost:8080/api/security/all
$ curl --basic --user user:user http://localhost:8080/api/security/admin

$ curl --basic --user admin:admin http://localhost:8080/api/security/admin
``` 

## References

- https://www.baeldung.com/java-ee-8-security
- https://jaxenter.de/api-security-soteria-73132