package calendarcontrollertests.eventtesting;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Scanner;

import controller.CreateCommand;
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
    CreateCommand createCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    EventInterface event = calendarManagement.getSelectedCalendar()
            .getEvent("Event One", start, end);

    assertEquals("Event One", event.getSubject());
    assertEquals("2025-08-10T09:00", start.toString());
    assertEquals("2025-08-10T10:00", end.toString());
  }

  @Before
  public void testCreateEventSeriesByRange() {
    LocalDateTime start1 = LocalDateTime.of(2025, 6, 8, 9, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 6, 8, 10, 0);
    LocalDateTime start2 = LocalDateTime.of(2025, 6, 15, 9, 0);
    LocalDateTime end2 = LocalDateTime.of(2025, 6, 15, 10, 0);
    LocalDateTime start3 = LocalDateTime.of(2025, 6, 22, 9, 0);
    LocalDateTime end3 = LocalDateTime.of(2025, 6, 22, 10, 0);

    String createEventSeriesByRange = " event \"Event Nine\"" +
            " from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-06-22";
    CreateCommand createCommand = new CreateCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommand.execute();
  }

  @Before
  public void testCreateEventSeriesByInstances() {
    String createEventSeriesByInstances = " event \"Event Twelve\"" +
            " on 2025-06-10 repeats T for 4 times";
    CreateCommand createCommand = new CreateCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommand.execute();
  }

  @Test
  public void testInvalidCreateEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateCommand createCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    assertThrows(IllegalArgumentException.class, () -> {
      createCommand.execute();
    });
  }

  @Test
  public void testInvalidCreateEventSeries() {
    String createEventSeriesByRange = " event \"Event Nine\"" +
            " from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-07-01";
    CreateCommand createCommand1 = new CreateCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommand1.execute();
    assertThrows(IllegalArgumentException.class, () -> {
      createCommand1.execute();
    });

    String createEventSeriesByInstances = " event \"Event Twelve\"" +
            " on 2025-08-12 repeats T for 4 times";
    CreateCommand createCommand2 = new CreateCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommand2.execute();
    assertThrows(IllegalArgumentException.class, () -> {
      createCommand2.execute();
    });
  }

  @Test
  public void testInvalidWeekday() {
    String invalidWeekday = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats a until 2025-07-01";
    CreateCommand createCommand = new CreateCommand(invalidWeekday,
            calendarManagement, calendarView);
    createCommand.execute();

    String simulatedInput = "Invalid command: create event \"Event Nine\"" +
            " from 2025-06-08T08:00 to "
            + "2025-06-08T09:00 repeats a until 2025-07-01\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);
    String firstLine = scanner.nextLine();
    scanner.close();

    assertEquals("Invalid command: create event \"Event Nine\" from 2025-06-08T08:00 to "
                        + "2025-06-08T09:00 repeats a until 2025-07-01", firstLine);
  }
}
