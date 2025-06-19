package calendarcontrollertests.eventtesting;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.CopyEventCommand;
import controller.CreateCommand;
import controller.QueryCommand;
import model.calendar.Calendar;
import model.event.Event;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests for the controller being able to copy events.
 */
public class CalendarControllerCopyEventTest extends AbstractControllerEventTest {

  @Before
  public void setUp() {
    calendarManagement.selectCalendar("test");

    String createCalendar = " calendar --name newCalendar --timezone Europe/Paris";
    CreateCommand createCommand = new CreateCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();

    createCalendar = " calendar --name emptyCalendar --timezone America/New_York";
    createCommand = new CreateCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();

    createCalendar = " calendar --name dupeTimeZone --timezone America/New_York";
    createCommand = new CreateCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();

    String createEvent = " event \"Test Event 1\" on 2005-12-12";
    CreateCommand createEventCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);

    createEventCommand.execute();

    createEvent = " event \"Test Event 2\" on 2005-12-13";
    createEventCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);

    createEventCommand.execute();

    createEvent = " event \"Test Event 3\" on 2005-10-17 repeats M for 5 times";
    createEventCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);

    createEventCommand.execute();
  }

  @Test
  public void testCopySingleEvent() {
    String copyEvent = " event \"Test Event 1\" on 2005-12-12T08:00 "
            + "--target emptyCalendar to 2005-12-14T10:00";
    CopyEventCommand copyEventCommand = new CopyEventCommand(copyEvent,
            calendarManagement, calendarView);

    copyEventCommand.execute();

    String selectCalendar = " calendar --name emptyCalendar";
    QueryCommand selectCalendarCommand = new QueryCommand(selectCalendar,
            "use", calendarManagement, calendarView);

    selectCalendarCommand.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();

    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent = testCalendar.getEventsSingleDay(LocalDate.parse("2005-12-14"));

    assertEquals(1, testEvent.size());
    assertEquals("Test Event 1", testEvent.get(0).getSubject());
    assertEquals(LocalDateTime.parse("2005-12-14T10:00"),
            testEvent.get(0).getStartDateTime());
  }

  @Test
  public void testCopyEventsDifferentTimeZones() {
    String copyEvent = " events on 2005-10-17 --target newCalendar to 2005-11-17";
    CopyEventCommand copyEventCommand = new CopyEventCommand(copyEvent,
            calendarManagement, calendarView);

    copyEventCommand.execute();

    String selectCalendar = " calendar --name newCalendar";
    QueryCommand selectCalendarCommand = new QueryCommand(selectCalendar,
            "use", calendarManagement, calendarView);

    selectCalendarCommand.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();

    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent = testCalendar.getEventsSingleDay(LocalDate.parse("2005-11-17"));

    assertEquals(1, testEvent.size());
    assertEquals("Test Event 3", testEvent.get(0).getSubject());
    assertEquals(LocalDateTime.parse("2005-11-17T14:00"),
            testEvent.get(0).getStartDateTime());
  }

  @Test
  public void testCopyEventsSameTimeZone() {
    String copyEvent = " events on 2005-10-17 --target dupeTimeZone to 2005-11-17";
    CopyEventCommand copyEventCommand = new CopyEventCommand(
            copyEvent,
            calendarManagement,
            calendarView
    );

    copyEventCommand.execute();

    String selectCalendar = " calendar --name dupeTimeZone";
    QueryCommand selectCalendarCommand = new QueryCommand(
            selectCalendar,
            "use",
            calendarManagement,
            calendarView
    );

    selectCalendarCommand.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();

    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent = testCalendar.getEventsSingleDay(LocalDate.parse("2005-11-17"));

    assertEquals(1, testEvent.size());
    assertEquals("Test Event 3", testEvent.get(0).getSubject());
    assertEquals(LocalDateTime.parse("2005-11-17T08:00"),
            testEvent.get(0).getStartDateTime());
  }

  @Test
  public void testCopyEventsDifferentTimeZone() {
    String copyEvent = " events on 2005-10-17 --target emptyCalendar to 2005-11-17";
    CopyEventCommand copyEventCommand = new CopyEventCommand(
            copyEvent,
            calendarManagement,
            calendarView
    );

    copyEventCommand.execute();

    String selectCalendar = " calendar --name emptyCalendar";
    QueryCommand selectCalendarCommand = new QueryCommand(
            selectCalendar,
            "use",
            calendarManagement,
            calendarView
    );

    selectCalendarCommand.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();

    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent = testCalendar.getEventsSingleDay(LocalDate.parse("2005-11-17"));

    assertEquals(1, testEvent.size());
    assertEquals("Test Event 3", testEvent.get(0).getSubject());
    assertEquals(LocalDateTime.parse("2005-11-17T08:00"),
            testEvent.get(0).getStartDateTime());
  }

  @Test
  public void testCopyEventsInSameTimeZone() {
    String selectCalendar = " calendar --name emptyCalendar";
    QueryCommand selectCalendarCommand = new QueryCommand(
            selectCalendar,
            "use",
            calendarManagement,
            calendarView
    );

    selectCalendarCommand.execute();


    String createEvent = " event \"Event One\" from 2025-12-12T09:00 to 2025-12-12T10:00";
    CreateCommand createEventCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);

    createEventCommand.execute();

    String createEvent2 = " event \"Event Two\" from 2025-12-13T11:00 to 2025-12-13T15:00";
    CreateCommand createEventCommand2 = new CreateCommand(createEvent2,
            calendarManagement, calendarView);

    createEventCommand2.execute();

    String createEvent3 = " event \"Event Three\" from 2025-12-17T12:00 to 2025-12-17T14:00";
    CreateCommand createEventCommand3 = new CreateCommand(createEvent3,
            calendarManagement, calendarView);

    createEventCommand3.execute();

    String copyEvent = " events between 2025-12-12 and 2025-12-17 "
            + "--target dupeTimeZone to 2025-12-20";
    CopyEventCommand copyEventCommand = new CopyEventCommand(
            copyEvent,
            calendarManagement,
            calendarView
    );

    copyEventCommand.execute();

    String selectCalendarDupe = " calendar --name dupeTimeZone";
    QueryCommand selectCalendarCommandDupe = new QueryCommand(
            selectCalendarDupe,
            "use",
            calendarManagement,
            calendarView
    );

    selectCalendarCommandDupe.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();

    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent = testCalendar.getEventsSingleDay(LocalDate.parse("2025-12-20"));

    assertEquals(3, testEvent.size());

    String printEvent = " events on 2025-12-20";
    QueryCommand printCommand = new QueryCommand(
            printEvent,
            "print",
            calendarManagement,
            calendarView
    );
    printCommand.execute();

    String simulatedInput =
            "Events found on 2025-12-20:\n"
                    + "* Event Two from 2025-12-20T11:00 to 2025-12-20T15:00\n"
                    + "* Event One from 2025-12-20T09:00 to 2025-12-20T10:00\n"
                    + "* Event Three from 2025-12-20T12:00 to 2025-12-20T14:00";


    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);

    List<String> allLines = new ArrayList<>();

    while (scanner.hasNextLine()) {
      allLines.add(scanner.nextLine());
    }

    scanner.close();

    assertEquals("* Event Two from 2025-12-20T11:00 to 2025-12-20T15:00", allLines.get(1));
    assertEquals("* Event One from 2025-12-20T09:00 to 2025-12-20T10:00", allLines.get(2));
    assertEquals("* Event Three from 2025-12-20T12:00 to 2025-12-20T14:00", allLines.get(3));
  }

  @Test
  public void testCopyEventsInRangeDifferentTimeZone() {
    String createEvent = " event \"Event One\" from 2025-12-12T09:00 to 2025-12-12T10:00";
    CreateCommand createEventCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);

    createEventCommand.execute();

    String createEvent2 = " event \"Event Two\" from 2025-12-13T11:00 to 2025-12-13T15:00";
    CreateCommand createEventCommand2 = new CreateCommand(createEvent2,
            calendarManagement, calendarView);

    createEventCommand2.execute();

    String createEvent3 = " event \"Event Three\" from 2025-12-17T12:00 to 2025-12-17T14:00";
    CreateCommand createEventCommand3 = new CreateCommand(createEvent3,
            calendarManagement, calendarView);

    createEventCommand3.execute();

    String copyEvent = " events between 2025-12-12 and 2025-12-17 "
            + "--target newCalendar to 2025-12-20";
    CopyEventCommand copyEventCommand = new CopyEventCommand(
            copyEvent,
            calendarManagement,
            calendarView
    );

    copyEventCommand.execute();

    String selectCalendar = " calendar --name newCalendar";
    QueryCommand selectCalendarCommand = new QueryCommand(
            selectCalendar,
            "use",
            calendarManagement,
            calendarView
    );

    selectCalendarCommand.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();

    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent = testCalendar.getEventsSingleDay(LocalDate.parse("2025-12-20"));

    assertEquals(3, testEvent.size());

    String printEvent = " events on 2025-12-20";
    QueryCommand printCommand = new QueryCommand(
            printEvent,
            "print",
            calendarManagement,
            calendarView
    );
    printCommand.execute();

    String simulatedInput =
            "Events found on 2025-12-20:\n"
                    + "* Event Two from 2025-12-20T17:00 to 2025-12-20T21:00\n"
                    + "* Event One from 2025-12-20T15:00 to 2025-12-20T16:00\n"
                    + "* Event Three from 2025-12-20T18:00 to 2025-12-20T20:00";


    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);

    List<String> allLines = new ArrayList<>();

    while (scanner.hasNextLine()) {
      allLines.add(scanner.nextLine());
    }

    scanner.close();

    assertEquals("* Event Two from 2025-12-20T17:00 to 2025-12-20T21:00", allLines.get(1));
    assertEquals("* Event One from 2025-12-20T15:00 to 2025-12-20T16:00", allLines.get(2));
    assertEquals("* Event Three from 2025-12-20T18:00 to 2025-12-20T20:00", allLines.get(3));
  }
}
