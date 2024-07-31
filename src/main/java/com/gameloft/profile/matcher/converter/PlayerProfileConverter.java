package com.gameloft.profile.matcher.converter;

import com.gameloft.profile.matcher.api.model.ClanResponse;
import com.gameloft.profile.matcher.api.model.DeviceResponse;
import com.gameloft.profile.matcher.api.model.PlayerProfileResponse;
import com.gameloft.profile.matcher.entity.Clan;
import com.gameloft.profile.matcher.entity.Device;
import com.gameloft.profile.matcher.entity.PlayerProfile;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerProfileConverter {

    public static PlayerProfileResponse toResponse(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            return null;
        }

        PlayerProfileResponse response = new PlayerProfileResponse();
        response.setPlayerId(playerProfile.getPlayerId());
        response.setCredential(playerProfile.getCredential());
        response.setCreated(playerProfile.getCreated());
        response.setModified(playerProfile.getModified());
        response.setLastSession(playerProfile.getLastSession());
        response.setTotalSpent(playerProfile.getTotalSpent());
        response.setTotalRefund(playerProfile.getTotalRefund());
        response.setTotalTransactions(playerProfile.getTotalTransactions());
        response.setLastPurchase(playerProfile.getLastPurchase());
        response.setActiveCampaigns(playerProfile.getActiveCampaigns());


        // Convert devices from Set<Device> to List<Device>
        if (playerProfile.getDevices() != null) {
            response.setDevices(playerProfile.getDevices().stream().map(DeviceConverter::toResponse).collect(Collectors.toSet()));
        }

        response.setLevel(playerProfile.getLevel());
        response.setXp(playerProfile.getXp());
        response.setTotalPlaytime(playerProfile.getTotalPlaytime());
        response.setCountry(playerProfile.getCountry());
        response.setLanguage(playerProfile.getLanguage());
        response.setBirthdate(playerProfile.getBirthdate());
        response.setGender(playerProfile.getGender());

        // Convert inventory from Map<String, Integer> to Map<String, Integer>
        Map<String, Integer> inventoryMap = playerProfile.getInventory();
        if (inventoryMap != null) {
            response.setInventory(new HashMap<>(inventoryMap));
        }

        // Set Clan
        if (playerProfile.getClan() != null) {
            response.setClan(ClanConverter.toResponse(playerProfile.getClan()));
        }

        response.setCustomfield(playerProfile.getCustomField());

        return response;
    }

    private static class ClanConverter {
        public static ClanResponse toResponse(Clan clan) {
            if (clan == null) {
                return null;
            }

            ClanResponse response = new ClanResponse();
            response.setId(clan.getClanID());
            response.setName(clan.getName());

            return response;
        }
    }

    private static class DeviceConverter {
        public static DeviceResponse toResponse(Device device) {
            if (device == null) {
                return null;
            }

            DeviceResponse response = new DeviceResponse();
            response.setId(device.getDeviceId());

            return response;
        }
    }

}
