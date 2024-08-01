package com.gameloft.profile.matcher.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameloft.profile.matcher.api.CampaignsApi;
import com.gameloft.profile.matcher.api.model.CampaignResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@AllArgsConstructor
public class MockCampaign implements CampaignsApi {

    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<List<CampaignResponse>> getCampaigns() {
        try {
            // Load the JSON file from the classpath
            InputStream inputStream = new ClassPathResource("campaigns.json").getInputStream();

            // Convert JSON array to List<CampaignResponse>
            List<CampaignResponse> campaigns = objectMapper.readValue(inputStream, new TypeReference<>() {});

            // Return the list of campaigns wrapped in a ResponseEntity
            return ResponseEntity.ok(campaigns);
        } catch (IOException e) {
            // Handle error, possibly returning a different response or logging the error
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
