package com.wire.bots.echo;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wire.bots.echo.filters.WireAuthenticationFeature;
import com.wire.bots.echo.model.Config;
import com.wire.bots.echo.resource.Webhook;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class Service extends Application<Config> {
    public static String token;

    public static void main(String[] args) throws Exception {
        new Service().run(args);
    }

    @Override
    public void run(Config config, Environment environment) {
        Service.token = config.serviceToken;

        final Client httpClient = createHttpClient();

        // Use Webhook to receive events from the Wire Backend
        environment.jersey().register(new Webhook(httpClient, config));

        environment.jersey().register(WireAuthenticationFeature.class);

    }

    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
    }

    private Client createHttpClient() {
        ClientConfig config = new ClientConfig()
                .register(JacksonJsonProvider.class);

        return ClientBuilder
                .newBuilder()
                .withConfig(config)
                .build();
    }
}
