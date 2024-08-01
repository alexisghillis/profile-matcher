package com.gameloft.profile.matcher.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "device")
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq_gen")
    @SequenceGenerator(name = "device_seq_gen", sequenceName = "device_seq", allocationSize = 1)
    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "model")
    private String model;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "firmware")
    private Integer firmware;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "devices")
    private Set<PlayerProfile> playerProfiles = new HashSet<>();
}