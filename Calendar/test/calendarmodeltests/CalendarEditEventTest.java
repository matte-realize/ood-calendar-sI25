package calendarmodeltests;

import org.junit.Test;

import java.time.LocalDateTime;

import model.enums.EditMode;
import model.event.Event;
import model.event.EventInterface;
import model.enums.Location;
import model.enums.Status;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests the edit methods within the Calendar class within
 * events that are not part of the series.
 */
public class CalendarEditEventTest extends AbstractCalendarTest {
  @Test
  public void testEditEventOnlySubject() {
    LocalDateTime allDayTimeStart = LocalDateTime.of(2025, 6, 10, 8, 0);
    EventInterface allDayEventEdit = new Event.CustomEventBuilder()
            .setSubject("Harry's Wedding")
            .setStartDateTime(allDayTimeStart)
            .build();

    calendar.editEvent(
            "Wedding",
            allDayTimeStart,
            allDayEventEdit,
            EditMode.SINGLE
    );
    EventInterface updatedAllDayEvent = calendar.getEvent("Harry's Wedding", allDayTimeStart, null);
    assertEquals("Harry's Wedding", updatedAllDayEvent.getSubject());

    LocalDateTime detailedTimeStart = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime detailedTimeEnd = LocalDateTime.of(2025, 7, 14, 10, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting with Lawyers")
            .setStartDateTime(detailedTimeStart)
            .setEndDateTime(detailedTimeEnd)
            .build();

    calendar.editEvent(
            "Meeting",
            detailedTimeStart,
            detailedEventEdit,
            EditMode.SINGLE
    );
    EventInterface updatedDetailedEvent =
            calendar.getEvent(
                    "Meeting with Lawyers",
                    detailedTimeStart,
                    detailedTimeEnd);
    assertEquals("Meeting with Lawyers", updatedDetailedEvent.getSubject());
  }

  @Test
  public void testEditEventOnlyStartTime() {
    LocalDateTime allDayTimeStart = LocalDateTime.of(2025, 6, 10, 8, 0);
    LocalDateTime allDayUpdateTimeStart = LocalDateTime.of(2025, 6, 11, 8, 0);

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

    String expectedWeddingStartDateTime = LocalDateTime.of(2025, 6, 11, 8, 0).toString();
    EventInterface updatedAllDayEvent = calendar.getEvent("Wedding", allDayUpdateTimeStart, null);

    assertEquals(expectedWeddingStartDateTime, updatedAllDayEvent.getStartDateTime().toString());

    LocalDateTime detailedTimeStart = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime detailedTimeEnd = LocalDateTime.of(2025, 7, 14, 11, 0);
    LocalDateTime detailedTimeStartUpdate = LocalDateTime.of(2025, 7, 14, 10, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(detailedTimeStartUpdate)
            .setEndDateTime(detailedTimeEnd)
            .build();

    calendar.editEvent(
            "Meeting",
            detailedTimeStart,
            detailedEventEdit,
            EditMode.SINGLE
    );

    String expectedMeetingStartDateTime = LocalDateTime.of(2025, 7, 14, 10, 0).toString();
    EventInterface updatedDetailedEvent =
            calendar.getEvent(
                    "Meeting",
                    detailedTimeStartUpdate,
                    detailedTimeEnd
            );

    assertEquals(expectedMeetingStartDateTime, updatedDetailedEvent.getStartDateTime().toString());
  }

  @Test
  public void testEditEventOnlyEndTime() {
    LocalDateTime startTime = LocalDateTime.of(2025, 7, 14, 9, 0);
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
    LocalDateTime startTime = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 7, 14, 10, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(startTime)
            .setEndDateTime(endTime)
            .setDescription("Discussion about politics.")
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );
    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", startTime, endTime);

    assertEquals("Discussion about politics.", updatedDetailedEvent.getDescription());
  }

  @Test
  public void testEditEventOnlyLocation() {
    LocalDateTime startTime = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 7, 14, 10, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(startTime)
            .setEndDateTime(endTime)
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
    LocalDateTime startTime = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 7, 14, 10, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(startTime)
            .setEndDateTime(endTime)
            .setStatus(Status.PUBLIC)
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );

    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", startTime, endTime);

    assertEquals(Status.PUBLIC, updatedDetailedEvent.getStatus());
  }

  @Test
  public void testEditEventMultipleAttributes() {
    LocalDateTime startTime = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 7, 14, 10, 0);
    LocalDateTime updateStartTime = LocalDateTime.of(2025, 7, 15, 12, 0);
    LocalDateTime updateEndTime = LocalDateTime.of(2025, 7, 15, 15, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Important Meeting")
            .setStartDateTime(updateStartTime)
            .setEndDateTime(updateEndTime)
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );

    EventInterface updatedDetailedEvent =
            calendar.getEvent(
                    "Important Meeting",
                    updateStartTime,
                    updateEndTime
            );

    assertEquals("Important Meeting", updatedDetailedEvent.getSubject());
    assertEquals(updateStartTime.toString(), updatedDetailedEvent.getStartDateTime().toString());
    assertEquals(updateEndTime.toString(), updatedDetailedEvent.getEndDateTime().toString());
  }

  @Test
  public void testMultipleEdits() {
    LocalDateTime startTime = LocalDateTime.of(2025, 7, 14, 9, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 7, 14, 10, 0);
    LocalDateTime startTimeUpdate = LocalDateTime.of(2025, 7, 14, 10, 0);

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(startTimeUpdate)
            .setEndDateTime(endTime)
            .build();

    calendar.editEvent(
            "Meeting",
            startTime,
            detailedEventEdit,
            EditMode.SINGLE
    );

    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting", startTimeUpdate, endTime);

    assertEquals(startTimeUpdate.toString(), updatedDetailedEvent.getStartDateTime().toString());

    EventInterface detailedEventEdit2 = new Event.CustomEventBuilder()
            .setSubject("Important Meeting")
            .setStartDateTime(startTimeUpdate)
            .setEndDateTime(endTime)
            .build();

    calendar.editEvent(
            "Meeting",
            startTimeUpdate,
            detailedEventEdit2,
            EditMode.SINGLE
    );

    EventInterface updatedDetailedEvent2 =
            calendar.getEvent(
                    "Important Meeting",
                    startTimeUpdate,
                    endTime
            );

    assertEquals("Important Meeting", updatedDetailedEvent2.getSubject());
  }
}
