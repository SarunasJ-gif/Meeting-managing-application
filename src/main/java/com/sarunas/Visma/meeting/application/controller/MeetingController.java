package com.sarunas.Visma.meeting.application.controller;

import com.sarunas.Visma.meeting.application.model.Meeting;
import com.sarunas.Visma.meeting.application.model.Person;
import com.sarunas.Visma.meeting.application.model.PersonResponse;
import com.sarunas.Visma.meeting.application.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/meeting")
@Validated
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping
    public ResponseEntity<Meeting> addMeeting(@RequestBody Meeting meeting) throws IOException {
        Meeting savedMeeting = meetingService.addMeeting(meeting);
        return new ResponseEntity<>(savedMeeting, HttpStatus.CREATED);
    }

    @DeleteMapping("/{meetingId}/responsiblePerson/{personId}")
    public ResponseEntity<Meeting> deleteMeeting(@PathVariable Long meetingId, @PathVariable Long personId) throws IOException {
        Meeting deleteMeeting = meetingService.deleteMeeting(meetingId, personId);
        return new ResponseEntity<>(deleteMeeting, HttpStatus.OK);
    }

    @PostMapping("/{meetingId}/addPerson")
    public ResponseEntity<PersonResponse> addPersonToMeeting(@PathVariable Long meetingId, @Valid @RequestBody Person person) throws IOException {
        PersonResponse personResponse = meetingService.addPersonToMeeting(meetingId, person);
        return new ResponseEntity<>(personResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{meetingId}/personToDelete/{personId}")
    public ResponseEntity<Person> removePersonFromMeeting(@PathVariable Long meetingId, @PathVariable Long personId) throws IOException {
        Person removePerson = meetingService.removePersonFromMeeting(meetingId, personId);
        return new ResponseEntity<>(removePerson, HttpStatus.OK);
    }

    @GetMapping("/filteredMeetings")
    public Set<Meeting> getFilteredMeetings(@RequestParam(value = "description", required = false) String description,
                                            @RequestParam(value = "responsiblePerson", required = false) String responsiblePerson,
                                            @RequestParam(value = "category", required = false) String category,
                                            @RequestParam(value = "type", required = false) String type,
                                            @RequestParam(value = "startDate", required = false)
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam(value = "endDate", required = false)
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @RequestParam(value = "numberOfAttendees", required = false) Integer numberOfAttendees) throws IOException {
        return meetingService.getFilteredMeetings(description, responsiblePerson, category, type, startDate, endDate, numberOfAttendees);
    }

    @GetMapping
    public Iterable<Meeting> getAllMeetings() throws IOException {
        return meetingService.getAllMeetings();
    }
}
