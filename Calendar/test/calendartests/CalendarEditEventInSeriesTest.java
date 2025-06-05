package calendartests;

import org.junit.Test;

import java.time.DayOfWeek;
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

public class CalendarEditEventInSeriesTest extends AbstractCalendarTest {
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
}
