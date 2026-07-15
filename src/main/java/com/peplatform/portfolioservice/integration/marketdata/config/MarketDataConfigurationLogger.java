package com.peplatform.portfolioservice.integration.marketdata.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketDataConfigurationLogger {

    private final MarketDataProperties properties;

    @PostConstruct
    void logConfiguration() {
        String apiKey = properties.getFmp().getApiKey();

        log.info(
                "Market-data configuration loaded: "
                        + "primaryProvider={}, fmpBaseUrl={}, "
                        + "fmpApiKeyConfigured={}, yahooBaseUrl={}",
                properties.getPrimaryProvider(),
                properties.getFmp().getBaseUrl(),
                apiKey != null && !apiKey.isBlank(),
                properties.getYahoo().getBaseUrl()
        );
    }
}