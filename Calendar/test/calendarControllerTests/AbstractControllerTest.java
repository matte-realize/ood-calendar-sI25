package calendarControllerTests;

import org.junit.Before;

import java.time.ZoneId;

import controller.CalendarController;
import model.calendar.CalendarManagement;
import view.CalendarView;

public abstract class AbstractControllerTest {
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
