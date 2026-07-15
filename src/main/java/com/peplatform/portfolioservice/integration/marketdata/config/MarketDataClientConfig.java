package com.peplatform.portfolioservice.integration.marketdata.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(MarketDataProperties.class)
public class MarketDataClientConfig {

    @Bean
    @Qualifier("fmpMarketDataRestClient")
    public RestClient fmpMarketDataRestClient(
            MarketDataProperties properties
    ) {
        MarketDataProperties.Fmp fmp =
                properties.getFmp();

        return buildRestClient(
                fmp.getBaseUrl(),
                fmp.getConnectTimeout(),
                fmp.getReadTimeout()
        );
    }

    @Bean
    @Qualifier("yahooMarketDataRestClient")
    public RestClient yahooMarketDataRestClient(
            MarketDataProperties properties
    ) {
        MarketDataProperties.Yahoo yahoo =
                properties.getYahoo();

        return buildRestClient(
                yahoo.getBaseUrl(),
                yahoo.getConnectTimeout(),
                yahoo.getReadTimeout()
        );
    }

    private RestClient buildRestClient(
            String baseUrl,
            Duration connectTimeout,
            Duration readTimeout
    ) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(httpClient);

        requestFactory.setReadTimeout(readTimeout);

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .defaultHeader(
                        HttpHeaders.USER_AGENT,
                        "portfolio-service/1.0"
                )
                .build();
    }
}