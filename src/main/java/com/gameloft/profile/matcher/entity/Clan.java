package com.gameloft.profile.matcher.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "clan")
@Data
public class Clan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clan_seq_gen")
    @SequenceGenerator(name = "clan_seq_gen", sequenceName = "clan_seq", allocationSize = 1)
    @Column(name = "clan_id")
    private Long clanID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerProfile playerProfile;
}
