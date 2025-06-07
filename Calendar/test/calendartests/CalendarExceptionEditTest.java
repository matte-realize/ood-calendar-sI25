package calendartests;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import model.EditMode;
import model.Event;
import model.EventInterface;
import model.EventSeries;
import model.Location;
import model.Status;

import static org.junit.Assert.assertThrows;

/**
 * JUnit test for testing exceptions when editing events independently
 * or within a series.
 */
public class CalendarExceptionEditTest extends AbstractCalendarTest {
  @Test
  public void testEditingSubjectToNull() {
    EventInterface allDayEventEdit = new Event.CustomEventBuilder()
            .setSubject(null)
            .setStartDateTime(allDayTimeStart)
            .build();

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.editEvent(
              "Wedding",
              allDayTimeStart,
              allDayEventEdit,
              EditMode.SINGLE
      );
    });
  }

  @Test
  public void testEditingSubjectToEmpty() {
    EventInterface allDayEventEdit = new Event.CustomEventBuilder()
            .setSubject("")
            .setStartDateTime(allDayTimeStart)
            .build();

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.editEvent(
              "Wedding",
              allDayTimeStart,
              allDayEventEdit,
              EditMode.SINGLE
      );
    });
  }

  @Test
  public void testEditingStartDateToNull() {
    EventInterface allDayEventEdit = new Event.CustomEventBuilder()
            .setSubject("Wedding")
            .setStartDateTime(null)
            .build();

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.editEvent(
              "Wedding",
              allDayTimeStart,
              allDayEventEdit,
              EditMode.SINGLE
      );
    });
  }

  @Test
  public void testEditingNonexistentSubjectEvent() {
    EventInterface allDayEventEdit = new Event.CustomEventBuilder()
            .setSubject("Wedding")
            .setStartDateTime(LocalDateTime.of(2025, 6, 10, 7, 0))
            .build();

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.editEvent(
              "Other Event",
              allDayTimeStart,
              allDayEventEdit,
              EditMode.SINGLE
      );
    });
  }

  @Test
  public void testEditingNonexistentStartTimeEvent() {
    EventInterface allDayEventEdit = new Event.CustomEventBuilder()
            .setSubject("Wedding")
            .setStartDateTime(LocalDateTime.of(2025, 6, 10, 7, 0))
            .build();

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.editEvent(
              "Wedding",
              LocalDateTime.of(2025, 6, 10, 7, 0),
              allDayEventEdit,
              EditMode.SINGLE
      );
    });
  }

  // the controller will automatically calculate occurrences
  @Test
  public void testEditEventWithoutOccurrences() {
    EventSeries multiDaySeries = calendar.createEventSeries(
            "Workout",
            LocalDateTime.of(2025, 6, 2, 8, 0),
            null,
            List.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
            0,
            "Weekly workouts",
            Location.PHYSICAL,
            Status.PUBLIC
    );

    // prepare for editing
    assertThrows(IndexOutOfBoundsException.class, () -> {
      Event target = multiDaySeries.getInstances().get(1);
    });
  }
}
