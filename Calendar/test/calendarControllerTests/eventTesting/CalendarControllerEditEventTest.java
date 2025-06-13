package calendarControllerTests.eventTesting;

import org.junit.Test;

import controller.CreateCommand;
import controller.EditCommand;

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
  }

  @Test
  public void testEditFuture() {
    String createEventSeries = " event \"Event Five\" from 2025-06-09T07:00 to 2025-06-09T08:00 "
            + "repeats MW for 10 times";
    CreateCommand createCommand = new CreateCommand(createEventSeries,
            calendarManagement, calendarView);
    createCommand.execute();

    String editEventSeriesIncludingAndFuture = "edit series subject \"Event Five\" "
            + "from 2025-06-09T07:00 with edit";
    EditCommand editCommand = new EditCommand(editEventSeriesIncludingAndFuture,
            calendarManagement, calendarView);
    editCommand.execute();
  }

  @Test
  public void testEditAll() {
    String createEventSeries = " event \"Event Five\" from 2025-06-09T07:00 to 2025-06-09T08:00 "
            + "repeats MW for 10 times";
    CreateCommand createCommand = new CreateCommand(createEventSeries,
            calendarManagement, calendarView);
    createCommand.execute();

    String editEventSeriesAll = "edit events subject \"edit\" from 2025-06-16T07:00 with edit2";
    EditCommand editCommand = new EditCommand(editEventSeriesAll,
            calendarManagement, calendarView);
    editCommand.execute();
  }
}
