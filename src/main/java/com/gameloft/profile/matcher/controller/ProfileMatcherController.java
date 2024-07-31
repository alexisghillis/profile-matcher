package com.gameloft.profile.matcher.controller;

import com.gameloft.profile.matcher.api.GetClientConfigApi;
import com.gameloft.profile.matcher.api.model.PlayerProfileResponse;
import com.gameloft.profile.matcher.converter.PlayerProfileConverter;
import com.gameloft.profile.matcher.service.ProfileMatcherService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
public class ProfileMatcherController implements GetClientConfigApi {

    private final ProfileMatcherService profileMatcherService;

    @Override
    @Transactional
    public ResponseEntity<PlayerProfileResponse> getClientConfigPlayerIdGet(UUID playerId) {

        return ResponseEntity.status(OK).body(PlayerProfileConverter.toResponse(profileMatcherService.updatePlayerProfile(playerId)));
    }

}
