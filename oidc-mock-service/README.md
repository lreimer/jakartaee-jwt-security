# OIDC Mock Service on Jakarta EE 8

A mock service to provide relevant OpenID connect endpoints and functionality. Code is a modified
version of the OIDC example from the Java EE samples.

## Usage

To run everything locally, you need to first compile and build the showcase image.
```
$ ./gradlew assemble
$ docker-compose up --build
```

## References

- https://github.com/javaee-samples/vendoree-samples/tree/master/payara/openid

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>
