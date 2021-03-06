# JSON Web Key Set (JWKS) Service on Jakarta EE 8

A simple service to generate and managed JSON Web Key Sets. It allows to generate
JSON Web Keys, and obtain the list of known public keys.

## Usage

To run everything locally, you need to first compile and build the showcase image.
```
$ ./gradlew assemble
$ docker-compose up --build
```

## References

- https://auth0.com/docs/jwks
- https://github.com/mitreid-connect/mkjwk.org
- https://github.com/mitreid-connect/json-web-key-generator
- https://github.com/auth0/jwks-rsa-java
