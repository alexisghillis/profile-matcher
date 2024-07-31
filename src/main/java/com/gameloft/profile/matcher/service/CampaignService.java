package com.gameloft.profile.matcher.service;

import com.gameloft.profile.matcher.controller.MockCampaign;
import com.gameloft.profile.matcher.converter.CampaignConverter;
import com.gameloft.profile.matcher.entity.Campaign;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CampaignService {
    private final MockCampaign campaignsApi;
    private final CampaignConverter campaignConverter;

    public List<Campaign> getRunningCampaigns() {

        return Objects.requireNonNull(campaignsApi.getCampaigns().getBody())
                .stream().map(campaignConverter::convert)
                .collect(Collectors.toList());
    }
}