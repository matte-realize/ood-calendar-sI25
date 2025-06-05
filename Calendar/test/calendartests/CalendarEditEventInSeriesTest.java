package calendartests;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import Model.EditMode;
import Model.Event;
import Model.EventInterface;
import Model.EventSeries;
import Model.Location;
import Model.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A JUnit test that tests the edit methods of events in a series
 * within the Calendar class.
 */
public class CalendarEditEventInSeriesTest extends AbstractCalendarTest {
  @Test
  public void testEditSingleEventInSeries() {
    EventSeries multiDaySeries = calendar.createEventSeries(
            "Workout",
            LocalDateTime.of(2025, 6, 2, 7, 0),
            LocalDateTime.of(2025, 6, 16, 7, 0),
            List.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
            "Weekly workouts",
            Location.PHYSICAL,
            Status.PUBLIC
    );

    Event target = multiDaySeries.getInstances().get(1);
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

    for (Event e : multiDaySeries.getInstances()) {
      if (!e.getStartDateTime().equals(targetStart)) {
        assertEquals(originalSubject, e.getSubject());
      }
    }
  }

  @Test
  public void testEditAllEventsInSeries() {
    EventSeries series = calendar.createEventSeries(
            "Therapy",
            LocalDateTime.of(2025, 6, 5, 16, 0),
            LocalDateTime.of(2025, 6, 26, 16, 0),
            List.of(DayOfWeek.THURSDAY),
            "Session with Dr.Shine",
            Location.ONLINE,
            Status.PRIVATE
    );

    EventInterface edit = new Event.CustomEventBuilder()
            .setSubject("Therapy Session")
            .setStartDateTime(series.getStartDateTime())
            .build();

    calendar.editEvent("Therapy", series.getStartDateTime(), edit, EditMode.ALL);

    assertNotNull(calendar.getEvent("Therapy Session", LocalDate.of(2025, 6, 5)));
    assertNotNull(calendar.getEvent("Therapy Session", LocalDate.of(2025, 6, 12)));
    assertNotNull(calendar.getEvent("Therapy Session", LocalDate.of(2025, 6, 19)));
    assertNotNull(calendar.getEvent("Therapy Session", LocalDate.of(2025, 6, 26)));

    /*for (Event e : series.getInstances()) {
      EventInterface updated = calendar.getEvent("Therapy Session", e.getStartDateTime().toLocalDate());
      assertNotNull(updated);
      assertEquals("Therapy Session", updated.getSubject());
    }*/
  }
}
