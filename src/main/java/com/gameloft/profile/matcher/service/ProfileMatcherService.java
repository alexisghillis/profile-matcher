package com.gameloft.profile.matcher.service;

import com.gameloft.profile.matcher.entity.Campaign;
import com.gameloft.profile.matcher.entity.PlayerProfile;
import com.gameloft.profile.matcher.exceptions.ResourceNotFoundException;
import com.gameloft.profile.matcher.repository.PlayerProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProfileMatcherService {

    private final CampaignService campaignService;
    private final PlayerProfileRepository playerProfileRepository;


    public PlayerProfile updatePlayerProfile(UUID playerId) {
        List<Campaign> campaigns = campaignService.getRunningCampaigns();

        Optional<PlayerProfile> optionalPlayerProfile = playerProfileRepository.findByPlayerId(playerId);

        PlayerProfile playerProfile = optionalPlayerProfile
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        for (Campaign campaign : campaigns) {
            if (campaign.isEnabled() && isCampaignActive(campaign)) {
                if(matchesCampaign(playerProfile, campaign)){
                    updateProfile(playerProfile, campaign);
                    playerProfileRepository.save(playerProfile);
                }
            }

            if(!isCampaignActive(campaign) && playerProfile.getActiveCampaigns().contains(campaign.getName())){
                removeCampaignFromProfile(playerProfile, campaign);
                playerProfileRepository.save(playerProfile);
            }
        }
        return playerProfile;
    }

    private boolean isCampaignActive(Campaign campaign) {
        OffsetDateTime now = OffsetDateTime.now();
        return (campaign.getStartDate().isBefore(now) || campaign.getStartDate().isEqual(now)) &&
                (campaign.getEndDate().isAfter(now) || campaign.getEndDate().isEqual(now));
    }

    private boolean matchesCampaign(PlayerProfile profile, Campaign campaign) {
        return isLevelInRange(profile.getLevel(), campaign.getMatchers().getLevel()) &&
                hasRequiredAttributes(profile, campaign.getMatchers().getHas()) &&
                doesNotHaveExcludedAttributes(profile, campaign.getMatchers().getDoesNotHave());
    }

    private boolean isLevelInRange(int level, Campaign.Range range) {
        return level >= range.getMin() && level <= range.getMax();
    }

    private boolean hasRequiredAttributes(PlayerProfile profile, Campaign.Has has) {
        return has.getCountry().contains(profile.getCountry()) &&
                has.getItems().stream().allMatch(item -> profile.getInventory().containsKey(item));
    }

    private boolean doesNotHaveExcludedAttributes(PlayerProfile profile, Campaign.DoesNotHave doesNotHave) {
        return doesNotHave.getItems().stream().noneMatch(item -> profile.getInventory().containsKey(item));
    }

    private void updateProfile(PlayerProfile profile, Campaign campaign) {
        profile.getActiveCampaigns().add(campaign.getName());
        profile.setModified(OffsetDateTime.now());
    }

    private void removeCampaignFromProfile(PlayerProfile profile, Campaign campaign) {
        profile.getActiveCampaigns().remove(campaign.getName());
        profile.setModified(OffsetDateTime.now());
    }

}
