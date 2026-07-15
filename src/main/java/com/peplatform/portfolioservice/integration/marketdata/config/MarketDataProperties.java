package com.peplatform.portfolioservice.integration.marketdata.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "integration.market-data")
public class MarketDataProperties {

    @NotBlank
    private String primaryProvider = "FMP";

    @Valid
    @NotNull
    private Fmp fmp = new Fmp();

    @Valid
    @NotNull
    private Yahoo yahoo = new Yahoo();

    @Getter
    @Setter
    public static class Fmp {

        @NotBlank
        private String baseUrl =
                "https://financialmodelingprep.com";

        private String apiKey;

        @NotNull
        private Duration connectTimeout =
                Duration.ofSeconds(3);

        @NotNull
        private Duration readTimeout =
                Duration.ofSeconds(5);
    }

    @Getter
    @Setter
    public static class Yahoo {

        @NotBlank
        private String baseUrl =
                "https://query1.finance.yahoo.com";

        @NotNull
        private Duration connectTimeout =
                Duration.ofSeconds(3);

        @NotNull
        private Duration readTimeout =
                Duration.ofSeconds(5);
    }
}