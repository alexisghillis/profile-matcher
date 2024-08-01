package com.gameloft.profile.matcher.bdt.StepDefinitions;

import com.gameloft.profile.matcher.bdt.BdtTestContext;
import com.gameloft.profile.matcher.bdt.CucumberSpringConfiguration;
import com.gameloft.profile.matcher.entity.Campaign;
import com.gameloft.profile.matcher.entity.PlayerProfile;
import com.gameloft.profile.matcher.repository.PlayerProfileRepository;
import com.gameloft.profile.matcher.service.CampaignService;
import com.gameloft.profile.matcher.service.ProfileMatcherService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {CucumberSpringConfiguration.class})
public class ClientMatcherSteps {

    @Autowired
    private BdtTestContext bdtTestContext;
    @Autowired
    @Qualifier("mockedCampaignService")
    private CampaignService mockedCampaignService;

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Autowired
    @Qualifier("mockedProfileMatcherService")
    private ProfileMatcherService profileMatcherService;

    private PlayerProfile playerProfile;
    private List<Campaign> runningCampaigns;

    @Given("a player exists")
    public void aPlayerExists() {
        playerProfile = new PlayerProfile();
        playerProfile.setLevel(10);
        playerProfile.setCountry("US");
        playerProfile.setInventory(Collections.singletonMap("item1", 1));
        playerProfile.setActiveCampaigns(new HashSet<>());
        bdtTestContext.setPlayerId(playerProfileRepository.save(playerProfile).getPlayerId());
    }

    @Given("the player has the country {string} and level {int}")
    public void thePlayerHasCountryAndLevel(String country, int level) {
        playerProfile.setCountry(country);
        playerProfile.setLevel(level);
        playerProfileRepository.save(playerProfile);
    }

    @Given("there is an active campaign named {string}")
    public void thereIsAnActiveCampaignNamed(String campaignName) {
        Campaign campaign = new Campaign();
        campaign.setName(campaignName);
        campaign.setEnabled(true);
        campaign.setStartDate(OffsetDateTime.now().minusDays(1));
        campaign.setEndDate(OffsetDateTime.now().plusDays(1));

        Campaign.Matchers matchers = new Campaign.Matchers();
        Campaign.Has has = new Campaign.Has();
        has.setCountry(List.of("US"));
        has.setItems(List.of("item1"));
        matchers.setHas(has);
        Campaign.Range range = new Campaign.Range();
        range.setMax(10);
        range.setMin(1);
        matchers.setLevel(range);
        Campaign.DoesNotHave doesNotHave = new Campaign.DoesNotHave();
        doesNotHave.setItems(List.of("item2"));
        matchers.setDoesNotHave(doesNotHave);

        campaign.setMatchers(matchers);

        Mockito.when(mockedCampaignService.getRunningCampaigns()).thenReturn(List.of(campaign));
    }

    @When("the client requests the profile configuration")
    public void theClientRequestsTheProfileConfiguration() {
        UUID playerId = bdtTestContext.getPlayerId();
        runningCampaigns = profileMatcherService.updatePlayerProfile(playerId).getActiveCampaigns().stream()
                .map(campaignName -> {
                    Campaign campaign = new Campaign();
                    campaign.setName(campaignName);
                    return campaign;
                }).collect(Collectors.toList());
    }

    @Then("the response should include the active campaign {string}")
    public void theResponseShouldIncludeTheActiveCampaign(String campaignName) {
        boolean campaignIncluded = runningCampaigns.stream()
                .anyMatch(campaign -> campaign.getName().equals(campaignName));
        assertTrue(campaignIncluded);
    }

    @Then("the response should not include any active campaigns")
    public void theResponseShouldNotIncludeAnyActiveCampaigns() {
        assertTrue(runningCampaigns.isEmpty());
    }

    @Given("there are no active campaigns")
    public void thereAreNoActiveCampaigns() {
        Mockito.when(mockedCampaignService.getRunningCampaigns()).thenReturn(Collections.emptyList());
    }

    @Given("there is an inactive campaign named {string}")
    public void thereIsAnInactiveCampaignNamed(String campaignName) {
        Campaign campaign = new Campaign();
        campaign.setName(campaignName);
        campaign.setEnabled(true);
        campaign.setStartDate(OffsetDateTime.now().minusDays(5));
        campaign.setEndDate(OffsetDateTime.now().minusDays(2));

        Campaign.Matchers matchers = new Campaign.Matchers();
        Campaign.Has has = new Campaign.Has();
        has.setCountry(List.of("US"));
        has.setItems(List.of("item1"));
        matchers.setHas(has);
        Campaign.Range range = new Campaign.Range();
        range.setMax(10);
        range.setMin(1);
        matchers.setLevel(range);
        Campaign.DoesNotHave doesNotHave = new Campaign.DoesNotHave();
        doesNotHave.setItems(List.of("item2"));
        matchers.setDoesNotHave(doesNotHave);

        campaign.setMatchers(matchers);

        Mockito.when(mockedCampaignService.getRunningCampaigns()).thenReturn(List.of(campaign));
    }

    @Given("there are multiple campaigns matching the player's profile")
    public void thereAreMultipleCampaignsMatchingThePlayersProfile() {
        Campaign campaign1 = createCampaign("Campaign1", true, OffsetDateTime.now().minusDays(1), OffsetDateTime.now().plusDays(1), "US", List.of("item1"), List.of("item2"), 1, 10);
        Campaign campaign2 = createCampaign("Campaign2", true, OffsetDateTime.now().minusDays(1), OffsetDateTime.now().plusDays(1), "US", List.of("item1"), List.of("item2"), 1, 10);

        Mockito.when(mockedCampaignService.getRunningCampaigns()).thenReturn(List.of(campaign1, campaign2));
    }

    @Given("the player has an item {string} which should exclude them from certain campaigns")
    public void thePlayerHasAnItemWhichShouldExcludeThemFromCertainCampaigns(String excludedItem) {
        Map<String, Integer> inventory = new HashMap<>(playerProfile.getInventory());
        inventory.put(excludedItem, 1);
        playerProfile.setInventory(inventory);
        playerProfileRepository.save(playerProfile);
    }

    @Then("the response should include the active campaigns {string} and {string}")
    public void theResponseShouldIncludeTheActiveCampaigns(String campaign1, String campaign2) {
        UUID playerId = bdtTestContext.getPlayerId();
        PlayerProfile updatedProfile = profileMatcherService.updatePlayerProfile(playerId);

        // Check that both campaigns are included in the active campaigns list
        assertTrue(updatedProfile.getActiveCampaigns().contains(campaign1), "Expected active campaign " + campaign1 + " not found.");
        assertTrue(updatedProfile.getActiveCampaigns().contains(campaign2), "Expected active campaign " + campaign2 + " not found.");
    }

    @Then("the response should not include the active campaign {string}")
    public void theResponseShouldNotIncludeTheActiveCampaign(String campaignName) {
        UUID playerId = bdtTestContext.getPlayerId();
        PlayerProfile updatedProfile = profileMatcherService.updatePlayerProfile(playerId);

        // Check that the campaign is not included in the active campaigns list
        assertFalse(updatedProfile.getActiveCampaigns().contains(campaignName), "Unexpected active campaign " + campaignName + " found.");
    }


    private Campaign createCampaign(String name, boolean enabled, OffsetDateTime startDate, OffsetDateTime endDate, String country, List<String> items, List<String> doesNotHaveItems, int minLevel, int maxLevel) {
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setEnabled(enabled);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);

        Campaign.Matchers matchers = new Campaign.Matchers();
        Campaign.Has has = new Campaign.Has();
        has.setCountry(List.of(country));
        has.setItems(items);
        matchers.setHas(has);
        Campaign.Range range = new Campaign.Range();
        range.setMax(maxLevel);
        range.setMin(minLevel);
        matchers.setLevel(range);
        Campaign.DoesNotHave doesNotHave = new Campaign.DoesNotHave();
        doesNotHave.setItems(doesNotHaveItems);
        matchers.setDoesNotHave(doesNotHave);

        campaign.setMatchers(matchers);
        return campaign;
    }

}

