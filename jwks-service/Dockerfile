FROM qaware/distroless-zulu-payara-micro:11.0.8-5.2020.5

CMD ["--hzconfigfile", "/opt/payara/hazelcast.xml", "--logproperties", "/opt/payara/logging.properties", "--deploymentDir", "/opt/payara/deployments"]

COPY src/main/docker/ /opt/payara/
COPY build/libs/jwks-service.war /opt/payara/deployments/
