package com.sarunas.Visma.meeting.application;

import com.sarunas.Visma.meeting.application.model.Meeting;
import com.sarunas.Visma.meeting.application.model.MeetingCategory;
import com.sarunas.Visma.meeting.application.model.MeetingType;
import com.sarunas.Visma.meeting.application.model.Person;
import com.sarunas.Visma.meeting.application.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = VismaMeetingApplicationTests.class)
@SpringBootTest
class VismaMeetingApplicationTests {

	@MockBean
	private MeetingService meetingService;

	@Test
	public void saveMeeting() throws IOException {
		Meeting meeting = new Meeting(1L, "Jonas Visma Meeting", new Person(1L, "Jonas", "jonas@mail.com"),
				"Jonas meeting", MeetingCategory.HUB,
				MeetingType.LIVE, LocalDateTime.of(2023, Month.MAY, 30, 12, 45, 00),
				LocalDateTime.of(2023, Month.MAY, 30, 13, 00, 00),
				List.of(new Person(1L, "Jonas", "jonas@mail.com")));
		when(meetingService.addMeeting(meeting)).thenReturn(meeting);
		assertEquals(meeting, meetingService.addMeeting(meeting));
	}


	@Test
	public void deleteMeeting() throws IOException {
		Meeting meeting = new Meeting(1L, "Jonas Visma Meeting", new Person(1L, "Jonas", "jonas@mail.com"),
				"Jonas meeting", MeetingCategory.HUB,
				MeetingType.LIVE, LocalDateTime.of(2023, Month.MAY, 30, 12, 45, 00),
				LocalDateTime.of(2023, Month.MAY, 30, 13, 00, 00),
				List.of(new Person(1L, "Jonas", "jonas@mail.com")));
		when(meetingService.addMeeting(meeting)).thenReturn(meeting);
		meetingService.deleteMeeting(1L, 1L);
		assertEquals(0, meetingService.getAllMeetings().size());
	}

	@Test
	public void addPerson() throws IOException {
		Meeting meeting = new Meeting(1L, "Jonas Visma Meeting", new Person(1L, "Jonas", "jonas@mail.com"),
				"Jonas meeting", MeetingCategory.HUB,
				MeetingType.LIVE, LocalDateTime.of(2023, Month.MAY, 30, 12, 45, 00),
				LocalDateTime.of(2023, Month.MAY, 30, 13, 00),
				new ArrayList<>(List.of(new Person(1L, "Jonas", "jonas@mail.com"),
						new Person(2L, "Peter", "peter@mail.com"))));
		doAnswer(invocation -> {
			Person person = invocation.getArgument(1);
			meeting.getPersons().add(person);
			return null;
		}).when(meetingService).addPersonToMeeting(eq(1L), any(Person.class));

		Person person = new Person(3L, "John Snow", "john.snow@mail.com");
		meetingService.addPersonToMeeting(1L, person);
		assertEquals(3, meeting.getPersons().size());
	}
}
