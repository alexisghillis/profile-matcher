package com.gameloft.profile.matcher.service;

import com.gameloft.profile.matcher.entity.Campaign;
import com.gameloft.profile.matcher.entity.PlayerProfile;
import com.gameloft.profile.matcher.repository.PlayerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ProfileMatcherServiceIntegrationTest {

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Autowired
    @Qualifier("mockedCampaignService")
    private CampaignService campaignService;
    private ProfileMatcherService profileMatcherService;

    private UUID playerId;
    private PlayerProfile playerProfile;
    private Campaign activeCampaign;
    private Campaign inactiveCampaign;

    @BeforeEach
    public void setUp() {

        activeCampaign = createActiveCampaign();
        inactiveCampaign = createInactiveCampaign();

        // Mock campaign service behavior
        when(campaignService.getRunningCampaigns()).thenReturn(List.of(activeCampaign, inactiveCampaign));
    }

    @Test
    public void updatePlayerProfile_createsActiveCampaignEntry_whenCampaignMatches() {
        profileMatcherService = new ProfileMatcherService(campaignService, playerProfileRepository);

        playerProfile = createPlayerProfile();
        playerId = playerProfileRepository.save(playerProfile).getPlayerId();

        playerProfile = profileMatcherService.updatePlayerProfile(playerId);

        assertCampaignIsActive("Active Campaign");
        assertCampaignIsNotActive("Inactive Campaign");
    }

    @Test
    public void updatePlayerProfile_removesInactiveCampaign() {
        profileMatcherService = new ProfileMatcherService(campaignService, playerProfileRepository);

        playerProfile = createPlayerProfile();
        playerProfile.getActiveCampaigns().add("Inactive Campaign");
        playerId = playerProfileRepository.save(playerProfile).getPlayerId();

        profileMatcherService.updatePlayerProfile(playerId);

        assertCampaignIsNotActive("Inactive Campaign");
    }

    @Test
    public void updatePlayerProfile_doesNotUpdateWhenCampaignDoesNotMatch() {
        profileMatcherService = new ProfileMatcherService(campaignService, playerProfileRepository);

        playerProfile = createPlayerProfile();
        playerId = playerProfileRepository.save(playerProfile).getPlayerId();

        // Change the campaign to not match
        activeCampaign.getMatchers().getHas().setItems(List.of("nonexistent_item"));
        when(campaignService.getRunningCampaigns()).thenReturn(List.of(activeCampaign, inactiveCampaign));

        PlayerProfile updatedProfile = profileMatcherService.updatePlayerProfile(playerId);

        assertCampaignIsNotActive("Active Campaign");
    }

    private PlayerProfile createPlayerProfile() {
        PlayerProfile profile = new PlayerProfile();
        profile.setLevel(10);
        profile.setCountry("US");
        profile.setInventory(new HashMap<>(Map.of("item1", 1)));
        profile.setActiveCampaigns(new HashSet<>());
        return profile;
    }

    private Campaign createActiveCampaign() {
        Campaign campaign = new Campaign();
        campaign.setName("Active Campaign");
        campaign.setEnabled(true);
        campaign.setStartDate(OffsetDateTime.now().minusDays(1));
        campaign.setEndDate(OffsetDateTime.now().plusDays(1));
        campaign.setMatchers(createMatchers(true));
        return campaign;
    }

    private Campaign createInactiveCampaign() {
        Campaign campaign = new Campaign();
        campaign.setName("Inactive Campaign");
        campaign.setEnabled(true);
        campaign.setStartDate(OffsetDateTime.now().minusDays(5));
        campaign.setEndDate(OffsetDateTime.now().minusDays(2));
        return campaign;
    }

    private Campaign.Matchers createMatchers(boolean isActive) {
        Campaign.Matchers matcher = new Campaign.Matchers();
        Campaign.Has has = new Campaign.Has();
        has.setCountry(List.of("US"));
        has.setItems(List.of("item1"));
        matcher.setHas(has);

        Campaign.Range range = new Campaign.Range();
        range.setMax(10);
        range.setMin(1);
        matcher.setLevel(range);

        Campaign.DoesNotHave doesNotHave = new Campaign.DoesNotHave();
        doesNotHave.setItems(List.of("item2"));
        matcher.setDoesNotHave(doesNotHave);

        return matcher;
    }

    private void assertCampaignIsActive(String campaignName) {
        PlayerProfile updatedProfile = playerProfileRepository.findByPlayerId(playerId).orElseThrow();
        assertTrue(updatedProfile.getActiveCampaigns().contains(campaignName));
    }

    private void assertCampaignIsNotActive(String campaignName) {
        PlayerProfile updatedProfile = playerProfileRepository.findByPlayerId(playerId).orElseThrow();
        assertFalse(updatedProfile.getActiveCampaigns().contains(campaignName));
    }
}


