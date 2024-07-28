package com.gameloft.profile.matcher.entity;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class Campaign {
    private String game;
    private String name;
    private double priority;
    private Matchers matchers;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private boolean enabled;
    private OffsetDateTime lastUpdated;


    @Data
    public static class Matchers {
        private Range level;
        private Has has;
        private DoesNotHave doesNotHave;
    }

    @Data
    public static class Range {
        private int min;
        private int max;

    }

    @Data
    public static class Has {
        private List<String> country;
        private List<String> items;

    }

    @Data
    public static class DoesNotHave {
        private List<String> items;
    }
}