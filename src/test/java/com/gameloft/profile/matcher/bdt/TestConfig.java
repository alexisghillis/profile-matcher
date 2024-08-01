package com.gameloft.profile.matcher.bdt;

import com.gameloft.profile.matcher.repository.PlayerProfileRepository;
import com.gameloft.profile.matcher.service.CampaignService;
import com.gameloft.profile.matcher.service.ProfileMatcherService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.gameloft.profile.matcher")
public class TestConfig {
    @Bean("mockedCampaignService")
    public CampaignService mockedCampaignService() {
        return Mockito.mock(CampaignService.class);
    }

    @Autowired
    public PlayerProfileRepository playerProfileRepository;

    @Bean("mockedProfileMatcherService")
    public ProfileMatcherService profileMatcherService(){
        return new ProfileMatcherService(mockedCampaignService(), playerProfileRepository);
    }
}
