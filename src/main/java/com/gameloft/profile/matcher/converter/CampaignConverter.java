package com.gameloft.profile.matcher.converter;

import com.gameloft.profile.matcher.api.model.CampaignResponse;
import com.gameloft.profile.matcher.api.model.CampaignResponseMatchers;
import com.gameloft.profile.matcher.entity.Campaign;
import org.springframework.stereotype.Component;

@Component
public class CampaignConverter {


    public Campaign convert(CampaignResponse campaignResponse) {
        if (campaignResponse == null) {
            return null;
        }

        Campaign campaign = new Campaign();
        campaign.setGame(campaignResponse.getGame());
        campaign.setName(campaignResponse.getName());
        campaign.setPriority(campaignResponse.getPriority() != null ? campaignResponse.getPriority() : 0);
        campaign.setStartDate(campaignResponse.getStartDate());
        campaign.setEndDate(campaignResponse.getEndDate());
        campaign.setEnabled(campaignResponse.getEnabled() != null ? campaignResponse.getEnabled() : false);
        campaign.setLastUpdated(campaignResponse.getLastUpdated());

        if (campaignResponse.getMatchers() != null) {
            campaign.setMatchers(convertMatchers(campaignResponse.getMatchers()));
        }

        return campaign;
    }

    private Campaign.Matchers convertMatchers(CampaignResponseMatchers responseMatchers) {
        if (responseMatchers == null) {
            return null;
        }

        Campaign.Matchers matchers = new Campaign.Matchers();

        if (responseMatchers.getLevel() != null) {
            Campaign.Range range = new Campaign.Range();
            range.setMin(responseMatchers.getLevel().getMin());
            range.setMax(responseMatchers.getLevel().getMax());
            matchers.setLevel(range);
        }

        if (responseMatchers.getHas() != null) {
            Campaign.Has has = new Campaign.Has();
            has.setCountry(responseMatchers.getHas().getCountry());
            has.setItems(responseMatchers.getHas().getItems());
            matchers.setHas(has);
        }

        if (responseMatchers.getDoesNotHave() != null) {
            Campaign.DoesNotHave doesNotHave = new Campaign.DoesNotHave();
            doesNotHave.setItems(responseMatchers.getDoesNotHave().getItems());
            matchers.setDoesNotHave(doesNotHave);
        }

        return matchers;
    }
}