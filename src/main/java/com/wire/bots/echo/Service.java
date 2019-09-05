package com.wire.bots.echo;

import com.wire.bots.echo.model.Config;
import com.wire.bots.echo.resource.Webhook;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

public class Service extends Application<Config> {
    public static void main(String[] args) throws Exception {
        new Service().run("server", "echo.yaml");
    }

    @Override
    public void run(Config config, Environment environment) {
        Client client = new JerseyClientBuilder(environment)
                .build(getName());

        // Use Webhook to receive events from the Wire Backend
        environment.jersey().register(new Webhook(client, config.proxyUrl, config.authentication));
    }
}
