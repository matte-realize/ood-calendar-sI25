package calendarControllerTests.calendarTesting;

import org.junit.Test;

import controller.CreateCommand;
import controller.QueryCommand;

/**
 * A JUnit test that tests for the controller being able to query calendars.
 */
public class CalendarControllerQueryCalendarTest extends AbstractControllerCalendarTest {
  @Test
  public void testUseCalendar() {
    String createCalendar1 = " calendar --name cal1 --timezone Europe/Paris";
    CreateCommand createCommand1 = new CreateCommand(createCalendar1,
            calendarManagement, calendarView);
    createCommand1.execute();

    String useCalendar = "use calendar --name cal1";

    QueryCommand useCommand = new QueryCommand(useCalendar, "",
            calendarManagement, calendarView);
    useCommand.execute();
  }

  @Test
  public void testSwitchCalendar() {
    String createCalendar1 = " calendar --name cal1 --timezone Europe/Paris";
    CreateCommand createCommand1 = new CreateCommand(createCalendar1,
            calendarManagement, calendarView);
    createCommand1.execute();

    String createCalendar2 = " calendar --name cal2 --timezone Europe/Paris";
    CreateCommand createCommand2 = new CreateCommand(createCalendar2,
            calendarManagement, calendarView);
    createCommand2.execute();

    String useCalendar1 = "use calendar --name cal1";
    String useCalendar2 = "use calendar --name cal2";

    QueryCommand useCommand1 = new QueryCommand(useCalendar1, "",
            calendarManagement, calendarView);
    useCommand1.execute();

    QueryCommand useCommand2 = new QueryCommand(useCalendar2, "",
            calendarManagement, calendarView);
    useCommand2.execute();
  }
}
