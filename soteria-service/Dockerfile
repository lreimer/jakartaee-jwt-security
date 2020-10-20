FROM qaware/distroless-zulu-payara-micro:11.0.8-5.2020.5

CMD ["--nocluster", "--disablephonehome", "--logproperties", "/opt/payara/logging.properties", "--deploymentDir", "/opt/payara/deployments"]

COPY src/main/docker/ /opt/payara/
COPY build/libs/soteria-service.war /opt/payara/deployments/
