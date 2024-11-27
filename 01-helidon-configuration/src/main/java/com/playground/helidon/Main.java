package com.playground.helidon;

import io.helidon.config.Config;

import java.util.List;
import java.util.function.Supplier;


public class Main {

    private Main() { }

    public static void main(String[] args) throws InterruptedException {
        ConfigurationHandler.initialize(false);

        Config config = Config.global();

        System.out.println("========================================");
        accessConfigurationByKey(config);
        System.out.println("========================================");
        traverseConfiguration(config);
        System.out.println("========================================");
        configurationNodeType(config);
        System.out.println("========================================");
        configurationValueDataType(config);
        System.out.println("========================================");
        mutabilitySupport(config);
        System.out.println("========================================");
    }

    public static void accessConfigurationByKey(Config config) {
        assert config.get("") == config;
        assert config.root()  == config;

        assert config.get("application.deployment.platform") == config.get("application.deployment").get("platform");
        assert config.get("application.deployment.platform") == config.get("application").get("deployment").get("platform");

        List<Config> providers = config.get("application.data.providers").asNodeList().orElse(List.of());

        for(Config provider : providers) {
            System.out.printf("[KEY] %s [VALUE] { name: %-10s, priority: %d } %n", provider.key().toString(), provider.get("name").asString().get(), provider.get("priority").asInt().get());
        }
    }

    public static void configurationValueDataType(Config config) {
        System.out.println("[INT]     asInt()      " + config.get("application.value.int").asInt().get());
        System.out.println("[LONG]    asLong()     " + config.get("application.value.long").asLong().get());
        System.out.println("[DOUBLE]  asDouble()   " + config.get("application.value.double").asDouble().get());
        System.out.println("[STRING]  asString()   " + config.get("application.value.string").asString().get());
        System.out.println("[BOOLEAN] asBoolean()  " + config.get("application.value.boolean").asBoolean().get());
    }

    public static void traverseConfiguration(Config config) {
        config.get("application").traverse().forEach(node -> {
            if (node.isLeaf()) {
                System.out.printf("[KEY] %-40s [VALUE] %s%n", node.key().toString(), node.asString().get());
            }
        });
    }

    public static void configurationNodeType(Config config) {
        config.get("application").traverse().forEach(node -> {
            System.out.printf("[TYPE] %-10s [KEY] %s%n", node.type().toString(), node.key().toString());
        });

        // Missing Configuration Key Scenario
        Config node = config.get("key.does.not.exist");
        System.out.printf("[TYPE] %-10s [KEY] %s%n", node.type().toString(), node.key().toString());
        assert !node.exists();
    }

    public static void mutabilitySupport(Config config) throws InterruptedException {
        // Using onChange Listener
        config.get("application.name").onChange(node -> {
            System.out.println("[VALUE] (onChange) " + node.asString().orElse("Default"));
        });

        // Using Supplier<T>
        Supplier<String> supplier = config.get("application.name").asString().supplier("Default");

        for (int i = 1; i < 10; i++) {
            System.out.println("[VALUE] (Supplier<T>) " + supplier.get());
            Thread.sleep(6000);
        }
    }
}