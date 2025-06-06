package calendartests;

import org.junit.Before;

import Model.Calendar;

/**
 * An abstract JUnit test class designed to set up and test all calendar
 * functionalities that implement the Calendar interface.
 */
public abstract class AbstractCalendarTest {
  protected Calendar calendar;

  @Before
  public void setup() {
    calendar = new Calendar();
  }
}
