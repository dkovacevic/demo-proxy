package com.wire.bots.echo;

import com.wire.bots.echo.model.Config;
import com.wire.bots.echo.resource.Webhook;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;

import javax.ws.rs.client.Client;

public class Service extends Application<Config> {
    public static void main(String[] args) throws Exception {
        new Service().run(args);
    }

    @Override
    public void run(Config config, Environment environment) {
        JerseyClientConfiguration jerseyConfig = new JerseyClientConfiguration();
        jerseyConfig.setTimeout(Duration.seconds(30));
        jerseyConfig.setConnectionTimeout(Duration.seconds(4));
        jerseyConfig.setConnectionRequestTimeout(Duration.seconds(4));
        jerseyConfig.setKeepAlive(Duration.seconds(0));
        jerseyConfig.setRetries(3);

        Client client = new JerseyClientBuilder(environment)
                .using(jerseyConfig)
                .build(getName());

        // Use Webhook to receive events from the Wire Backend
        environment.jersey().register(new Webhook(client, config.proxyUrl, config.authentication));
    }
}
