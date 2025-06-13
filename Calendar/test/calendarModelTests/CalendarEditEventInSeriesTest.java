package calendarModelTests;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import model.enums.EditMode;
import model.event.Event;
import model.event.EventInterface;
import model.event.EventSeries;
import model.enums.Location;
import model.enums.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * A JUnit test that tests the edit methods of events in a series
 * within the Calendar class.
 */
public class CalendarEditEventInSeriesTest extends AbstractCalendarTest {
  @Test
  public void testEditSingleEventInSeries() {
    EventSeries multiDaySeries = calendar.createEventSeries(
            "Workout",
            LocalDateTime.of(2025, 6, 2, 8, 0),
            null,
            List.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
            4,
            "Weekly workouts",
            Location.PHYSICAL,
            Status.PUBLIC
    );

    Event target = multiDaySeries.getInstances().get(1);
    String originalSubject = target.getSubject();
    LocalDateTime targetStart = target.getStartDateTime();
    LocalDateTime targetEnd = target.getEndDateTime();

    EventInterface edit = new Event.CustomEventBuilder()
            .setSubject("Modified Workout")
            .setStartDateTime(targetStart)
            .setEndDateTime(targetEnd)
            .setDescription("Weekly workouts")
            .setLocation(Location.PHYSICAL)
            .setStatus(Status.PUBLIC)
            .build();

    calendar.editEvent("Workout", targetStart, edit, EditMode.SINGLE);

    EventInterface updated = calendar.getEvent("Modified Workout", targetStart, targetEnd);
    assertNotNull(updated);
    assertEquals("Modified Workout", updated.getSubject());

    EventInterface original = calendar.getEvent("Workout", targetStart, targetEnd);
    assertNull(original);

    for (Event e : multiDaySeries.getInstances()) {
      if (!e.getStartDateTime().equals(targetStart)) {
        EventInterface unchanged = calendar.getEvent(originalSubject,
                e.getStartDateTime(),
                e.getEndDateTime());
        assertNotNull(unchanged);
        assertEquals(originalSubject, unchanged.getSubject());
      }
    }
  }

  @Test
  public void testEditEventAndFutureInSeries() {
    EventSeries series = calendar.createEventSeries(
            "Team Meeting",
            LocalDateTime.of(2025, 6, 3, 8, 0),
            null,
            List.of(DayOfWeek.TUESDAY),
            4,
            "Weekly team sync",
            Location.ONLINE,
            Status.PUBLIC
    );

    Event target = series.getInstances().get(1);
    LocalDateTime targetStart = target.getStartDateTime();
    LocalDateTime targetEnd = target.getEndDateTime();

    EventInterface edit = new Event.CustomEventBuilder()
            .setSubject("Strategic Planning")
            .setStartDateTime(targetStart)
            .setEndDateTime(targetEnd)
            .setDescription("Weekly team sync")
            .setLocation(Location.PHYSICAL)
            .setStatus(Status.PUBLIC)
            .build();

    calendar.editEvent("Team Meeting", targetStart, edit, EditMode.FUTURE);

    Event firstInstance = series.getInstances().get(0);
    EventInterface firstUnchanged = calendar.getEvent("Team Meeting",
            firstInstance.getStartDateTime(),
            firstInstance.getEndDateTime());
    assertEquals("Team Meeting", firstUnchanged.getSubject());
    assertEquals(Location.ONLINE, firstUnchanged.getLocation());

    for (int i = 1; i < series.getInstances().size(); i++) {
      Event instance = series.getInstances().get(i);
      EventInterface updated = calendar.getEvent("Strategic Planning",
              instance.getStartDateTime(),
              instance.getEndDateTime());
      assertEquals("Strategic Planning", updated.getSubject());
      assertEquals(Location.PHYSICAL, updated.getLocation());

      EventInterface original = calendar.getEvent("Team Meeting",
              instance.getStartDateTime(),
              instance.getEndDateTime());
      assertNull(original);
    }
  }

  @Test
  public void testEditAllEventsInSeries() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 5, 8, 0);

    EventSeries series = calendar.createEventSeries(
            "Therapy",
            start,
            null,
            List.of(DayOfWeek.THURSDAY),
            4,
            "Session with Dr.Shine",
            Location.ONLINE,
            Status.PRIVATE
    );

    Event firstInstance = series.getInstances().get(0);
    LocalDateTime editStart = firstInstance.getStartDateTime();

    EventInterface edit = new Event.CustomEventBuilder()
            .setSubject("Therapy Session")
            .setStartDateTime(editStart)
            .setEndDateTime(editStart.withHour(17).withMinute(0))
            .setDescription("Session with Dr.Shine")
            .setLocation(Location.ONLINE)
            .setStatus(Status.PRIVATE)
            .build();

    calendar.editEvent("Therapy", editStart, edit, EditMode.ALL);

    for (Event instance : series.getInstances()) {
      EventInterface updated = calendar.getEvent("Therapy Session",
              instance.getStartDateTime(),
              instance.getStartDateTime().withHour(17).withMinute(0));
      assertNotNull(updated);
      assertEquals("Therapy Session", updated.getSubject());
      assertEquals("Session with Dr.Shine", updated.getDescription());
      assertEquals(Location.ONLINE, updated.getLocation());
      assertEquals(Status.PRIVATE, updated.getStatus());

      EventInterface original = calendar.getEvent("Therapy",
              instance.getStartDateTime(),
              instance.getEndDateTime());
      assertNull(original);
    }

    assertNotNull(calendar.getEvent("Therapy Session",
            LocalDateTime.of(2025, 6, 5, 8, 0),
            LocalDateTime.of(2025, 6, 5, 17, 0)));

    assertNotNull(calendar.getEvent("Therapy Session",
            LocalDateTime.of(2025, 6, 12, 8, 0),
            LocalDateTime.of(2025, 6, 12, 17, 0)));

    assertNotNull(calendar.getEvent("Therapy Session",
            LocalDateTime.of(2025, 6, 19, 8, 0),
            LocalDateTime.of(2025, 6, 19, 17, 0)));

    assertNotNull(calendar.getEvent("Therapy Session",
            LocalDateTime.of(2025, 6, 26, 8, 0),
            LocalDateTime.of(2025, 6, 26, 17, 0)));
  }
}