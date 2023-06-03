package com.sarunas.Visma.meeting.application.service;

import com.sarunas.Visma.meeting.application.exception.MeetingNotFoundException;
import com.sarunas.Visma.meeting.application.exception.PersonExistsInMeetingException;
import com.sarunas.Visma.meeting.application.exception.PersonNotFoundException;
import com.sarunas.Visma.meeting.application.exception.RightToDeleteException;
import com.sarunas.Visma.meeting.application.model.Meeting;
import com.sarunas.Visma.meeting.application.model.Person;
import com.sarunas.Visma.meeting.application.model.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private FileService fileService;

    @Override
    public Meeting addMeeting(Meeting meeting) throws IOException {
        List<Meeting> allMeetings = fileService.getMeetings();
        meeting.setId(generateId());
        List<Person> persons = meeting.getPersons();
        persons.add(meeting.getResponsiblePerson());
        meeting.setPersons(persons);
        allMeetings.add(meeting);
        fileService.saveMeeting(allMeetings);
        return meeting;
    }

    @Override
    public Meeting deleteMeeting(Long meetingId, Long personId) throws IOException {
        List<Meeting> allMeetings = fileService.getMeetings();
        Optional<Meeting> optionalMeetingToDelete = allMeetings.stream()
                .filter(meeting -> meeting.getId().equals(meetingId)).findAny();
        if (optionalMeetingToDelete.isEmpty()) {
            throw new MeetingNotFoundException("Such meeting doesn't exist");
        }
        Meeting meetingToDelete = optionalMeetingToDelete.get();
        if (!personId.equals(meetingToDelete.getResponsiblePerson().getId())) {
            throw new RightToDeleteException("You have no right to delete meeting!");
        }
        allMeetings.remove(meetingToDelete);
        fileService.saveMeeting(allMeetings);
        return meetingToDelete;
    }

    @Override
    public PersonResponse addPersonToMeeting(Long meetingId, Person person) throws IOException {
        List<Meeting> allMeetings = fileService.getMeetings();

        Optional<Meeting> optionalMeeting = allMeetings.stream()
                .filter(m -> m.getId().equals(meetingId)).findAny();
        if (optionalMeeting.isEmpty()) {
            throw new MeetingNotFoundException("Such meeting doesn't exist");
        }
        Meeting meeting = optionalMeeting.get();
        allMeetings.remove(meeting);
        List<Person> persons = meeting.getPersons();
        Optional<Person> optionalExistingPerson = persons.stream()
                .filter(p -> p.getId().equals(person.getId())).findAny();
        if (optionalExistingPerson.isPresent()) {
            throw new PersonExistsInMeetingException("Such person is in the meeting");
        }
        persons.add(person);
        meeting.setPersons(persons);
        allMeetings.add(meeting);
        fileService.saveMeeting(allMeetings);
        PersonResponse personResponse = new PersonResponse();
        personResponse.setId(person.getId());
        personResponse.setName(person.getName());
        personResponse.setEmail(person.getEmail());
        return personResponse;
    }

    @Override
    public Person removePersonFromMeeting(Long meetingId, Long personId) throws IOException {
        List<Meeting> allMeetings = fileService.getMeetings();

        Optional<Meeting> optionalMeeting = allMeetings.stream()
                .filter(m -> m.getId().equals(meetingId)).findAny();
        if (optionalMeeting.isEmpty()) {
            throw new MeetingNotFoundException("Such meeting doesn't exist");
        }
        Meeting meeting = optionalMeeting.get();
        allMeetings.remove(meeting);
        if (personId.equals(meeting.getResponsiblePerson().getId())) {
            throw new RightToDeleteException("You cannot remove responsible person");
        }
        List<Person> persons = meeting.getPersons();
        Optional<Person> optionalPersonToRemove = persons.stream().filter(m -> m.getId().equals(personId)).findAny();
        if (optionalPersonToRemove.isEmpty()) {
            throw new PersonNotFoundException("Such person doesn't exist in meeting");
        }
        Person personRemove = optionalPersonToRemove.get();
        persons.remove(personRemove);
        meeting.setPersons(persons);
        allMeetings.add(meeting);
        fileService.saveMeeting(allMeetings);
        return personRemove;
    }

    @Override
    public Set<Meeting> getFilteredMeetings(String description, String responsiblePerson,
                                     String category, String type, LocalDate startDate,
                                     LocalDate endDate, Integer numberOfAttendees) throws IOException {
        Set<Meeting> filteredMeetings = new HashSet<>();
        List<Meeting> meetings = fileService.getMeetings();
        if (Objects.nonNull(description)) {
            filteredMeetings.addAll(filterByDescription(meetings, description));
        }
        if (Objects.nonNull(responsiblePerson)) {
            filteredMeetings.addAll(filterByResponsiblePerson(meetings, responsiblePerson));
        }
        if (Objects.nonNull(category)) {
            filteredMeetings.addAll(filterByCategory(meetings, category));
        }
        if (Objects.nonNull(type)) {
            filteredMeetings.addAll(filterByType(meetings, type));
        }
        if (Objects.nonNull(startDate)) {
            filteredMeetings.addAll(filterByStartDate(meetings, startDate));
        }
        if (Objects.nonNull(endDate)) {
            filteredMeetings.addAll(filterByEndDate(meetings, endDate));
        }
        if (Objects.nonNull(numberOfAttendees)) {
            filteredMeetings.addAll(filterByNumberOfAttendees(meetings, numberOfAttendees));
        }
        return filteredMeetings;
    }

    @Override
    public List<Meeting> getAllMeetings() throws IOException {
        return fileService.getMeetings();
    }

    private List<Meeting> filterByDescription(List<Meeting> meetings, String description) {
        return meetings.stream().filter(meeting -> description.equalsIgnoreCase(meeting.getDescription()))
                .collect(Collectors.toList());
    }

    private List<Meeting> filterByResponsiblePerson(List<Meeting> meetings, String responsiblePerson) {
        return meetings.stream().filter(meeting -> responsiblePerson
                .equalsIgnoreCase(meeting.getResponsiblePerson().getEmail()) || responsiblePerson
                .equalsIgnoreCase(meeting.getResponsiblePerson().getName())).collect(Collectors.toList());
    }

    private List<Meeting> filterByCategory(List<Meeting> meetings, String category) {
        return meetings.stream().filter(meeting -> category.equalsIgnoreCase(meeting.getCategory()
                .getValue())).collect(Collectors.toList());
    }

    private List<Meeting> filterByType(List<Meeting> meetings, String type) {
        return meetings.stream().filter(meeting -> type.equalsIgnoreCase(meeting.getType()
                .getValue())).collect(Collectors.toList());
    }

    private List<Meeting> filterByStartDate(List<Meeting> meetings, LocalDate startDate) {
        return meetings.stream().filter(meeting -> meeting.getStartDate().isAfter(startDate.atStartOfDay()))
                .collect(Collectors.toList());
    }

    private List<Meeting> filterByEndDate(List<Meeting> meetings, LocalDate endDate) {
        return meetings.stream().filter(meeting -> meeting.getEndDate().compareTo(endDate.atStartOfDay()) <= 0)
                .collect(Collectors.toList());
    }

    private List<Meeting> filterByNumberOfAttendees(List<Meeting> meetings, int numberOfAttendees) {
        return meetings.stream().filter(meeting -> meeting.getPersons().size() >= numberOfAttendees)
                .collect(Collectors.toList());
    }

    private Long generateId() {
        long timestamp = System.currentTimeMillis();
        return timestamp * 1000 + new Random().nextInt(1000);
    }
}
