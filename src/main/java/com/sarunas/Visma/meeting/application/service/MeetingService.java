package com.sarunas.Visma.meeting.application.service;

import com.sarunas.Visma.meeting.application.model.Meeting;
import com.sarunas.Visma.meeting.application.model.Person;
import com.sarunas.Visma.meeting.application.model.PersonResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface MeetingService {

    Meeting addMeeting(Meeting meeting) throws IOException;
    Meeting deleteMeeting(Long meetingId, Long personId) throws IOException;
    PersonResponse addPersonToMeeting(Long meetingId, Person person) throws IOException;
    Person removePersonFromMeeting(Long meetingId, Long personId) throws IOException;
    Set<Meeting> getFilteredMeetings(String description, String responsiblePerson,
                                     String category, String type, LocalDate startDate,
                                     LocalDate endDate, Integer numberOfAttendees) throws IOException;
    List<Meeting> getAllMeetings() throws IOException;
}
