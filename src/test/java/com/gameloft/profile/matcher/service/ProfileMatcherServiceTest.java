package com.gameloft.profile.matcher.service;

import com.gameloft.profile.matcher.entity.Campaign;
import com.gameloft.profile.matcher.entity.PlayerProfile;
import com.gameloft.profile.matcher.exceptions.ResourceNotFoundException;
import com.gameloft.profile.matcher.repository.PlayerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileMatcherServiceTest {

    @Mock
    private CampaignService campaignService;

    @Mock
    private PlayerProfileRepository playerProfileRepository;


    private ProfileMatcherService profileMatcherService;

    private UUID playerId;
    private PlayerProfile playerProfile;
    private Campaign activeCampaign;
    private Campaign inactiveCampaign;

    @BeforeEach
    public void setUp() {
        playerId = UUID.randomUUID();
        playerProfile = createPlayerProfile(playerId, 10, "US", Map.of("item1", 1));
        activeCampaign = createCampaign("Active Campaign", true, OffsetDateTime.now().minusDays(1), OffsetDateTime.now().plusDays(1),
                createMatchers(createHas(List.of("US"), List.of("item1")), createRange(1, 10), createDoesNotHave(List.of("item2"))));
        inactiveCampaign = createCampaign("Inactive Campaign", true, OffsetDateTime.now().minusDays(5), OffsetDateTime.now().minusDays(2),
                createMatchers(createHas(Collections.emptyList(), Collections.emptyList()), createRange(0, 0), createDoesNotHave(Collections.emptyList())));
    }

    @Test
    public void updatePlayerProfile_throwsResourceNotFoundException_whenPlayerNotFound() {
        when(playerProfileRepository.findByPlayerId(playerId)).thenReturn(Optional.empty());
        when(campaignService.getRunningCampaigns()).thenReturn(List.of(activeCampaign));
        profileMatcherService = new ProfileMatcherService(campaignService, playerProfileRepository);

        assertThrows(ResourceNotFoundException.class, () -> profileMatcherService.updatePlayerProfile(playerId));

        verify(playerProfileRepository, times(1)).findByPlayerId(playerId);
        verifyNoMoreInteractions(playerProfileRepository);
    }

    @Test
    public void updatePlayerProfile_updatesProfile_whenCampaignMatches() {
        when(playerProfileRepository.findByPlayerId(playerId)).thenReturn(Optional.of(playerProfile));
        when(campaignService.getRunningCampaigns()).thenReturn(List.of(activeCampaign));
        profileMatcherService = new ProfileMatcherService(campaignService, playerProfileRepository);

        PlayerProfile updatedProfile = profileMatcherService.updatePlayerProfile(playerId);

        assertTrue(updatedProfile.getActiveCampaigns().contains("Active Campaign"));
        verify(playerProfileRepository, times(1)).save(playerProfile);
    }

    @Test
    public void updatePlayerProfile_doesNotUpdateProfile_whenCampaignDoesNotMatch() {
        when(playerProfileRepository.findByPlayerId(playerId)).thenReturn(Optional.of(playerProfile));
        activeCampaign.getMatchers().getHas().setItems(List.of("nonexistent_item"));
        when(campaignService.getRunningCampaigns()).thenReturn(List.of(activeCampaign));
        profileMatcherService = new ProfileMatcherService(campaignService, playerProfileRepository);

        PlayerProfile updatedProfile = profileMatcherService.updatePlayerProfile(playerId);

        assertFalse(updatedProfile.getActiveCampaigns().contains("Active Campaign"));
        verify(playerProfileRepository, never()).save(playerProfile);
    }

    @Test
    public void updatePlayerProfile_removesInactiveCampaign() {
        playerProfile.getActiveCampaigns().add("Inactive Campaign");
        when(playerProfileRepository.findByPlayerId(playerId)).thenReturn(Optional.of(playerProfile));
        when(campaignService.getRunningCampaigns()).thenReturn(List.of(inactiveCampaign));
        profileMatcherService = new ProfileMatcherService(campaignService, playerProfileRepository);

        PlayerProfile updatedProfile = profileMatcherService.updatePlayerProfile(playerId);

        assertFalse(updatedProfile.getActiveCampaigns().contains("Inactive Campaign"));
        verify(playerProfileRepository, times(1)).save(playerProfile);
    }

    @Test
    public void updatePlayerProfile_handlesMultipleCampaigns() {
        Campaign secondCampaign = createCampaign("Second Campaign", true, OffsetDateTime.now().minusDays(1), OffsetDateTime.now().plusDays(1),
                createMatchers(createHas(List.of("US"), List.of("item2")), createRange(1, 10), createDoesNotHave(List.of("item3"))));

        when(playerProfileRepository.findByPlayerId(playerId)).thenReturn(Optional.of(playerProfile));
        when(campaignService.getRunningCampaigns()).thenReturn(List.of(activeCampaign, secondCampaign));
        profileMatcherService = new ProfileMatcherService(campaignService, playerProfileRepository);

        PlayerProfile updatedProfile = profileMatcherService.updatePlayerProfile(playerId);

        assertTrue(updatedProfile.getActiveCampaigns().contains("Active Campaign"));
        assertFalse(updatedProfile.getActiveCampaigns().contains("Second Campaign"));
        verify(playerProfileRepository, times(1)).save(playerProfile);
    }

    // Helper methods
    private PlayerProfile createPlayerProfile(UUID playerId, int level, String country, Map<String, Integer> inventory) {
        PlayerProfile profile = new PlayerProfile();
        profile.setPlayerId(playerId);
        profile.setLevel(level);
        profile.setCountry(country);
        profile.setInventory(new HashMap<>(inventory));
        profile.setActiveCampaigns(new HashSet<>());
        return profile;
    }

    private Campaign createCampaign(String name, boolean enabled, OffsetDateTime startDate, OffsetDateTime endDate, Campaign.Matchers matchers) {
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setEnabled(enabled);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setMatchers(matchers);
        return campaign;
    }

    private Campaign.Matchers createMatchers(Campaign.Has has, Campaign.Range range, Campaign.DoesNotHave doesNotHave) {
        Campaign.Matchers matchers = new Campaign.Matchers();
        matchers.setHas(has);
        matchers.setLevel(range);
        matchers.setDoesNotHave(doesNotHave);
        return matchers;
    }

    private Campaign.Has createHas(List<String> countries, List<String> items) {
        Campaign.Has has = new Campaign.Has();
        has.setCountry(countries);
        has.setItems(items);
        return has;
    }

    private Campaign.Range createRange(int min, int max) {
        Campaign.Range range = new Campaign.Range();
        range.setMin(min);
        range.setMax(max);
        return range;
    }

    private Campaign.DoesNotHave createDoesNotHave(List<String> items) {
        Campaign.DoesNotHave doesNotHave = new Campaign.DoesNotHave();
        doesNotHave.setItems(items);
        return doesNotHave;
    }
}
