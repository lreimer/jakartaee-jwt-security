# Microprofile JWT Authz Service on Jakarta EE 8

A simple authentication service to create Microprofile JWT-compatible JSON Web Tokens that are
signed using JSON Web Keys.

## Usage

To run everything locally, you need to first compile and build the showcase image.
```
$ ./gradlew assemble
$ docker-compose up --build
```

## Retrieve JWTs

```
$ curl --basic --user admin:admin http://localhost:8080/api/jwt
$ http --auth admin:admin http://localhost:8080/api/jwt

$ curl --basic --user user:user http://localhost:8080/api/jwt
$ http --auth user:user http://localhost:8080/api/jwt

# decode and verify the access_token
$ open https://jwt.io
```

## References

- https://github.com/eclipse/microprofile-jwt-auth 
- https://www.eclipse.org/community/eclipse_newsletter/2017/september/article2.php
- https://www.heise.de/developer/artikel/MicroProfile-unter-der-Lupe-Teil-3-JWT-Security-3973746.html