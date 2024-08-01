INSERT INTO player_profile (
    player_id, credential, created, modified, last_session,
    total_spent, total_refund, total_transactions, last_purchase,
    active_campaigns, level, xp, total_playtime, country, language,
    birthdate, gender, inventory, custom_field
) VALUES (
    '97983be2-98b7-11e7-90cf-082e5f28d836', 'apple_credential',
    '2021-01-10T13:37:17Z', '2021-01-23T13:37:17Z', '2021-01-23T13:37:17Z',
    400, 0, 5, '2021-01-22T13:37:17Z', '[]', 3, 1000, 144,
    'CA', 'fr', '2000-01-10T13:37:17Z', 'male',
    '{"cash": 123, "coins": 123, "item_1": 1, "item_34": 3, "item_55": 2}',
    'mycustom'
);

INSERT INTO device (
    device_id, model, carrier, firmware
) VALUES (
    '1', 'apple iphone 11', 'vodafone', '123'
);

INSERT INTO player_profile_device (
    player_id, device_id
) VALUES (
    '97983be2-98b7-11e7-90cf-082e5f28d836', '1'
);

INSERT INTO clan (
    clan_id, name, description, player_id
) VALUES (
    '123456', 'Hello world clan', '', '97983be2-98b7-11e7-90cf-082e5f28d836'
);
