package calendarControllerTests.eventTesting;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;

/**
 * A JUnit test that tests for the controller being able to create events.
 */
public class CalendarControllerCreateEventTest extends AbstractControllerEventTest {
  @Test
  public void testCreateEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent, calendarManagement);

    createCommand.execute();
  }

  @Test
  public void testCreateEventSeriesByRange() {
    String createEventSeriesByRange = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
                                      + " repeats U until 2025-07-01";
    CreateEventCommand createCommand = new CreateEventCommand(createEventSeriesByRange,
                                                              calendarManagement);

    createCommand.execute();
  }

  @Test
  public void testCreateEventSeriesByInstances() {
    String createEventSeriesByInstances = " event \"Event Twelve\" on 2025-06-10 repeats T for 4 times";

    CreateEventCommand createCommand = new CreateEventCommand(createEventSeriesByInstances,
                                                              calendarManagement);

    createCommand.execute();
  }

  @Test
  public void testInvalidCreateEvent() {

  }

  @Test
  public void testInvalidCreateEventSeries() {

  }
}
