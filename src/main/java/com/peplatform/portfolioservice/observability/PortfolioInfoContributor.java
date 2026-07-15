package com.peplatform.portfolioservice.observability;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PortfolioInfoContributor implements InfoContributor {

    private final Environment environment;
    private final Instant startedAt = Instant.now();

    public PortfolioInfoContributor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> service = new LinkedHashMap<>();
        service.put("name", environment.getProperty("spring.application.name", "portfolio-service"));
        service.put("description", "Private equity portfolio and investment management service");
        service.put("javaVersion", System.getProperty("java.version"));
        service.put("startedAt", startedAt.toString());
        service.put("activeProfiles", environment.getActiveProfiles());
        builder.withDetail("service", service);
    }
}
