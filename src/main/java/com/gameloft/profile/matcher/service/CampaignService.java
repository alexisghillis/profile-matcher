package com.gameloft.profile.matcher.service;

import com.gameloft.profile.matcher.converter.CampaignResponseConverter;
import com.gameloft.profile.matcher.entity.Campaign;
import com.gameloft.profile.matcher.rest.CampaignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CampaignService {
    private final CampaignClient campaignClient;
    private final CampaignResponseConverter campaignResponseConverter;

    public List<Campaign> getRunningCampaigns() {

        return Objects.requireNonNull(campaignClient.campaignsGet().getBody())
                .stream().map(campaignResponseConverter::mapCampaignResponseToCampaign)
                .collect(Collectors.toList());
    }
}