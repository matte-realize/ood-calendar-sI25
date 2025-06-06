package Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Calendar;
import Model.Event;
import View.CalendarView;

/**
 * A command that extends the abstract command class which allows for the user
 * to be able to query events through the calendar controller.
 */
public class QueryEventCommand extends AbstractCommand {
  private static final Pattern PrintEvents = Pattern.compile(
          "^print events on (\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern PrintEventsWindow = Pattern.compile(
          "^print events from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private static final Pattern ShowStatus = Pattern.compile(
          "^show status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private final String tokensString;
  private Calendar calendarModel;
  private CalendarView calendarView;

  public QueryEventCommand(String tokensString, String command, Calendar calendarModel, CalendarView calendarView) {
    this.tokensString = command + tokensString;
    this.calendarModel = calendarModel;
    this.calendarView = calendarView;
  }

  @Override
  public void execute() throws IllegalArgumentException {

    Matcher m;

    if ((m = PrintEvents.matcher(tokensString)).matches()) {
      handlePrintEventsOn(m);
    } else if ((m = PrintEventsWindow.matcher(tokensString)).matches()) {
      handlePrintEventsFromTo(m);
    } else if ((m = ShowStatus.matcher(tokensString)).matches()) {
      handleShowStatusOn(m);
    } else {
      throw new IllegalArgumentException("Invalid query command: \"" + tokensString + "\"\n");
    }
  }

  private void handlePrintEventsOn(Matcher m) {
    String date = m.group(1);

    if (!isValidDate(date)) {
      throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
    }

    calendarView.printEvents(calendarModel.getEventsSingleDay(LocalDate.parse(date)), date);
  }

  private void handlePrintEventsFromTo(Matcher m) {
    String startDate = m.group(1);
    String endDate = m.group(2);

    if (!isValidDateTime(startDate) || !isValidDateTime(endDate)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    calendarView.printEvents(calendarModel.getEventsWindow(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate)), startDate + " to " + endDate);

  }

  private void handleShowStatusOn(Matcher m) {
    String dateTime = m.group(1);

    if (!isValidDateTime(dateTime)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    List<Event> events = calendarModel.getEventsWindow(LocalDateTime.parse(dateTime), LocalDateTime.parse(dateTime));
    if (events.isEmpty()) {
      calendarView.printStatus("Available", dateTime);
    } else {
      calendarView.printStatus("Busy", dateTime);
    }
  }
}
