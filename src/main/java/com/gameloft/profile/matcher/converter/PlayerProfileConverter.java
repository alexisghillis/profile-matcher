package com.gameloft.profile.matcher.converter;

import com.gameloft.profile.matcher.api.model.PlayerProfileResponse;
import com.gameloft.profile.matcher.entity.PlayerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerProfileConverter {


    PlayerProfileResponse mapPlayerProfileToPlayerProfileResponse(PlayerProfile playerProfile);
}
