package com.gameloft.profile.matcher.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "device")
@Data
public class Device {
    @Id
    @GeneratedValue
    @Column(name = "device_id")
    private UUID deviceId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "devices")
    private Set<PlayerProfile> playerProfiles = new HashSet<>();
}