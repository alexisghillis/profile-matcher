package com.gameloft.profile.matcher.repository;

import com.gameloft.profile.matcher.entity.PlayerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, UUID> {
}
