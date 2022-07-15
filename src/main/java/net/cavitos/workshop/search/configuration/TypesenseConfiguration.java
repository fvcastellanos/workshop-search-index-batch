package net.cavitos.workshop.search.configuration;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.typesense.api.Client;
import org.typesense.resources.Node;

import java.time.Duration;

@Configuration
public class TypesenseConfiguration {

    @Value("${typesense.protocol}")
    private String protocol;

    @Value("${typesense.host}")
    private String host;

    @Value("${typesense.port}")
    private String port;

    @Value("${typesense.apiKey}")
    private String apiKey;

    @Bean
    public Client typesenseClient() {

        final var nodes = ImmutableList.of(
                new Node(protocol, host, port)
        );

        final var configuration = new org.typesense.api.Configuration(nodes, Duration.ofSeconds(2), apiKey);

        return new Client(configuration);
    }
}
