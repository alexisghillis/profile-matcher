package com.gameloft.profile.matcher.rest;

import com.gameloft.profile.matcher.api.CampaignsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "campaignClient", url = "${campaign.api.url}")
public interface CampaignClient extends CampaignsApi {
}