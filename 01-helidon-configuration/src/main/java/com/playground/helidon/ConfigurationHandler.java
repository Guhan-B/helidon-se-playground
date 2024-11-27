package com.playground.helidon;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.PollingStrategies;

import java.io.File;
import java.time.Duration;


public class ConfigurationHandler {
    private static final String CONFIGURATION_FILENAME = "application.yaml";
    private static final String USER_HOME_PATH = System.getProperty("user.home") + File.separator;

    public static void initialize(boolean useMetaConfig) {
        if (!useMetaConfig) {
            // Creating a configuration Builder instance to customize configuration
            Config.Builder builder = Config.builder();

            // Adding custom configuration sources. The following sources are define below
            // 1. Environment Variables
            // 2. System Variables
            // 3. application.yaml from user-home (With support for polling)
            // 4. application.yaml from classpath
            builder.addSource(ConfigSources.environmentVariables());
            builder.addSource(ConfigSources.systemProperties());
            builder.addSource(
                    ConfigSources
                            .file(USER_HOME_PATH + CONFIGURATION_FILENAME)
                            .pollingStrategy(PollingStrategies.regular(Duration.ofSeconds(1)))
            );
            builder.addSource(ConfigSources.classpath(CONFIGURATION_FILENAME));

            // Building & setting the configuration as global
            Config.global(builder.build());
        }
        else {
            Config.global(Config.create());
        }

    }
}
