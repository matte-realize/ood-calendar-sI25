package calendartests;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Model.EditMode;
import Model.Event;
import Model.EventInterface;
import Model.Location;
import Model.Status;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests the edit methods within the Calendar class within
 * events that are not part of the series.
 */
public class CalendarEditEventTest extends AbstractCalendarTest {
  @Test
  public void testEditEventOnlySubject() {
    LocalDateTime allDayTimeStart = LocalDateTime.of(2025, 6, 10, 0, 0);

    EventInterface allDayEventEdit = new Event.CustomEventBuilder()
            .setSubject("Harry's Wedding")
            .setStartDateTime(allDayTimeStart)
            .build();

    calendar.editEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            allDayEventEdit,
            EditMode.SINGLE
    );
    EventInterface updatedAllDayEvent = calendar.getEvent("Harry's Wedding", allDayTimeStart, null);
    assertEquals("Harry's Wedding", updatedAllDayEvent.getSubject());

    LocalDateTime detailedTimeStart = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime detailedEndStart = LocalDateTime.of(2025, 7, 14, 10, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting with Lawyers")
            .setStartDateTime(detailedTimeStart)
            .setEndDateTime(detailedEndStart)
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );
    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting with Lawyers", detailedTimeStart, detailedEndStart);
    assertEquals("Meeting with Lawyers", updatedDetailedEvent.getSubject());
  }
}

  /*@Test
  public void testEditEventOnlyStartTime() {
    LocalDateTime allDayTimeStart = LocalDateTime.of(2025, 6, 10, 0, 0);
    LocalDateTime allDayUpdateTimeStart = LocalDateTime.of(2025, 6, 11, 0, 0);

    EventInterface allDayEventUpdate = new Event.CustomEventBuilder()
            .setSubject("Wedding")
            .setStartDateTime(allDayUpdateTimeStart)
            .build();

    calendar.editEvent(
            "Wedding",
            allDayTimeStart,
            allDayEventUpdate,
            EditMode.SINGLE
    );

    String expectedWeddingStartDateTime = LocalDateTime.of(2025, 6, 11, 0, 0).toString();
    EventInterface updatedAllDayEvent = calendar.getEvent("Wedding", allDayUpdateTimeStart, null);

    assertEquals(expectedWeddingStartDateTime, updatedAllDayEvent.getStartDateTime().toString());

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(LocalDateTime.of(2025, 7, 15, 9, 0))
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );

    String expectedMeetingStartDateTime = LocalDateTime.of(2025, 7, 15, 9, 0).toString();
    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", LocalDate.of(2025, 7, 15));

    assertEquals(expectedMeetingStartDateTime, updatedDetailedEvent.getStartDateTime().toString());
  }

  @Test
  public void testEditEventOnlyEndTime() {
    LocalDateTime startTime = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 7, 14, 10, 0);
    LocalDateTime endTimeUpdate = LocalDateTime.of(2025, 7, 14, 12, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(startTime)
            .setEndDateTime(endTimeUpdate)
            .build();

    calendar.editEvent(
            "Meeting",
            startTime,
            detailedEventEdit,
            EditMode.SINGLE
    );

    String expectedMeetingEndDateTime = LocalDateTime.of(2025, 7, 14, 12, 0).toString();
    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", startTime, endTimeUpdate);

    assertEquals(expectedMeetingEndDateTime, updatedDetailedEvent.getEndDateTime().toString());
  }

  @Test
  public void testEditEventOnlyDescription() {
    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(LocalDateTime.of(2025, 7, 14, 9, 0))
            .setDescription("Discussion about politics.")
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );
    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", LocalDate.of(2025, 7, 14));

    assertEquals("Discussion about politics.", updatedDetailedEvent.getDescription());
  }

  @Test
  public void testEditEventOnlyLocation() {
    LocalDateTime startTime = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 7, 14, 10, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(startTime)
            .setLocation(Location.PHYSICAL)
            .build();

    calendar.editEvent(
            "Meeting",
            startTime,
            detailedEventEdit,
            EditMode.SINGLE
    );

    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", startTime, endTime);

    assertEquals(Location.PHYSICAL, updatedDetailedEvent.getLocation());
  }

  @Test
  public void testEditEventOnlyStatus() {
    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(LocalDateTime.of(2025, 7, 14, 9, 0))
            .setStatus(Status.PUBLIC)
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );

    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", LocalDate.of(2025, 7, 14));

    assertEquals(Status.PUBLIC, updatedDetailedEvent.getStatus());
  }

  @Test
  public void testEditEventMultipleAttributes() {
    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Important Meeting")
            .setStartDateTime(LocalDateTime.of(2025, 7, 15, 12, 0))
            .setEndDateTime(LocalDateTime.of(2025, 7, 15, 15, 0))
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );

    String expectedMeetingStartDateTime = LocalDateTime.of(2025, 7, 15, 12, 0).toString();
    String expectedMeetingEndDateTime = LocalDateTime.of(2025, 7, 15, 15, 0).toString();
    EventInterface updatedDetailedEvent = calendar.getEvent("Important Meeting", LocalDate.of(2025, 7, 15));

    assertEquals("Important Meeting", updatedDetailedEvent.getSubject());
    assertEquals(expectedMeetingStartDateTime, updatedDetailedEvent.getStartDateTime().toString());
    assertEquals(expectedMeetingEndDateTime, updatedDetailedEvent.getEndDateTime().toString());
  }

  @Test
  public void testMultipleEdits() {
    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(LocalDateTime.of(2025, 7, 14, 10, 0))
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );

    String expectedMeetingStartDateTime = LocalDateTime.of(2025, 7, 14, 10, 0).toString();
    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", LocalDate.of(2025, 7, 14));

    assertEquals(expectedMeetingStartDateTime, updatedDetailedEvent.getStartDateTime().toString());

    EventInterface detailedEventEdit2 = new Event.CustomEventBuilder()
            .setSubject("Important Meeting")
            .setStartDateTime(LocalDateTime.of(2025, 7, 14, 10, 0))
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 10, 0),
            detailedEventEdit2,
            EditMode.SINGLE
    );

    EventInterface updatedDetailedEvent2 = calendar.getEvent("Important Meeting", LocalDate.of(2025, 7, 14));

    assertEquals("Important Meeting", updatedDetailedEvent2.getSubject());
  }
}
*/