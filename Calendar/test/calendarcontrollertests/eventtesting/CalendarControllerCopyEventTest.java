package calendarcontrollertests.eventtesting;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    CopyEventCommand copyEventCommand = new CopyEventCommand(copyEvent,
            calendarManagement, calendarView);

    copyEventCommand.execute();

    String selectCalendar = " calendar --name dupeTimeZone";
    QueryCommand selectCalendarCommand = new QueryCommand(selectCalendar,
            "use", calendarManagement, calendarView);

    selectCalendarCommand.execute();

    Calendar testCalendar = calendarManagement.getSelectedCalendar();

    assertEquals(calendarManagement.getSelectedCalendar().toString(), testCalendar.toString());

    List<Event> testEvent = testCalendar.getEventsSingleDay(LocalDate.parse("2005-11-17"));

    assertEquals(1, testEvent.size());
    assertEquals("Test Event 3", testEvent.get(0).getSubject());
    assertEquals(LocalDateTime.parse("2005-11-17T08:00"),
            testEvent.get(0).getStartDateTime());
  }

}
