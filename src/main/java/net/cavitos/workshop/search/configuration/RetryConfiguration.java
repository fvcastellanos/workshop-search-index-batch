package net.cavitos.workshop.search.configuration;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

@Configuration
public class RetryConfiguration {

    @Value("${retry.max-attemps:3}")
    private int maxAttempts;

    @Value("${retry.fixed-backoff:500}")
    private long fixedBackoff;

    @Bean
    public RetryTemplate retryTemplate() {

        return new RetryTemplateBuilder()
                .retryOn(ImmutableList.of(Exception.class)) // this need to be more granular
                .maxAttempts(maxAttempts)
                .fixedBackoff(fixedBackoff)
                .build();
    }
}
