package calendarControllerTests.calendarTesting;

import org.junit.Test;

import controller.eventCommands.CreateEventCommand;
import controller.eventCommands.QueryEventCommand;

/**
 * A JUnit test that tests for the controller being able to query calendars.
 */
public class CalendarControllerQueryCalendarTest extends AbstractControllerCalendarTest {
  @Test
  public void testUseCalendar() {
    String createCalendar1 = " calendar --name cal1 --timezone Europe/Paris";
    CreateEventCommand createCommand1 = new CreateEventCommand(createCalendar1,
            calendarManagement, calendarView);
    createCommand1.execute();

    String useCalendar = "use calendar --name cal1";

    QueryEventCommand useCommand = new QueryEventCommand(useCalendar, "", calendarManagement, calendarView);
    useCommand.execute();
  }

  @Test
  public void testSwitchCalendar() {
    String createCalendar1 = " calendar --name cal1 --timezone Europe/Paris";
    CreateEventCommand createCommand1 = new CreateEventCommand(createCalendar1,
            calendarManagement, calendarView);
    createCommand1.execute();

    String createCalendar2 = " calendar --name cal2 --timezone Europe/Paris";
    CreateEventCommand createCommand2 = new CreateEventCommand(createCalendar2,
            calendarManagement, calendarView);
    createCommand2.execute();

    String useCalendar1 = "use calendar --name cal1";
    String useCalendar2 = "use calendar --name cal2";

    QueryEventCommand useCommand1 = new QueryEventCommand(useCalendar1, "", calendarManagement, calendarView);
    useCommand1.execute();

    QueryEventCommand useCommand2 = new QueryEventCommand(useCalendar2, "", calendarManagement, calendarView);
    useCommand2.execute();
  }
}
