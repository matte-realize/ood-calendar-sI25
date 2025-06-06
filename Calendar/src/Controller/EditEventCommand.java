package Controller;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Calendar;

public class EditEventCommand extends AbstractCommand {

  private static final Pattern EditSingleEvent = Pattern.compile(
          "^edit event (subject|start|end|description|location|status) \"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) with (.+)$");

  private static final Pattern EditEvents = Pattern.compile(
          "^edit events (subject|start|end|description|location|status) \"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) with (.+)$");

  private static final Pattern EditEventSeries = Pattern.compile(
          "^edit series (subject|start|end|description|location|status) \"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) with (.+)$");

  private final String tokensString;
  private Calendar calendarModel;

  public EditEventCommand(String tokensString, Calendar calendarModel) {
    this.tokensString = "edit" + tokensString;
    this.calendarModel = calendarModel;
  }

  @Override
  public void execute() throws IllegalArgumentException {

    Matcher m;

    if ((m = EditSingleEvent.matcher(tokensString)).matches()) {
      handleEditSingleEvent(m);
    } else if ((m = EditEvents.matcher(tokensString)).matches()) {
      handleEditEventsFrom(m);
    } else if ((m = EditEventSeries.matcher(tokensString)).matches()) {
      handleEditSeries(m);
    } else {
      throw new IllegalArgumentException("Invalid edit command: \"" + tokensString + "\"\n");
    }
  }

  private void handleEditSingleEvent(Matcher m) {
    String property = m.group(1);
    String subject = m.group(2);
    String from = m.group(3);
    String to = m.group(4);
    String newValue = m.group(5);

    if (!isValidDateTime(from) || !isValidDateTime(to)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    if (!isValidNewValue(property, newValue)) {
      throw new IllegalArgumentException("Invalid new value format. Make sure the new value is of the same type as you are trying to edit");
    }

    //calendarModel.editEvent(subject, LocalDateTime.parse(from), );
  }

  private void handleEditEventsFrom(Matcher m) {
    String property = m.group(1);
    String subject = m.group(2);
    String from = m.group(3);
    String newValue = m.group(4);

    if (!isValidDateTime(from)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    if (!isValidNewValue(property, newValue)) {
      throw new IllegalArgumentException("Invalid new value format. Make sure the new value is of the same type as you are trying to edit");
    }

    System.out.printf("Editing SINGLE event \"%s\" (%s): %s -> %s%n",
            subject, from, property, newValue);
  }

  private void handleEditSeries(Matcher m) {
    String property = m.group(1);
    String subject = m.group(2);
    String from = m.group(3);
    String newValue = m.group(4);

    if (!isValidDateTime(from)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    if (!isValidNewValue(property, newValue)) {
      throw new IllegalArgumentException("Invalid new value format. Make sure the new value is of the same type as you are trying to edit");
    }

    System.out.printf("Editing SINGLE event \"%s\" (%s): %s -> %s%n",
            subject, from, property, newValue);
  }
}
