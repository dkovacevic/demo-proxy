package com.wire.bots.echo;

import com.wire.bots.echo.filters.WireAuthenticationFeature;
import com.wire.bots.echo.model.Config;
import com.wire.bots.echo.resource.Webhook;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Service extends Application<Config> {
    public static String token;

    public static void main(String[] args) throws Exception {
        new Service().run(args);
    }

    @Override
    public void run(Config config, Environment environment) {
        Service.token = config.authentication;

        // Use Webhook to receive events from the Wire Backend
        environment.jersey().register(new Webhook());

        environment.jersey().register(WireAuthenticationFeature.class);

    }
}
