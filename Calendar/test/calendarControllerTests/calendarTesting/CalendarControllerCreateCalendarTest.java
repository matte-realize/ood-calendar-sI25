package calendarControllerTests.calendarTesting;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;

import static org.junit.Assert.assertThrows;

/**
 * A JUnit test that tests for the controller being able to create calendars.
 */
public class CalendarControllerCreateCalendarTest extends AbstractControllerCalendarTest {
  @Test
  public void testCreateCalendar() {
    String createCalendar = " calendar --name test --timezone Europe/Paris";
    CreateEventCommand createCommand = new CreateEventCommand(createCalendar,
            calendarManagement, calendarView);

    createCommand.execute();
  }

  @Test
  public void testInvalidCreateCalendar() {
    String invalidTimeZone = " calendar --name invalid --timezone America/Shanghai";

    CreateEventCommand createCommand1 = new CreateEventCommand(invalidTimeZone,
            calendarManagement, calendarView);
    assertThrows(IllegalArgumentException.class, () -> {
      createCommand1.execute();
    });
  }
}
