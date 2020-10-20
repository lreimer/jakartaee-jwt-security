FROM qaware/distroless-zulu-payara-micro:11.0.8-5.2020.5

CMD ["--disablephonehome", "--logproperties", "/opt/payara/logging.properties", "--deploymentDir", "/opt/payara/deployments"]

COPY src/main/docker/ /opt/payara/
COPY build/libs/oidc-auth0-service.war /opt/payara/deployments/
