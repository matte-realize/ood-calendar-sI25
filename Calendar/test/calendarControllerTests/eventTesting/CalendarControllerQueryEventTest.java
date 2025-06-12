package calendarControllerTests.eventTesting;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;
import controller.eventCommands.QueryEventCommand;

/**
 * A JUnit test that tests for the controller being able to query events.
 */
public class CalendarControllerQueryEventTest extends AbstractControllerEventTest {
  @Test
  public void testQueryEventsOnADay() {

  }

  @Test
  public void testQueryEventsOnARange() {
    
  }

  @Test
  public void testPrintEventsOnADay() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent, calendarManagement, calendarView);
    createCommand.execute();

    String printEvent = " events on 2025-08-10";
    QueryEventCommand printCommand = new QueryEventCommand(printEvent, "print", calendarManagement, calendarView);
    printCommand.execute();
  }

  @Test
  public void testPrintEventsOnARange() {
    String createEventSeriesByRange = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-07-01";
    CreateEventCommand createCommandByRange = new CreateEventCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommandByRange.execute();

    String printEventOnRange1 = " events from 2025-06-08T00:00 to 2025-06-22T00:00";
    QueryEventCommand printCommand1 = new QueryEventCommand(printEventOnRange1, "print", calendarManagement, calendarView);
    printCommand1.execute();

    String createEventSeriesByInstances = " event \"Event Twelve\" on 2025-08-12 repeats T for 4 times";
    CreateEventCommand createCommandByInstances = new CreateEventCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommandByInstances.execute();

    String printEventOnRange2 = " events from 2025-08-12T00:00 to 2025-09-09T00:00";
    QueryEventCommand printCommand2 = new QueryEventCommand(printEventOnRange2, "print", calendarManagement, calendarView);
    printCommand2.execute();
  }

  @Test
  public void testDisplayExpectedEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent, calendarManagement, calendarView);
    createCommand.execute();

    String showStatus1 = "show status on 2025-06-10T09:00";
    QueryEventCommand showStatusCommand1 = new QueryEventCommand(showStatus1, "", calendarManagement, calendarView);
    showStatusCommand1.execute();

    String showStatus2 = "show status on 2025-08-10T09:00";
    QueryEventCommand showStatusCommand2 = new QueryEventCommand(showStatus2, "", calendarManagement, calendarView);
    showStatusCommand2.execute();
  }
}
