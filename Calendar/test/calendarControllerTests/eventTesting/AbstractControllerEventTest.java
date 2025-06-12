package calendarControllerTests.eventTesting;

import org.junit.Before;

import java.time.ZoneId;

import controller.CalendarController;
import model.calendar.CalendarManagement;
import view.CalendarView;

/**
 * An abstract test that sets up the baseline to be used for all controller tests after
 * creating and entering the calendar for testing the commands for events.
 */
public abstract class AbstractControllerEventTest {
  protected CalendarManagement calendarManagement;
  protected CalendarView calendarView;
  protected CalendarController controller;

  @Before
  public void setup() {
    calendarManagement = new CalendarManagement();
    calendarView = new CalendarView();
    controller = new CalendarController(calendarManagement, calendarView);

    calendarManagement.createCalendar("test", ZoneId.of("America/New_York"));
    calendarManagement.selectCalendar("test");
  }
}
