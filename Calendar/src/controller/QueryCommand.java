package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.calendar.Calendar;
import model.calendar.CalendarManagement;
import model.event.Event;
import view.CalendarView;

/**
 * A command that extends the abstract command class which allows for the user
 * to be able to query events through the calendar controller.
 */
public class QueryCommand extends AbstractCommand {
  private static final Pattern PrintEvents = Pattern.compile(
          "^print events on (\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern PrintEventsWindow = Pattern.compile(
          "^print events from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) "
                  + "to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private static final Pattern ShowStatus = Pattern.compile(
          "^show status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private static final Pattern UseCalendar = Pattern.compile(
          "^use calendar --name ([^\"]+)$");

  private final String tokensString;
  private CalendarManagement calendarModel;
  private CalendarView calendarView;
  private final Calendar selectedCalendar;

  /**
   * Constructor for the query command.
   *
   * @param tokensString  a string that determines the token.
   * @param command       a string of the command.
   * @param calendarModel a calendar model.
   * @param calendarView  the calendar view.
   */
  public QueryCommand(String tokensString,
                      String command,
                      CalendarManagement calendarModel,
                      CalendarView calendarView) {
    this.tokensString = command + tokensString;
    this.calendarModel = calendarModel;
    this.calendarView = calendarView;
    this.selectedCalendar = calendarModel.getSelectedCalendar();
  }

  @Override
  public void execute() {
    Matcher m;

    if ((m = PrintEvents.matcher(tokensString)).matches()) {
      handlePrintEventsOn(m);
    } else if ((m = PrintEventsWindow.matcher(tokensString)).matches()) {
      handlePrintEventsFromTo(m);
    } else if ((m = ShowStatus.matcher(tokensString)).matches()) {
      handleShowStatusOn(m);
    } else if ((m = UseCalendar.matcher(tokensString)).matches()) {
      handelUseCalendar(m);
    } else {
      calendarView.printError("Invalid query command: \"" + tokensString + "\"\n");
    }
  }

  private void handlePrintEventsOn(Matcher m) {
    String date = m.group(1);

    if (!isValidDate(date)) {
      calendarView.printError("Invalid date format. Expected format: yyyy-MM-dd");
      return;
    }

    calendarView.printEvents(
            selectedCalendar.getEventsSingleDay(LocalDate.parse(date)),
            date);
  }

  private void handlePrintEventsFromTo(Matcher m) {
    String startDate = m.group(1);
    String endDate = m.group(2);

    if (!isValidDateTime(startDate) || !isValidDateTime(endDate)) {
      calendarView.printError("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
      return;
    }

    calendarView.printEvents(
            selectedCalendar.getEventsWindow(
                    LocalDateTime.parse(startDate), LocalDateTime.parse(endDate)
            ),
            startDate + " to " + endDate);

  }

  private void handleShowStatusOn(Matcher m) {
    String dateTime = m.group(1);

    if (!isValidDateTime(dateTime)) {
      calendarView.printError("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
      return;
    }

    List<Event> events = selectedCalendar.getEventsWindow(
            LocalDateTime.parse(dateTime), LocalDateTime.parse(dateTime)
    );
    if (events.isEmpty()) {
      calendarView.printStatus("Available", dateTime);
    } else {
      calendarView.printStatus("Busy", dateTime);
    }
  }

  private void handelUseCalendar(Matcher m) {
    String calendarName = m.group(1);

    calendarModel.selectCalendar(calendarName);
  }
}
