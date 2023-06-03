package com.sarunas.Visma.meeting.application.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MeetingCategory {

    CODEMONKEY("CodeMonkey"),
    HUB("Hub"),
    SHORT("Short"),
    TEAMBUILDING("TeamBuilding");

    private String value;

    MeetingCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MeetingCategory fromString(String value) {
        return MeetingCategory.valueOf(value.toUpperCase());
    }
}
