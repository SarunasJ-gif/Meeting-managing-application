package com.sarunas.Visma.meeting.application.exception;

public class PersonExistsInMeetingException extends RuntimeException {
    public PersonExistsInMeetingException(String message) {
        super(message);
    }
}
