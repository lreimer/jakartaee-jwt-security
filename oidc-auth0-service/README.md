# OIDC Auth0 Service on Jakarta EE 8 and Payara

A simple Jakarta EE 8 service that is OIDC secured using Auth0 as provider. 

## Usage

To run everything locally, you need to first compile and build the showcase image.
```
$ ./gradlew assemble
$ docker-compose up --build
```

For Auth0 to work, you need to set the CLIENT_ID and CLIENT_SECRET environment 
variables in a `.env` file.

Also, for the user groups to work with Auth0 and Jakarta EE 8 you need to create a rule
with the following content:
```javascript
function(user, context, callback) {
  const namespace = 'https://jakartaee.nativ.cloud/';
  context.idToken[namespace + 'groups'] = user.user_metadata.groups;
  callback(null, user, context);
}
```

For the user you also have to set the following `user_metadata` attribute:
```json
{
  "groups": [
    "Administrator",
    "Developer"
  ]
}
```

## References

- https://auth0.com/blog/developing-robust-web-apps-with-javaserver-faces-and-java-ee/

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>
