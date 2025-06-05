import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import Model.Calendar;
import Model.EditMode;
import Model.Event;
import Model.EventInterface;
import Model.EventSeries;
import Model.Location;
import Model.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CalendarTest {
  private Calendar calendar;

  @Before
  public void setup() {
    calendar = new Calendar();
  }

  @Test
  public void testCreateEventReturnsCorrectArguments() {
    EventInterface allDayEvent = calendar.createEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            null,
            null,
            null,
            null
    );

    String expectedWeddingStartDateTime = LocalDateTime.of(2025, 6, 10, 0, 0).toString();

    assertNotNull(allDayEvent);
    assertEquals("Wedding", allDayEvent.getSubject());
    assertEquals(expectedWeddingStartDateTime, allDayEvent.getStartDateTime().toString());

    EventInterface detailedEvent = calendar.createEvent(
      "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            LocalDateTime.of(2025, 7, 14, 10, 0),
            "Discussion about ethics.",
            Location.ONLINE,
            Status.PRIVATE
    );

    String expectedDetailedStartDateTime = LocalDateTime.of(2025, 7, 14, 9, 0).toString();
    String expectedDetailedEndDateTime = LocalDateTime.of(2025, 7, 14, 10, 0).toString();

    assertNotNull(detailedEvent);
    assertEquals("Wedding", allDayEvent.getSubject());
    assertEquals(expectedDetailedStartDateTime, detailedEvent.getStartDateTime().toString());
    assertEquals(expectedDetailedEndDateTime, detailedEvent.getEndDateTime().toString());
    assertEquals("Discussion about ethics.", detailedEvent.getDescription());
    assertEquals(Location.ONLINE, detailedEvent.getLocation());
    assertEquals(Status.PRIVATE, detailedEvent.getStatus());
  }

  @Test
  public void testEditEventOnlySubject() {
    EventInterface allDayEvent = calendar.createEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            null,
            null,
            null,
            null
    );

    EventInterface allDayEventEdit = new Event.CustomEventBuilder()
            .setSubject("Harry's Wedding")
            .setStartDateTime(LocalDateTime.of(2025, 6, 10, 0, 0))
            .build();

    calendar.editEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            allDayEventEdit,
            EditMode.SINGLE
    );

    EventInterface updatedAllDayEvent = calendar.getEvent("Harry's Wedding", LocalDate.of(2025, 6, 10));

    assertEquals("Harry's Wedding", updatedAllDayEvent.getSubject());

    EventInterface detailedEvent = calendar.createEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            LocalDateTime.of(2025, 7, 14, 10, 0),
            "Discussion about ethics.",
            Location.ONLINE,
            Status.PRIVATE
    );

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting with Lawyers")
            .setStartDateTime(LocalDateTime.of(2025, 7, 14, 9, 0))
            .build();

    calendar.editEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            detailedEventEdit,
            EditMode.SINGLE
    );

    EventInterface updatedDetailedEvent = calendar.getEvent("Meeting with Lawyers", LocalDate.of(2025, 7, 14));

    assertEquals("Meeting with Lawyers", updatedDetailedEvent.getSubject());
  }

  @Test
  public void testEditEventOnlyTime() {
    EventInterface allDayEvent = calendar.createEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            null,
            null,
            null,
            null
    );

    EventInterface allDayEventUpdate = new Event.CustomEventBuilder()
            .setSubject("Wedding")
            .setStartDateTime(LocalDateTime.of(2025, 6, 11, 0, 0))
            .build();

    calendar.editEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            allDayEventUpdate,
            EditMode.SINGLE
    );

    String expectedWeddingStartDateTime = LocalDateTime.of(2025, 6, 11, 0, 0).toString();

    assertNotNull(allDayEvent);
    assertEquals("Wedding", allDayEventUpdate.getSubject());
    assertEquals(expectedWeddingStartDateTime, allDayEventUpdate.getStartDateTime().toString());

    EventInterface detailedEvent = calendar.createEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            LocalDateTime.of(2025, 7, 14, 10, 0),
            "Discussion about ethics.",
            Location.ONLINE,
            Status.PRIVATE
    );

    EventInterface detailedEventEdit = new Event.CustomEventBuilder()
            .setSubject("Meeting")
            .setStartDateTime(LocalDateTime.of(2025, 7, 14, 9, 0))
            .build();
  }

  @Test
  public void testCreateWeeklySeriesWithOccurrences() {
    String subject = "Team Meeting";
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 10, 0);
    List<DayOfWeek> repeatDays = List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    int occurrences = 4;
    EventSeries series = calendar.createEventSeries(
            subject,
            start,
            null,
            repeatDays,
            occurrences,
            null,
            Location.ONLINE,
            Status.PRIVATE
    );

    assertEquals(subject, series.getSubject());
    assertEquals(4, series.getInstances().size());

    Set<DayOfWeek> validDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    for (Event instance : series.getInstances()) {
      assertTrue(validDays.contains(instance.getStartDateTime().getDayOfWeek()));
      assertEquals(subject, instance.getSubject());
    }
  }

  @Test
  public void testEditSingleEventInSeries() {
    EventSeries series = calendar.createEventSeries(
            "Workout",
            LocalDateTime.of(2025, 6, 2, 7, 0), // Monday
            null,
            List.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
            4,
            null,
            Location.PHYSICAL,
            Status.PUBLIC
    );

    Event target = series.getInstances().get(1); // second occurrence
    String originalSubject = target.getSubject();
    LocalDateTime targetStart = target.getStartDateTime();

    EventInterface edit = new Event.CustomEventBuilder()
            .setSubject("Modified Workout")
            .setStartDateTime(targetStart)
            .build();

    calendar.editEvent("Workout", targetStart, edit, EditMode.SINGLE);

    EventInterface updated = calendar.getEvent("Modified Workout", targetStart.toLocalDate());
    assertNotNull(updated);
    assertEquals("Modified Workout", updated.getSubject());

    for (Event e : series.getInstances()) {
      if (!e.getStartDateTime().equals(targetStart)) {
        assertEquals(originalSubject, e.getSubject());
      }
    }
  }

  @Test
  public void testGetEvent() {

  }
}
