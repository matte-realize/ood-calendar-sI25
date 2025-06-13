package calendarcontrollertests.eventtesting;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.CreateCommand;
import model.calendar.Calendar;
import model.event.Event;
import model.event.EventInterface;

import static org.junit.Assert.assertEquals;

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
    assertEquals("2025-08-10T09:00", event.getStartDateTime().toString());
    assertEquals("2025-08-10T10:00", event.getEndDateTime().toString());
  }

  @Test
  public void testCreateEventSeriesByRange() {
    LocalDateTime start1 = LocalDateTime.of(2025, 6, 8, 8, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 6, 8, 9, 0);
    LocalDateTime start2 = LocalDateTime.of(2025, 6, 15, 8, 0);
    LocalDateTime end2 = LocalDateTime.of(2025, 6, 15, 9, 0);
    LocalDateTime start3 = LocalDateTime.of(2025, 6, 22, 8, 0);
    LocalDateTime end3 = LocalDateTime.of(2025, 6, 22, 9, 0);

    String createEventSeriesByRange = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-06-22";
    CreateCommand createCommand = new CreateCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommand.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();
    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent1 = testCalendar.getEventsSingleDay(LocalDate.parse("2025-06-08"));
    assertEquals(1, testEvent1.size());
    assertEquals("Event Nine", testEvent1.get(0).getSubject());
    assertEquals(start1, testEvent1.get(0).getStartDateTime());
    assertEquals(end1, testEvent1.get(0).getEndDateTime());

    List<Event> testEvent2 = testCalendar.getEventsSingleDay(LocalDate.parse("2025-06-15"));
    assertEquals(1, testEvent2.size());
    assertEquals("Event Nine", testEvent2.get(0).getSubject());
    assertEquals(start2, testEvent2.get(0).getStartDateTime());
    assertEquals(end2, testEvent2.get(0).getEndDateTime());

    List<Event> testEvent3 = testCalendar.getEventsSingleDay(LocalDate.parse("2025-06-22"));
    assertEquals(1, testEvent3.size());
    assertEquals("Event Nine", testEvent3.get(0).getSubject());
    assertEquals(start3, testEvent3.get(0).getStartDateTime());
    assertEquals(end3, testEvent3.get(0).getEndDateTime());
  }

  @Test
  public void testCreateEventSeriesByInstances() {
    String createEventSeriesByInstances = " event \"Event Twelve\" on 2025-06-10 repeats T for 4 times";
    CreateCommand createCommand = new CreateCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommand.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();
    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent1 = testCalendar.getEventsSingleDay(LocalDate.parse("2025-06-10"));
    assertEquals(1, testEvent1.size());
    assertEquals("Event Twelve", testEvent1.get(0).getSubject());
    assertEquals(LocalDateTime.parse("2025-06-10T08:00"), testEvent1.get(0).getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-10T17:00"), testEvent1.get(0).getEndDateTime());

    List<Event> testEvent2 = testCalendar.getEventsSingleDay(LocalDate.parse("2025-06-17"));
    assertEquals(1, testEvent2.size());
    assertEquals("Event Twelve", testEvent2.get(0).getSubject());
    assertEquals(LocalDateTime.parse("2025-06-17T08:00"), testEvent2.get(0).getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-17T17:00"), testEvent2.get(0).getEndDateTime());

    List<Event> testEvent3 = testCalendar.getEventsSingleDay(LocalDate.parse("2025-06-24"));
    assertEquals(1, testEvent3.size());
    assertEquals("Event Twelve", testEvent3.get(0).getSubject());
    assertEquals(LocalDateTime.parse("2025-06-24T08:00"), testEvent3.get(0).getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-06-24T17:00"), testEvent3.get(0).getEndDateTime());
  }

  @Test
  public void testInvalidCreateDuplicateEvent() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateCommand createCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    String simulatedInput = "An event with the same subject, start, and end time already exists.\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);
    String line = scanner.nextLine();
    scanner.close();

    assertEquals("An event with the same subject, start, and end time already exists.", line);
  }

  @Test
  public void testInvalidEventWithEndDateBeforeStartDate() {
    String createEvent = " event \"Event One\" from 2025-08-10T10:00 to 2025-08-10T09:00";
    CreateCommand createCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);

    String simulatedInput = "End time can not be before or on start time.\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);
    String line = scanner.nextLine();
    scanner.close();

    assertEquals("End time can not be before or on start time.", line);
  }

  @Test
  public void testInvalidCreateEventSeries() {
    String createEventSeriesByRange = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats U until 2025-07-01";
    CreateCommand createCommand1 = new CreateCommand(createEventSeriesByRange,
            calendarManagement, calendarView);
    createCommand1.execute();

    String createEventSeriesByInstances = " event \"Event Twelve\" on 2025-08-12 repeats T for 4 times";
    CreateCommand createCommand2 = new CreateCommand(createEventSeriesByInstances,
            calendarManagement, calendarView);
    createCommand2.execute();

    String simulatedInput =
            "Subject and start time cannot be the same as an existing series!\n"
                    + "Subject and start time cannot be the same as an existing series!\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);

    List<String> allLines = new ArrayList<>();

    while (scanner.hasNextLine()) {
      allLines.add(scanner.nextLine());
    }

    scanner.close();

    assertEquals("Subject and start time cannot be the same as an existing series!", allLines.get(0));
    assertEquals("Subject and start time cannot be the same as an existing series!", allLines.get(1));
  }

  @Test
  public void testInvalidWeekday() {
    String invalidWeekday = " event \"Event Nine\" from 2025-06-08T08:00 to 2025-06-08T09:00"
            + " repeats a until 2025-07-01";
    CreateCommand createCommand = new CreateCommand(invalidWeekday,
            calendarManagement, calendarView);
    createCommand.execute();

    String simulatedInput = "Invalid command: create event \"Event Nine\" from 2025-06-08T08:00 to "
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
