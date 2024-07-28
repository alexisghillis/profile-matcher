package com.gameloft.profile.matcher.converter;

import com.gameloft.profile.matcher.api.model.CampaignResponse;
import com.gameloft.profile.matcher.entity.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CampaignResponseConverter {

    Campaign mapCampaignResponseToCampaign(CampaignResponse response);
}