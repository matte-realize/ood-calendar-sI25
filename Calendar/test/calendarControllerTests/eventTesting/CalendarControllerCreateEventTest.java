package calendarControllerTests.eventTesting;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import controller.eventCommands.CreateEventCommand;
import model.event.EventInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test that tests for the controller being able to create events.
 */
public class CalendarControllerCreateEventTest extends AbstractControllerEventTest {
  @Test
  public void testCreateEvent() {
    LocalDateTime start = LocalDateTime.of(2025, 8, 10, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 8, 10, 10, 0);

    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    EventInterface event = calendarManagement.getSelectedCalendar()
            .getEvent("Event One", start, end);

    assertEquals("Event One", event.getSubject());
    assertEquals("2025-08-10T09:00", start.toString());
    assertEquals("2025-08-10T10:00", end.toString());
  }

  @Test
  public void testCreateEventSeriesByRange() {
    String createEventSeriesByRange = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-07-01";
    CreateEventCommand createCommand = new CreateEventCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommand.execute();
  }

  @Test
  public void testCreateEventSeriesByInstances() {
    String createEventSeriesByInstances = " event \"Event Twelve\" on 2025-06-10 repeats T for 4 times";
    CreateEventCommand createCommand = new CreateEventCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommand.execute();
  }

  @Test
  public void testInvalidCreateEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateEventCommand createCommand = new CreateEventCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    assertThrows(IllegalArgumentException.class, () -> {
      createCommand.execute();
    });
  }

  @Test
  public void testInvalidCreateEventSeries() {
    String createEventSeriesByRange = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-07-01";
    CreateEventCommand createCommand1 = new CreateEventCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommand1.execute();
    assertThrows(IllegalArgumentException.class, () -> {
      createCommand1.execute();
    });

    String createEventSeriesByInstances = " event \"Event Twelve\" on 2025-08-12 repeats T for 4 times";
    CreateEventCommand createCommand2 = new CreateEventCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommand2.execute();
    assertThrows(IllegalArgumentException.class, () -> {
      createCommand2.execute();
    });
  }
}
