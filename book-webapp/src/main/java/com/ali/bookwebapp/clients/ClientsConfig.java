package com.ali.bookwebapp.clients;

import com.ali.bookwebapp.ApplicationProperties;
import com.ali.bookwebapp.clients.catalog.CatalogServiceClient;
import com.ali.bookwebapp.clients.orders.OrderServiceClient;
import java.time.Duration;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientsConfig {

    private final ApplicationProperties applicationProperties;

    public ClientsConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        var requestFactory = ClientHttpRequestFactoryBuilder.simple()
                .withCustomizer(c -> {
                    c.setConnectTimeout(Duration.ofSeconds(5));
                    c.setReadTimeout(Duration.ofSeconds(5));
                })
                .build();
        return restClientBuilder ->
                restClientBuilder.baseUrl(applicationProperties.apiGatewayUrl()).requestFactory(requestFactory);
    }

    @Bean
    CatalogServiceClient catalogServiceClient(RestClient.Builder builder) {
        RestClient restClient =
                builder.baseUrl(applicationProperties.apiGatewayUrl()).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(CatalogServiceClient.class);
    }

    @Bean
    OrderServiceClient orderServiceClient(RestClient.Builder builder) {
        RestClient restClient =
                builder.baseUrl(applicationProperties.apiGatewayUrl()).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(OrderServiceClient.class);
    }
}
