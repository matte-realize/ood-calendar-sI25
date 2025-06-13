package calendarControllerTests;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;

import controller.CalendarController;
import model.calendar.CalendarManagement;
import model.calendar.CalendarModel;
import view.CalendarView;

/**
 * A JUnit test that tests for the operations performed within the controller.
 */
public class CalendarControllerTest {
  private CalendarController controller;
  private CalendarManagement management;
  private CalendarView view;

  @Before
  public void setup() {
    management = new CalendarManagement();
    view = new CalendarView();
    controller = new CalendarController(management, view);
  }

  @Test
  public void testCorrectParsing() {
  }

  @Test
  public void testErrorTermination() {

  }

  @Test
  public void testModelInteraction() {

  }

  @Test
  public void testValidDateRange() {

  }
}
