package calendarcontrollertests.eventtesting;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.CreateCommand;
import controller.EditCommand;
import controller.QueryCommand;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests for the controller being able to query events.
 */
public class CalendarControllerQueryEventTest extends AbstractControllerEventTest {
  @Test
  public void testPrintEventsOnADay() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateCommand createCommand = new CreateCommand(
            createEvent,
            calendarManagement,
            calendarView
    );
    createCommand.execute();

    String printEvent = " events on 2025-08-10";
    QueryCommand printCommand = new QueryCommand(
            printEvent,
            "print",
            calendarManagement,
            calendarView
    );
    printCommand.execute();

    String simulatedInput =
            "Events found on 2025-08-10:\n"
                    + "* Event One from 2025-08-10T09:00 to 2025-08-10T10:00\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);

    if (scanner.hasNextLine()) {
      scanner.nextLine();
    }

    String secondLine = "";
    if (scanner.hasNextLine()) {
      secondLine = scanner.nextLine();
    }

    scanner.close();

    assertEquals("* Event One from 2025-08-10T09:00 to 2025-08-10T10:00", secondLine);
  }

  @Test
  public void testPrintEventSeriesRange() {
    String createEventSeriesByRange = " event \"Event Nine\" from "
            + "2025-06-08T08:00 to 2025-06-08T09:00 repeats U until 2025-07-01";
    CreateCommand createCommandByRange = new CreateCommand(
            createEventSeriesByRange,
            calendarManagement,
            calendarView
    );
    createCommandByRange.execute();

    String printEventOnRange = " events from 2025-06-08T00:00 to 2025-06-22T00:00";
    QueryCommand printCommand = new QueryCommand(
            printEventOnRange,
            "print",
            calendarManagement,
            calendarView
    );
    printCommand.execute();

    String simulatedInput =
            "Events found on 2025-06-08T00:00 to 2025-06-22T00:00:\n"
                    + "* Event Nine from 2025-06-15T08:00 to 2025-06-15T09:00\n"
                    + "* Event Nine from 2025-06-08T08:00 to 2025-06-08T09:00\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);

    List<String> allLines = new ArrayList<>();

    while (scanner.hasNextLine()) {
      allLines.add(scanner.nextLine());
    }

    scanner.close();

    assertEquals("* Event Nine from 2025-06-15T08:00 to 2025-06-15T09:00", allLines.get(1));
    assertEquals("* Event Nine from 2025-06-08T08:00 to 2025-06-08T09:00", allLines.get(2));
  }

  @Test
  public void testPrintEventSeriesInstances() {
    String createEventSeriesByInstances = " event \"Event Twelve\" on "
            + "2025-08-12 repeats T for 4 times";
    CreateCommand createCommandByInstances = new CreateCommand(
            createEventSeriesByInstances,
            calendarManagement,
            calendarView
    );
    createCommandByInstances.execute();

    String printEventOnRange = " events from 2025-08-12T00:00 to 2025-09-09T00:00";
    QueryCommand printCommand = new QueryCommand(
            printEventOnRange,
            "print",
            calendarManagement,
            calendarView
    );
    printCommand.execute();

    String simulatedInput =
            "Events found on 2025-08-12T00:00 to 2025-09-09T00:00:\n"
                    + "* Event Twelve from 2025-08-12T08:00 to 2025-08-12T17:00\n"
                    + "* Event Twelve from 2025-08-26T08:00 to 2025-08-26T17:00\n"
                    + "* Event Twelve from 2025-08-19T08:00 to 2025-08-19T17:00\n"
                    + "* Event Twelve from 2025-09-02T08:00 to 2025-09-02T17:00\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);

    List<String> allLines = new ArrayList<>();

    while (scanner.hasNextLine()) {
      allLines.add(scanner.nextLine());
    }

    scanner.close();

    assertEquals("* Event Twelve from 2025-08-12T08:00 to 2025-08-12T17:00", allLines.get(1));
    assertEquals("* Event Twelve from 2025-08-26T08:00 to 2025-08-26T17:00", allLines.get(2));
    assertEquals("* Event Twelve from 2025-08-19T08:00 to 2025-08-19T17:00", allLines.get(3));
    assertEquals("* Event Twelve from 2025-09-02T08:00 to 2025-09-02T17:00", allLines.get(4));
  }

  @Test
  public void testDisplayFree() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateCommand createCommand = new CreateCommand(createEvent, calendarManagement, calendarView);
    createCommand.execute();

    String showStatus = "show status on 2025-06-10T09:00";
    QueryCommand showStatusCommand = new QueryCommand(
            showStatus,
            "",
            calendarManagement,
            calendarView
    );
    showStatusCommand.execute();

    String simulatedInput = "Available on 2025-06-10T09:00\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);
    String available = scanner.nextLine();
    scanner.close();

    assertEquals("Available on 2025-06-10T09:00", available);
  }

  @Test
  public void testDisplayBusy() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateCommand createCommand = new CreateCommand(createEvent, calendarManagement, calendarView);
    createCommand.execute();

    String showStatus = "show status on 2025-08-10T09:00";
    QueryCommand showStatusCommand = new QueryCommand(
            showStatus,
            "",
            calendarManagement,
            calendarView
    );
    showStatusCommand.execute();

    String simulatedInput = "Busy on 2025-08-10T09:00\n";
    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);
    String busy = scanner.nextLine();
    scanner.close();

    assertEquals("Busy on 2025-08-10T09:00", busy);
  }

  @Test
  public void testDisplayChangedTimesFromTimezoneChange() {
    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateCommand createCommand = new CreateCommand(createEvent, calendarManagement, calendarView);
    createCommand.execute();

    String printEvent = " events on 2025-08-10";
    QueryCommand printCommand = new QueryCommand(
            printEvent,
            "print",
            calendarManagement,
            calendarView
    );
    printCommand.execute();

    String editCalendarTimeZone = " calendar --name test --property timezone America/Los_Angeles";
    EditCommand editCommand2 = new EditCommand(editCalendarTimeZone,
            calendarManagement, calendarView);
    editCommand2.execute();

    printCommand.execute();

    String simulatedInput =
            "Events found on 2025-08-10:\n"
                    + "* Event One from 2025-08-10T09:00 to 2025-08-10T10:00\n"
                    + "Events found on 2025-08-10:\n"
                    + "* Event One from 2025-08-10T06:00 to 2025-08-10T7:00";

    InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
    System.setIn(in);
    Scanner scanner = new Scanner(System.in);

    List<String> allLines = new ArrayList<>();

    while (scanner.hasNextLine()) {
      allLines.add(scanner.nextLine());
    }

    scanner.close();

    assertEquals("* Event One from 2025-08-10T09:00 to 2025-08-10T10:00", allLines.get(1));
    assertEquals("* Event One from 2025-08-10T06:00 to 2025-08-10T7:00", allLines.get(3));
  }
}
