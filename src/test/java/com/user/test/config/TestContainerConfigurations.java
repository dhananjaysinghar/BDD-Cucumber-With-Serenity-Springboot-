package com.user.test.config;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestContainerConfigurations {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> {
            Network componentTestNetwork = Network.newNetwork();
            // MONGO DB
            GenericContainer mongoContainer = getMongoContainer(componentTestNetwork);
            startContainer(mongoContainer, "27017:27017");
            org.testcontainers.Testcontainers.exposeHostPorts(mongoContainer.getFirstMappedPort());

            // SERVICE
            GenericContainer serviceContainer = getServiceContainer(componentTestNetwork);
            setEnvPropertiesForServiceContainer(mongoContainer, serviceContainer);
            startContainer(serviceContainer, "8080:8080");

            // COMPONENT_TEST_CONFIG SETUP
            setApplicationProperties(serviceContainer, mongoContainer);
        };
    }

    public static GenericContainer getServiceContainer(Network componentTestNetwork) {
        return new GenericContainer<>(new ImageFromDockerfile().withDockerfile(
                Paths.get(System.getProperty("user.dir") + "//Dockerfile")))
                .withCreateContainerCmdModifier(cmd -> cmd.withName("user-service-test-containers"))
                .withNetwork(componentTestNetwork)
                .withExposedPorts(8080)
                .withStartupTimeout(Duration.ofMinutes(10));
    }

    public static GenericContainer getMongoContainer(Network componentTestNetwork) {
        return new GenericContainer<>(DockerImageName.parse("mongo:latest"))
                .withExposedPorts(27017)
                .withNetworkAliases("mongoDB")
                .withNetwork(componentTestNetwork)
                .withCreateContainerCmdModifier(cmd -> cmd.withName("mongoDbContainer"))
                .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", "password")
                .withEnv("MONGO_INITDB_DATABASE", "test")
                .withCommand("--auth")
                .withStartupTimeout(Duration.ofMinutes(10));

    }

    private static void setEnvPropertiesForServiceContainer(GenericContainer mongoDBContainer, GenericContainer service) {
        service
                .withEnv("MONGO_DB_DATABASE_NAME", "test")
                //host.testcontainers.internal OR host.docker.internal
                .withEnv("MONGO_DB_URI", "mongodb://admin:password@host.docker.internal:"
                        + mongoDBContainer.getFirstMappedPort() + "/?authSource=admin");
    }


    private static void setApplicationProperties(GenericContainer service, GenericContainer mongoDBContainer) {
        String uri = "mongodb://" + mongoDBContainer.getHost() + ":" + mongoDBContainer.getMappedPort(27017);
        System.setProperty("SERVICE_BASE_PATH", "http://" + service.getHost() + ":" + service.getMappedPort(8080));
        System.setProperty("MONGO_DB_URI", uri);
        System.setProperty("MONGO_DB_DATABASE_NAME", "test");
    }

    private static void startContainer(GenericContainer container, String port) {
        List<String> portBindings = new ArrayList<>();
        portBindings.add(port);
        container.setPortBindings(portBindings);
        container.start();
        container.waitingFor(Wait.forHealthcheck());
    }
}
