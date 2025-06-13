package calendarcontrollertests;

import org.junit.Before;
import org.junit.Test;

import controller.CalendarController;
import model.calendar.CalendarManagement;
import view.CalendarView;

/**
 * A JUnit test that tests for the operations performed within the controller.
 */
public class CalendarControllerTest {

  @Before
  public void setup() {

    CalendarController controller;
    CalendarManagement management;
    CalendarView view;

    management = new CalendarManagement();
    view = new CalendarView();
    controller = new CalendarController(management, view);
  }

//  @Test
//  public void testCorrectParsing() {
//  }
//
//  @Test
//  public void testErrorTermination() {
//
//  }
//
//  @Test
//  public void testModelInteraction() {
//
//  }
//
//  @Test
//  public void testValidDateRange() {
//
//  }
}
