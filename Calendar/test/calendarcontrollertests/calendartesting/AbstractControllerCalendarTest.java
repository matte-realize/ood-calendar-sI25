package calendarcontrollertests.calendartesting;

import org.junit.Before;

import controller.CalendarController;
import model.calendar.CalendarManagement;
import view.CalendarView;

/**
 * An abstract test that sets up the baseline with the calendar management and view
 * to be used for all controller tests for calendar commands.
 */
public abstract class AbstractControllerCalendarTest {
  protected CalendarManagement calendarManagement;
  protected CalendarView calendarView;
  protected CalendarController controller;

  @Before
  public void setup() {
    calendarManagement = new CalendarManagement();
    calendarView = new CalendarView();
    controller = new CalendarController(calendarManagement, calendarView);
  }
}
