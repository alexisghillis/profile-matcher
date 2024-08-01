Feature: Get Client Config

  Scenario: Player profile matches an active campaign
    Given a player exists
    And the player has the country "US" and level 10
    And there is an active campaign named "Active Campaign"
    When the client requests the profile configuration
    Then the response should include the active campaign "Active Campaign"

  Scenario: Player profile does not match any active campaign
    Given a player exists
    And the player has the country "FR" and level 5
    And there are no active campaigns
    When the client requests the profile configuration
    Then the response should not include any active campaigns

  Scenario: Player does not match any inactive campaigns
    Given a player exists
    And the player has the country "US" and level 10
    And there is an inactive campaign named "Inactive Campaign"
    When the client requests the profile configuration
    Then the response should not include any active campaigns

  Scenario: Player matches multiple active campaigns
    Given a player exists
    And the player has the country "US" and level 10
    And there are multiple campaigns matching the player's profile
    When the client requests the profile configuration
    Then the response should include the active campaigns "Campaign1" and "Campaign2"

  Scenario: Player's profile contains excluded attributes
    Given a player exists
    And the player has the country "US" and level 10
    And the player has an item "item2" which should exclude them from certain campaigns
    And there is an active campaign named "Exclusive Campaign"
    When the client requests the profile configuration
    Then the response should not include the active campaign "Exclusive Campaign"
