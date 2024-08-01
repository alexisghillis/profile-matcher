package com.gameloft.profile.matcher.entity;


import com.gameloft.profile.matcher.converter.AttributeConverter.MapToStringConverter;
import com.gameloft.profile.matcher.converter.AttributeConverter.SetToStringConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "player_profile")
public class PlayerProfile {
    @Id
    @GeneratedValue
    @Column(name = "player_id", nullable = false)
    private UUID playerId;
    private String credential;
    private OffsetDateTime created;

    private OffsetDateTime modified;
    @Column(name = "last_session")
    private OffsetDateTime lastSession;
    @Column(name = "total_spent")
    private int totalSpent;
    @Column(name = "total_refund")
    private int totalRefund;
    @Column(name = "total_transactions")
    private int totalTransactions;
    @Column(name = "last_purchase")
    private OffsetDateTime lastPurchase;
    @Convert(converter = SetToStringConverter.class)
    @Column(columnDefinition = "TEXT")
    private Set<String> activeCampaigns;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "player_profile_device",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Device> devices = new HashSet<>();
    private int level;
    private int xp;
    @Column(name = "total_playtime")
    private int totalPlaytime;
    private String country;
    private String language;
    private OffsetDateTime birthdate;
    private String gender;
    @Convert(converter = MapToStringConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Integer> inventory;

    /*
    Bi-directional Relationships: In JPA entities with bi-directional relationships, excluding related entities from
    equals() and hashCode() can prevent issues such as infinite loops or performance degradation due to large object graphs being traversed.
    If you remove the Excludes you get this error: Method threw 'java.lang.StackOverflowError' exception. Cannot evaluate com.gameloft.profile.matcher.entity.PlayerProfile.toString()
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "playerProfile", orphanRemoval = true)
    @JoinColumn(name = "player_id")
    private Clan clan;
    @Column(name = "custom_field")
    private String customField;
}

