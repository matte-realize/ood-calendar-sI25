package calendarcontrollertests.eventtesting;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import controller.CreateCommand;
import controller.EditCommand;
import model.event.Event;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests for the controller being able to edit events.
 */
public class CalendarControllerEditEventTest extends AbstractControllerEventTest {

  @Test
  public void testEditSingleEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateCommand createCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    String editEvent = " event subject \"Event One\" from 2025-08-10T09:00 to "
            + "2025-08-10T10:00 with Edited Event One";
    EditCommand editCommand = new EditCommand(editEvent,
            calendarManagement, calendarView);
    editCommand.execute();

    List<Event> events = calendarManagement
            .getSelectedCalendar().getEventsSingleDay(LocalDate.parse("2025-08-10"));

    assertEquals("Edited Event One", events.get(0).getSubject());
  }

  @Test
  public void testEditFuture() {
    String createEventSeries = " event \"Event Five\" from 2025-06-09T07:00 to 2025-06-09T08:00 "
            + "repeats MW for 10 times";
    CreateCommand createCommand = new CreateCommand(createEventSeries,
            calendarManagement, calendarView);
    createCommand.execute();

    String editEventSeriesIncludingAndFuture = " series subject \"Event Five\" "
            + "from 2025-06-09T07:00 with edit";
    EditCommand editCommand = new EditCommand(editEventSeriesIncludingAndFuture,
            calendarManagement, calendarView);
    editCommand.execute();

    List<Event> events = calendarManagement
            .getSelectedCalendar().getEventsSingleDay(LocalDate.parse("2025-06-09"));

    assertEquals("edit", events.get(0).getSubject());
  }

  @Test
  public void testEditAll() {
    String createEventSeries = " event \"Event Five\" from 2025-06-09T07:00 to 2025-06-09T08:00 "
            + "repeats MW for 10 times";
    CreateCommand createCommand = new CreateCommand(createEventSeries,
            calendarManagement, calendarView);
    createCommand.execute();

    String editEventSeriesAll = " events subject \"Event Five\" from 2025-06-16T07:00 with edit2";
    EditCommand editCommand = new EditCommand(editEventSeriesAll,
            calendarManagement, calendarView);
    editCommand.execute();

    List<Event> events = calendarManagement
            .getSelectedCalendar().getEventsSingleDay(LocalDate.parse("2025-06-16"));

    assertEquals("edit2", events.get(0).getSubject());
  }
}
