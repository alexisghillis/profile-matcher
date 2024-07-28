package com.gameloft.profile.matcher.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WireMockConfig {

    @Bean
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/campaigns"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("campaigns.json")));
        return wireMockServer;
    }
}
