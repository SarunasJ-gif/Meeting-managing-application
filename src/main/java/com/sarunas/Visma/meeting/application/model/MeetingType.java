package com.sarunas.Visma.meeting.application.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MeetingType {

    LIVE("Live"),
    INPERSON("InPerson");

    private String value;

    MeetingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MeetingType fromString(String value) {
        return MeetingType.valueOf(value.toUpperCase());
    }
}
